package com.hualing.service;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.*;
import com.hualing.domain.*;
import com.hualing.repository.*;
import com.hualing.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Will on 22/07/2019.
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderWithDistributeRepository orderWithDistributeRepository;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private OrderDistributeSimpleRepository orderDistributeSimpleRepository;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StoreCommodityRepository storeCommodityRepository;

    @Autowired
    private StoreCommodityService storeCommodityService;

    @Autowired
    private OrderDistributeService orderDistributeService;

    @Autowired
    private OrderExpressService orderExpressService;

    @Autowired
    private OrgMoneyService orgMoneyService;

    @Autowired
    private OrderRequestService orderRequestService;

    @Autowired
    private OrgMoneyDetailService orgMoneyDetailService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ExpressDistributeRepository expressDistributeRepository;

    @Transactional
    public void save(Order order, UserClaim uc) throws CredentialException{
        order.setLastUpdatedTime(new Date());
        order.setLastUpdatedBy(uc.getId());
        Brand brand = brandService.getOnly();
        order.setBrand(brand);
        if(order.getId() == 0){
            Long orgId = order.getOrg().getId();
            int serial = getNewOrderNo(orgId);
            Org org = this.orgRepository.findById(orgId).get();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            String shortName = org.getShortName() == null? "": org.getShortName();
            String orderNo = shortName + sf.format(new Date()) + formatSerial(serial);
            order.setCreateDate(new Date());
            order.setOrderNo(orderNo);
            SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
            //match a discount
            if(StringUtils.nonNull(order.getOrderDate()) && StringUtils.nonNull(order.getYear()) && StringUtils.nonNull(order.getQuarter())){
                String orderDateStr = sf2.format(order.getOrderDate());
                try {
                    Discount discount = discountService.matchDiscount(order.getOrg().getId(), orderDateStr, order.getYear(), order.getQuarter(), order.getCommodityNo());
                    if(discount == null)
                        throw new CredentialException(50002, "无法匹配折扣!");
                    else order.setDiscount(discount.getDiscount());
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new CredentialException(50002, "无法匹配折扣!");
                }
            }
            int storeCount = storeCommodityService.count(order.getCommodityNo(), order.getSize(), order.getYear(), order.getQuarter());
            if(order.getQuantity() > storeCount)
                throw new CredentialException(50001, "库存不足，总库存： " + storeCount);
//            refuseOrderIfNoCommodity(order);
        } else {
            if(Constants.ORDER_STATUS_INIT.equals(order.getStatus())){
                //如果重新提交被拒绝订单，要删除订单的配货，发货信息
                clearOrder(order);

                Order oldOrder = orderRepository.findById(order.getId()).get();
                order.setCreateDate(oldOrder.getCreateDate());
            }
        }

        this.orderRepository.save(order);
    }

    private void clearOrder(Order order){
        if(order.getId() > 0 && order.getOrderNo() != null){
            //删除发货表
            OrderExpressCriteria orderExpressCriteria = new OrderExpressCriteria();
            orderExpressCriteria.setOrderNo(order.getOrderNo());
            orderExpressService.deleteAll(orderExpressCriteria);
            //发货配货映射表
            List<ExpressDistribute> edList = expressDistributeRepository.findByOrderNo(order.getOrderNo());
            if(edList.size() > 0 && edList.size() < 100){
                expressDistributeRepository.deleteAll(edList);
            }
            //删除配货表
            OrderDistributeCriteria disCriteria = new OrderDistributeCriteria();
            disCriteria.setOrderId(order.getId());
            orderDistributeService.delete(disCriteria);
        }
    }

    public Page<Order> query(OrderCriteria orderCriteria){
        return this.orderRepository.findAll(orderCriteria.buildSpecification(), orderCriteria.buildPageRequest());
    }

    public List<Order> queryAll(OrderCriteria orderCriteria){
        return this.orderRepository.findAll(orderCriteria.buildSpecification());
    }

    public Page<OrderWithDistribute> queryWithDistribute(OrderWithDistributeCriteria orderCriteria){
        Page<OrderWithDistribute> page = this.orderWithDistributeRepository.findAll(orderCriteria.buildSpecification(), orderCriteria.buildPageRequest());

        return page;
    }

    public Order get(Long id){
        return this.orderRepository.findById(id).get();
    }

    @Transactional
    public void saveList(List<Order> list, UserClaim uc) throws CredentialException {
        if(list == null || list.size() == 0)
            return;
        int serial = getNewOrderNo(list.get(0).getOrg().getId());
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        Org org = this.orgRepository.findById(list.get(0).getOrg().getId()).get();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Brand brand = brandService.getOnly();
        String shortName = org.getShortName() == null? "": org.getShortName();
        int i = 2;
        StringBuffer errMsg = new StringBuffer();
        for(Order order: list){
            if(!StringUtils.nonNull(order.getCommodityNo())){
                errMsg.append(i).append("行数据错误，货号为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getSize())){
                errMsg.append(i).append("行数据错误，尺码为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getOrderDate())){
                errMsg.append(i).append("行数据错误，订单日期为空").append("<br>");
            } else {
                if(!sf2.format(new Date()).equals(sf2.format(order.getOrderDate()))){
                    errMsg.append(i).append("行数据错误，订单日期不是当前日期").append("<br>");
                }
            }
            if(!StringUtils.nonNull(order.getYear())){
                errMsg.append(i).append("行数据错误，年份为空").append("<br>");
            }
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if(!(order.getYear() <= currentYear && order.getYear() >= currentYear - 10 )){
                errMsg.append(i).append("行数据错误，年份错误").append("<br>");
            }
            if(!("Q1".equals(order.getQuarter()) || "Q2".equals(order.getQuarter()) || "Q3".equals(order.getQuarter()) || "Q4".equals(order.getQuarter()))){
                errMsg.append(i).append("行数据错误，季节必须是Q1, Q2, Q3或Q4").append("<br>");
            }
            if(!StringUtils.nonNull(order.getDiscount())){
                errMsg.append(i).append("行数据错误，折扣为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getPrice())){
                errMsg.append(i).append("行数据错误，零售价为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getQuantity())){
                errMsg.append(i).append("行数据错误，数量为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getReceiver())){
                errMsg.append(i).append("行数据错误，收货人为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getReceiverPhone())){
                errMsg.append(i).append("行数据错误，联系电话为空").append("<br>");
            }

            if(!StringUtils.checkPhoneNumber(order.getReceiverPhone())){
                errMsg.append(i).append("行数据错误，联系电话不是电话号码").append("<br>");
            }

            if(!StringUtils.nonNull(order.getReceiverAddress())){
                errMsg.append(i).append("行数据错误，收货地址为空").append("<br>");
            }
            if(!StringUtils.nonNull(order.getExpress())){
                errMsg.append(i).append("行数据错误，快递为空").append("<br>");
            }
            boolean storeExist = false;
            if(order.getId() == 0){
                String orderNo = shortName + sf.format(new Date()) + formatSerial(serial);
                order.setOrderNo(orderNo);
                order.setBrand(brand);
                order.setCreateDate(new Date());
//                refuseOrderIfNoCommodity(order);
                storeExist = checkExistStore(order);
                if(order.getCommodityNo()!= null && order.getSize() != null && !storeExist){
                    errMsg.append(i).append("行数据错误，库存中没有该商品: ").append(order.getCommodityNo()).append(" ").append(order.getSize()).append("<br>");
                }
                if(StringUtils.nonNull(order.getCommodityNo()) && StringUtils.nonNull(order.getSize()) && StringUtils.nonNull(order.getYear()) && StringUtils.nonNull(order.getQuarter()) && storeExist){
                    if(!checkPrice(order))
                        errMsg.append(i).append("行数据错误，订单中商品零售价不正确").append("<br>");
                    //查询库存
                    int storeCount = storeCommodityService.count(order.getCommodityNo(), order.getSize(), order.getYear(), order.getQuarter());
                    if(storeCount < order.getQuantity()){
                        errMsg.append(i).append("行数据错误，库存不足，总库存： ").append(storeCount);
                    }
                }
            }

            //match a discount
            if(StringUtils.nonNull(order.getOrderDate()) && StringUtils.nonNull(order.getYear()) && StringUtils.nonNull(order.getQuarter())){
                String orderDateStr = sf2.format(order.getOrderDate());
                try {
                    Discount discount = discountService.matchDiscount(order.getOrg().getId(), orderDateStr, order.getYear(), order.getQuarter(), order.getCommodityNo());
                    if(discount == null)
                        errMsg.append(i).append("行数据错误，无法匹配折扣").append("<br>");
                    else order.setDiscount(discount.getDiscount());
                } catch (ParseException e) {
                    e.printStackTrace();
                    errMsg.append(i).append("行数据错误，无法匹配折扣").append(orderDateStr).append("<br>");
                }
            }

            order.setLastUpdatedTime(new Date());
            order.setLastUpdatedBy(uc.getId());
            if(Objects.isNull(order.getStatus()))
                order.setSize(Constants.ORDER_STATUS_INIT);
            serial ++;
            i++;
        }

        if(errMsg.length() > 0){
            throw new CredentialException(50001, errMsg.toString());
        } else {
            this.orderRepository.saveAll(list);
        }
    }

    @Transactional
    public void orderBackList(List<Order> list, UserClaim uc) throws CredentialException {
        for(int i = 0; i < list.size(); i++){
            Order info = list.get(i);
            OrderCriteria example = new OrderCriteria();
            example.setOrgId(info.getOrg().getId());
            example.setOrderDate(info.getOrderDate());
            example.setReceiver(info.getReceiver());
            example.setReceiverPhone(info.getReceiverPhone());
            example.setCommodityNo(info.getCommodityNo());
            example.setReceiverAddress(info.getReceiverAddress());
            example.setSizeNo(info.getSize());
            Optional<Order> opt = orderRepository.findOne(example.buildSpecification());


            if(opt == null || !opt.isPresent())
                throw new CredentialException(60001, (i+2) + "行出错：未找到该订单！");
            Order order = opt.get();
            if(order.getBackCount() == null)
                order.setBackCount(0);
            if(!Constants.ORDER_DISTRIBUTE_SENT.equals(order.getStatus()))
                throw new CredentialException(60001, (i+2) + "行出错：该订单状态不是已发货，无法退货！");
            else if(order.getQuantity() - order.getBackCount() < info.getQuantity())
                throw new CredentialException(60001, (i+2) + "行出错：退货数量大于可退货数量！");
            order.setStatus(Constants.ORDER_STATUS_WAIT_BACK);
            if(order.getBackCount() == null)
                order.setBackCount(0);
            order.setBackCount(order.getBackCount() + info.getQuantity());
            order.setLastUpdatedBy(uc.getId());
            order.setLastUpdatedTime(new Date());
            orderRequestService.request(order, info.getQuantity(), uc);
        }

    }

    private void refuseOrderIfNoCommodity(Order order){
        if(Constants.ORDER_STATUS_INIT.equals(order.getStatus())){
            StoreCommodityCriteria storeCommodityCriteria = new StoreCommodityCriteria();
            storeCommodityCriteria.setCommodityNo(order.getCommodityNo());
            storeCommodityCriteria.setActive(true);
            storeCommodityCriteria.setSizeNo(order.getSize());
            long count = this.storeCommodityRepository.count(storeCommodityCriteria.buildSpecification());
            if(count == 0){
                order.setStatus(Constants.ORDER_STATUS_REFUSE);
            }
        }
    }

    public boolean checkExistStore(Order order){
        StoreCommodityCriteria storeCommodityCriteria = new StoreCommodityCriteria();
        storeCommodityCriteria.setCommodityNo(order.getCommodityNo());
        storeCommodityCriteria.setActive(true);
        storeCommodityCriteria.setSizeNo(order.getSize());
        long count = this.storeCommodityRepository.count(storeCommodityCriteria.buildSpecification());
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPrice(Order order){
        StoreCommodityCriteria storeCommodityCriteria = new StoreCommodityCriteria();
        storeCommodityCriteria.setCommodityNo(order.getCommodityNo());
        storeCommodityCriteria.setActive(true);
        storeCommodityCriteria.setSizeNo(order.getSize());
        storeCommodityCriteria.setYear(order.getYear());
        storeCommodityCriteria.setQuarter(order.getQuarter());
        List<StoreCommodity> list = this.storeCommodityRepository.findAll(storeCommodityCriteria.buildSpecification());
        for(StoreCommodity com: list){
            if(com.getListPrice() == order.getPrice()){
                return true;
            }
        }
        return false;
    }

    public int getNewOrderNo(Long orgId){
        List<Object[]> result = orderRepository.getLatestOrderNo(orgId);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sf.format(new Date());
        int serial = 1;
        if(result != null && result.size() > 0 && result.get(0) != null){
            Org org = this.orgRepository.findById(orgId).get();
            String latestNo = (String)result.get(0)[0];
            String shortName = org.getShortName() == null? "": org.getShortName();
            if(dateStr.equals(latestNo.substring(latestNo.length() - 13, latestNo.length() - 5)))
                serial = Integer.parseInt(latestNo.substring(shortName.length() + 8, latestNo.length())) + 1;
        }
        return serial;
    }

    @Transactional
    public void orderConfirm(Order info, UserClaim uc) throws CredentialException {
        Order order = orderRepository.findById(info.getId()).get();
        List<OrderDistribute> distributeList = info.getDistributeList();
        if(!Constants.ORDER_STATUS_INIT.equals(order.getStatus()) && !Constants.ORDER_DISTRIBUTE_REFUSE.equals(order.getStatus()) && !Constants.ORDER_STATUS_CONFIRM.equals(order.getStatus())){
            throw new CredentialException(20001, "订单状态不是初始，拒绝调货，已确认，不能进行配货！");
        }
        //删除原有的配货表
        if(!Constants.ORDER_STATUS_INIT.equals(order.getStatus())){
            OrderDistributeCriteria disCriteria = new OrderDistributeCriteria();
            disCriteria.setOrderId(info.getId());
            List<OrderDistribute> oldDistributes = orderDistributeService.queryAll(disCriteria);
            List<OrderDistribute> delDistribuites = new ArrayList<OrderDistribute>();
            //回退库存
            List<StoreCommodity> backCommodityList = new ArrayList<StoreCommodity>();
            for(OrderDistribute distribute: oldDistributes){
                if(Constants.ORDER_DISTRIBUTE_WAIT_DELIVERY.equals(distribute.getStatus())) {
                    //等待调货状态下的需要将库存回退，重新配货
                    StoreCommodity storeCommodity = storeCommodityRepository.findById(distribute.getStoreCommodity().getId()).get();
                    if (storeCommodity != null) {
                        storeCommodity.setQuantity(storeCommodity.getQuantity() + distribute.getQuantity());
                        backCommodityList.add(storeCommodity);
                    }
                }

                if(Constants.ORDER_DISTRIBUTE_REFUSE.equals(distribute.getStatus()) || Constants.ORDER_DISTRIBUTE_WAIT_DELIVERY.equals(distribute.getStatus())){
                    //拒绝调货，等待调货 这两种状态下才需要删除
                    delDistribuites.add(distribute);
                }
            }
            if(backCommodityList.size() > 0)
                storeCommodityRepository.saveAll(backCommodityList);
            if(delDistribuites.size() > 0)
                orderDistributeService.deleteAll(delDistribuites);
        }

        //减库存
        List<StoreCommodity> storeCommodityList = new ArrayList<StoreCommodity>();
        for(OrderDistribute distribute: distributeList){
            StoreCommodity storeCommodity = storeCommodityRepository.findById(distribute.getStoreCommodity().getId()).get();
            if(storeCommodity == null){
                throw new CredentialException(20001, "未查询到库存！");
            } else {
                storeCommodity.setQuantity(storeCommodity.getQuantity() - distribute.getQuantity());
                if(storeCommodity.getQuantity() < 0){
                    throw new CredentialException(20001, storeCommodity.getStore().getName() + "的库存不足，请刷新页面，重新分配！");
                }
                storeCommodityList.add(storeCommodity);
            }

            //根据是否直发店铺设定分配状态
            if(distribute.getStore().getExpressRequired()){
                distribute.setStatus(Constants.ORDER_DISTRIBUTE_WAIT_SEND);
            } else {
                distribute.setStatus(Constants.ORDER_DISTRIBUTE_WAIT_DELIVERY);
            }
        }
        storeCommodityRepository.saveAll(storeCommodityList);

        //减机构余额
        if(Constants.ORDER_STATUS_INIT.equals(order.getStatus())) {
            OrgMoneyCriteria orgMoneyCriteria = new OrgMoneyCriteria();
            orgMoneyCriteria.setOrgId(order.getOrg().getId());
            Page<OrgMoney> orgMoneyPage = orgMoneyService.query(orgMoneyCriteria);
            if (orgMoneyPage.getTotalElements() == 0) {
                throw new CredentialException(20001, order.getOrg().getCompanyName() + "无机构余额信息，无法确认订单！");
            } else {
                OrgMoney orgMoney = orgMoneyPage.getContent().get(0);
                double orderSum = 0.00;
                if (StringUtils.nonNull(order.getSpecialDiscount()))
                    orderSum = order.getSpecialDiscount() * order.getQuantity() * order.getPrice();
                else orderSum = order.getDiscount() * order.getQuantity() * order.getPrice();
                if (orgMoney.getBalance() < orderSum) {
                    throw new CredentialException(20001, order.getOrg().getCompanyName() + "的机构余额不足，无法确认订单！");
                } else {
                    BigDecimal balance = new BigDecimal(orgMoney.getBalance() - orderSum);
//                    BigDecimal totalSum = new BigDecimal(orgMoney.getOrderSum() + orderSum);
                    orgMoney.setBalance(balance.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
//                    orgMoney.setOrderSum(totalSum.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                    orgMoneyService.save(orgMoney, uc);

                    OrgMoneyDetail detail = new OrgMoneyDetail();
                    detail.setAmount(orderSum);
                    detail.setBalance(orgMoney.getBalance());
                    detail.setDealType(Constants.DEAL_TYPE_CONSUME);
                    detail.setOrder(order);
                    detail.setOrg(order.getOrg());
                    orgMoneyDetailService.save(detail, uc);
                }
            }
        }

        //保存分配表
        orderDistributeService.saveList(distributeList, uc);
        //保存订单状态为已确认
        order.setStatus(Constants.ORDER_STATUS_CONFIRM);
        orderRepository.save(order);
    }

    @Transactional
    public void orderCancel(Long orderId, UserClaim uc) throws CredentialException {
        Order order = orderRepository.findById(orderId).get();
        if(order.getStatus() != Constants.ORDER_STATUS_REFUSE){
            if(Constants.ORDER_STATUS_INIT.equals(order.getStatus())){

            } else {
                //加机构余额
                OrgMoneyCriteria orgMoneyCriteria = new OrgMoneyCriteria();
                orgMoneyCriteria.setOrgId(order.getOrg().getId());
                Page<OrgMoney> orgMoneyPage = orgMoneyService.query(orgMoneyCriteria);
                if(orgMoneyPage.getTotalElements() == 0){
                    throw new CredentialException(20001, order.getOrg().getCompanyName() + "无机构余额信息，无法确认订单！");
                } else {
                    OrgMoney orgMoney = orgMoneyPage.getContent().get(0);
                    double orderSum = 0.00;
                    if(StringUtils.nonNull(order.getSpecialDiscount()))
                        orderSum = order.getSpecialDiscount() * order.getQuantity() * order.getPrice();
                    else orderSum = order.getDiscount() * order.getQuantity() * order.getPrice();

                    BigDecimal balance = new BigDecimal(orgMoney.getBalance() + orderSum);
                    orgMoney.setBalance(balance.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                    orgMoneyService.save(orgMoney, uc);

                    OrgMoneyDetail detail = new OrgMoneyDetail();
                    detail.setAmount(orderSum);
                    detail.setBalance(orgMoney.getBalance());
                    detail.setDealType(Constants.DEAL_TYPE_CANCEL);
                    detail.setOrder(order);
                    detail.setOrg(order.getOrg());
                    orgMoneyDetailService.save(detail, uc);
                }

                //加库存,将调货信息设置为已拒绝
                OrderDistributeCriteria disCriteria = new OrderDistributeCriteria();
                disCriteria.setOrderId(orderId);
                List<OrderDistribute> distributes = orderDistributeService.queryAll(disCriteria);
                for(OrderDistribute dis: distributes){
                    dis.setStatus(Constants.ORDER_STATUS_REFUSE);
                    //将库存加回去
                    StoreCommodity sc = dis.getStoreCommodity();
                    sc.setQuantity(sc.getQuantity() + dis.getQuantity());
                    storeCommodityService.save(sc, uc);
                }
                orderDistributeService.saveList(distributes, uc);

                //如果有发货信息，设置为已拒绝
                OrderExpressCriteria orderExpressCriteria = new OrderExpressCriteria();
                orderExpressCriteria.setOrderNo(order.getOrderNo());
                List<OrderExpress> expresses = orderExpressService.queryAll(orderExpressCriteria);
                if(expresses != null && expresses.size() > 0) {
                    for (OrderExpress express : expresses) {
                        express.setStatus(Constants.ORDER_STATUS_REFUSE);
                    }
                    orderExpressService.saveAll(expresses, uc);
                }

            }
            order.setStatus(Constants.ORDER_STATUS_REFUSE);
            save(order, uc);
        }
    }

    @Transactional
    public void orderBack(Order info, UserClaim uc) throws CredentialException {
        //退货处理
        Date date = new Date();
        Order order = orderRepository.findById(info.getId()).get();
        order.setStatus(Constants.ORDER_STATUS_BACK);
        order.setLastUpdatedBy(uc.getId());
        order.setLastUpdatedTime(date);
        orderRepository.save(order);
        //修改库存
        OrderDistributeCriteria orderDistributeCriteria = new OrderDistributeCriteria();
        orderDistributeCriteria.setOrderId(order.getId());
        List<OrderDistribute> distributeList = orderDistributeService.queryAll(orderDistributeCriteria);
        if(distributeList.size() > 0){
            List<OrderDistribute> orderDistributeBatch = new ArrayList<OrderDistribute>();
            for(OrderDistribute orderDistribute: distributeList){
                if(!Constants.ORDER_DISTRIBUTE_REFUSE.equals(orderDistribute.getStatus())){
                    orderDistribute.setStatus(Constants.ORDER_STATUS_BACK);
                    orderDistributeBatch.add(orderDistribute);
                    StoreCommodity storeCommodity = orderDistribute.getStoreCommodity();
                    storeCommodity.setQuantity(storeCommodity.getQuantity() + orderDistribute.getQuantity());
                    storeCommodityRepository.save(storeCommodity);
                }
            }
            if(orderDistributeBatch.size() > 0)
                orderDistributeService.saveList(orderDistributeBatch, uc);
        }
        //修改机构余额
        OrgMoneyCriteria orgMoneyCriteria = new OrgMoneyCriteria();
        orgMoneyCriteria.setOrgId(order.getOrg().getId());
        Page<OrgMoney> orgMoneyPage = orgMoneyService.query(orgMoneyCriteria);
        if(orgMoneyPage.getTotalElements() == 0){
            throw new CredentialException(20001, order.getOrg().getCompanyName() + "无机构余额信息，无法退货！");
        } else {
            OrgMoney orgMoney = orgMoneyPage.getContent().get(0);
            double orderSum = 0.00;
            if(StringUtils.nonNull(order.getSpecialDiscount()))
                orderSum = order.getSpecialDiscount() * order.getQuantity() * order.getPrice();
            else orderSum = order.getDiscount() * order.getQuantity() * order.getPrice();

            BigDecimal balance = new BigDecimal(orgMoney.getBalance() + orderSum);
//            BigDecimal totalSum = new BigDecimal(orgMoney.getOrderSum() - orderSum);
            orgMoney.setBalance(balance.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
//            if(totalSum.doubleValue() > 0)
//                orgMoney.setOrderSum(totalSum.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
            orgMoneyService.save(orgMoney, uc);
        }

    }

    @Transactional
    public void requestBack(Order info, UserClaim uc){
        Order order = orderRepository.findById(info.getId()).get();
        order.setStatus(Constants.ORDER_STATUS_WAIT_BACK);
        if(order.getBackCount() == null)
            order.setBackCount(0);
        order.setBackCount(order.getBackCount() + info.getCurrentBackCount());
        order.setLastUpdatedBy(uc.getId());
        order.setLastUpdatedTime(new Date());
        orderRequestService.request(order, info.getCurrentBackCount(), uc);
    }

    public void approveOrderRequest(Order order, int count, UserClaim uc) throws CredentialException {
        Date date = new Date();
        order.setStatus(Constants.ORDER_STATUS_BACK);
        order.setLastUpdatedBy(uc.getId());
        order.setLastUpdatedTime(date);
        orderRepository.save(order);

        //修改机构余额
        OrgMoneyCriteria orgMoneyCriteria = new OrgMoneyCriteria();
        orgMoneyCriteria.setOrgId(order.getOrg().getId());
        Page<OrgMoney> orgMoneyPage = orgMoneyService.query(orgMoneyCriteria);
        if(orgMoneyPage.getTotalElements() == 0){
            throw new CredentialException(20001, order.getOrg().getCompanyName() + "无机构余额信息，无法确认订单！");
        } else {
            OrgMoney orgMoney = orgMoneyPage.getContent().get(0);
            double backSum = order.getDiscount() * count * order.getPrice();

            BigDecimal balance = new BigDecimal(orgMoney.getBalance() + backSum);
//            BigDecimal totalSum = new BigDecimal(orgMoney.getOrderSum() - backSum);
            orgMoney.setBalance(balance.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
//            if(totalSum.doubleValue() > 0)
//                orgMoney.setOrderSum(totalSum.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
//            else orgMoney.setOrderSum(0.00);
            orgMoneyService.save(orgMoney, uc);

            OrgMoneyDetail detail = new OrgMoneyDetail();
            detail.setAmount(backSum);
            detail.setBalance(orgMoney.getBalance());
            detail.setDealType(Constants.DEAL_TYPE_BACK);
            detail.setOrder(order);
            detail.setOrg(order.getOrg());
            orgMoneyDetailService.save(detail, uc);
        }
    }

    private String formatSerial(int i){
        String s = "" + i;
        while(s.length() < 5){
            s = "0" + s;
        }
        return s;
    }

    public long count(OrderCriteria criteria){
        return this.orderRepository.count(criteria.buildSpecification());
    }
}
