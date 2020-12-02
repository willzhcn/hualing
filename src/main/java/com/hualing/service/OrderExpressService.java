package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderExpressCriteria;
import com.hualing.domain.*;
import com.hualing.repository.ExpressDistributeRepository;
import com.hualing.repository.OrderExpressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Will on 04/08/2019.
 */
@Service
public class OrderExpressService {
    @Autowired
    private OrderExpressRepository orderExpressRepository;

    @Autowired
    private ExpressDistributeRepository expressDistributeRepository;

    @Autowired
    private OrderDistributeService orderDistributeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExpressService expressService;

    @Transactional
    public void saveList(List<OrderExpress> list, UserClaim uc){
        Date date = new Date();
        HashMap<Long, OrderDistribute> orderDistributeMap = new HashMap<Long, OrderDistribute>();

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String today = sf.format(new Date());
        String maxOrderNo = orderExpressRepository.getMaxOrderNo(today + "%");
        if(maxOrderNo == null){
            maxOrderNo = today + "00000";
        }
        Long orderNo = Long.valueOf(maxOrderNo);
        for(OrderExpress orderExpress: list){
            orderExpress.setLastUpdatedTime(date);
            orderExpress.setLastUpdatedBy(uc.getId());
            if(uc.getRole() == Constants.ROLE_STORE_SUPERVISOR){
                orderExpress.setStoreId(uc.getStoreId());
            }
            //韵达非直发店铺才发设置订单编号
            Express express = expressService.getExpress(orderExpress.getExpress().getId());
            if(express.getName().indexOf("韵达") >= 0 && uc.getRole() != Constants.ROLE_STORE_SUPERVISOR){
                orderExpress.setOrderNo((++ orderNo) + "");
            }
            OrderExpress result = orderExpressRepository.save(orderExpress);
            for(ExpressDistribute expressDistribute: orderExpress.getDistributeList()){
                expressDistribute.setLastUpdatedBy(uc.getId());
                expressDistribute.setLastUpdatedTime(date);
                expressDistribute.setOrderExpressId(result.getId());

                Long orderDistributeId = expressDistribute.getOrderDistribute().getId();
                OrderDistribute orderDistribute = orderDistributeMap.get(orderDistributeId);
                if(orderDistribute == null){
                    orderDistribute = orderDistributeService.get(orderDistributeId);
                    //检查订单状态
                    if(Constants.ORDER_STATUS_REFUSE.equals(orderDistribute.getStatus()))
                        throw new CredentialException(50001, orderDistribute.getOrder().getOrderNo() + "--" + orderDistribute.getStore().getName() + " 状态改变，订单已被取消！");
                    orderDistributeMap.put(orderDistributeId, orderDistribute);
                }
                expressDistribute.setOrderNo(orderDistribute.getOrder().getOrderNo());
            }

            Iterator<Long> it = orderDistributeMap.keySet().iterator();
            List<OrderDistribute> orderDistributeList = new ArrayList<OrderDistribute>();
            while(it.hasNext()){
                Long id = it.next();
                OrderDistribute content = orderDistributeMap.get(id);
                content.setStatus(Constants.ORDER_DISTRIBUTE_PREPARED);
                orderDistributeList.add(content);
            }
            orderDistributeService.saveList(orderDistributeList, uc);
            expressDistributeRepository.saveAll(orderExpress.getDistributeList());
        }
    }


    public void saveAll(List<OrderExpress> list, UserClaim uc){
        if(list.size() > 0){
            for(OrderExpress orderExpress: list){
                orderExpress.setLastUpdatedBy(uc.getId());
                orderExpress.setLastUpdatedTime(new Date());
            }
            orderExpressRepository.saveAll(list);
        }
    }

    public OrderExpress get(Long id){
        return orderExpressRepository.findById(id).get();
    }

    public Page<OrderExpress> query(OrderExpressCriteria criteria){
        return orderExpressRepository.findAll(criteria.buildSpecification(), criteria.buildPageRequest());
    }

    public List<OrderExpress> queryAll(OrderExpressCriteria criteria){
        return orderExpressRepository.findAll(criteria.buildSpecification());
    }

    @Transactional
    public void send(OrderExpress info, UserClaim uc) throws CredentialException {
        OrderExpress orderExpress = get(info.getId());
        if(!Constants.ORDER_DISTRIBUTE_PREPARED.equals(orderExpress.getStatus())){
            throw new CredentialException(40001, "快递状态不是预处理完成，无法进行发货！");
        }
        Date date = new Date();
        orderExpress.setExpressNo(info.getExpressNo());
        orderExpress.setStatus(Constants.ORDER_DISTRIBUTE_SENT);
        orderExpress.setLastUpdatedBy(uc.getId());
        orderExpress.setLastUpdatedTime(date);
        orderExpressRepository.save(orderExpress);
        List<ExpressDistribute> distributeList = orderExpress.getDistributeList();
        HashMap<Long, Boolean> distributeMap = new HashMap<Long, Boolean>();
        HashMap<Long, Order> orderMap = new HashMap<Long, Order>();
        for(ExpressDistribute expressDistribute: distributeList){
            OrderDistribute orderDistribute = expressDistribute.getOrderDistribute();
            if(distributeMap.get(orderDistribute.getId()) == null){
                distributeMap.put(orderDistribute.getId(), true);

                List<Object[]> statusList = orderExpressRepository.getAllExpressDistributeStatus(orderDistribute.getId());
                boolean flag = true;
                for(Object[] row: statusList){
                    if(!Constants.ORDER_DISTRIBUTE_SENT.equals(row[0])){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    orderDistribute.setStatus(Constants.ORDER_DISTRIBUTE_SENT);
                    orderDistributeService.save(orderDistribute, uc);
                }

                Long orderId = orderDistribute.getOrder().getId();
                if(orderMap.get(orderId) == null)
                    orderMap.put(orderId, orderDistribute.getOrder());
            }
        }

        //修改订单状态
        Iterator<Long> it = orderMap.keySet().iterator();
        while(it.hasNext()){
            Long orderId = it.next();
            List<Object[]> statusList = orderExpressRepository.getAllOrderDistributeStatus(orderId);
            boolean flag = true;
            for(Object[] row: statusList){
                if(!Constants.ORDER_DISTRIBUTE_SENT.equals(row[0]) && !Constants.ORDER_DISTRIBUTE_REFUSE.equals(row[0])){
                    flag = false;
                    break;
                }
            }
            Order order = orderMap.get(orderId);
            if(flag && !Constants.ORDER_DISTRIBUTE_REFUSE.equals(order.getStatus())){
                order.setStatus(Constants.ORDER_DISTRIBUTE_SENT);
                orderService.save(order, uc);
            }
        }
    }

    public void deleteAll(OrderExpressCriteria criteria){
        List<OrderExpress> list = orderExpressRepository.findAll(criteria.buildSpecification());
        if(list.size() > 0 && list.size() < 100){
            orderExpressRepository.deleteAll(list);
        }
    }

}
