package com.hualing.service;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderDistributeCriteria;
import com.hualing.domain.Order;
import com.hualing.domain.OrderDistribute;
import com.hualing.domain.StoreCommodity;
import com.hualing.repository.OrderDistributeRepository;
import com.hualing.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Will on 01/08/2019.
 */
@Service
public class OrderDistributeService {
    @Autowired
    private OrderDistributeRepository orderDistributeRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreCommodityService storeCommodityService;

    @Transactional
    public void saveList(List<OrderDistribute> list, UserClaim uc){
        if(list.size() == 0)
            return;
        for(OrderDistribute dis: list){
            dis.setLastUpdatedBy(uc.getId());
            dis.setLastUpdatedTime(new Date());
        }
        this.orderDistributeRepository.saveAll(list);
    }

    public Page<OrderDistribute> query(OrderDistributeCriteria criteria){
        return this.orderDistributeRepository.findAll(criteria.buildSpecification(), criteria.buildPageRequest());
    }

    public long count(OrderDistributeCriteria criteria){
        return this.orderDistributeRepository.count(criteria.buildSpecification());
    }

    public List<OrderDistribute> queryAll(OrderDistributeCriteria criteria){
        return this.orderDistributeRepository.findAll(criteria.buildSpecification());
    }

    public OrderDistribute get(long id){
        return this.orderDistributeRepository.findById(id).get();
    }

    @Transactional
    public void updateStatus(OrderDistribute info, UserClaim uc){
        OrderDistribute distribute = orderDistributeRepository.findById(info.getId()).get();
        distribute.setStatus(info.getStatus());
        distribute.setLastUpdatedBy(uc.getId());
        distribute.setLastUpdatedTime(new Date());
        orderDistributeRepository.save(distribute);
        if(Constants.ORDER_DISTRIBUTE_REFUSE.equals(info.getStatus())){
            Order order = distribute.getOrder();
            order.setStatus(info.getStatus());
            orderService.save(order, uc);
            //将库存加回去
            StoreCommodity sc = distribute.getStoreCommodity();
            sc.setQuantity(sc.getQuantity() + distribute.getQuantity());
            storeCommodityService.save(sc, uc);
        }
    }

    public void cancelAll(List<OrderDistribute> list, UserClaim uc) throws CredentialException {
        for(OrderDistribute dis: list){
            try{
                dis.setStatus(Constants.ORDER_DISTRIBUTE_REFUSE);
                updateStatus(dis, uc);
            } catch (Exception e){
                e.printStackTrace();
                throw new CredentialException(50001, "订单'" + dis.getOrder().getOrderNo() + "'调货失败：" + e.getMessage());
            }
        }
    }

    @Transactional
    public void deliverAll(ArrayList<Long> ids, UserClaim uc) throws CredentialException {
        try{
            List<OrderDistribute> list = this.orderDistributeRepository.findAllById(ids);
            for(OrderDistribute distribute: list){
//                if(!Constants.ORDER_DISTRIBUTE_WAIT_DELIVERY.equals(distribute.getStatus()))
//                    throw new CredentialException(40001, "一键调货失败：订单" + distribute.getOrder().getOrderNo() + "从" + distribute.getStore().getName() + "的配货状态不是等待调货，不能进行调货，请重新查询！");
                distribute.setStatus(Constants.ORDER_DISTRIBUTE_WAIT_SEND);
                distribute.setLastUpdatedBy(uc.getId());
                distribute.setLastUpdatedTime(new Date());
            }
            System.out.print(list.size());
            orderDistributeRepository.saveAll(list);
        } catch (Exception e){
            e.printStackTrace();
            throw new CredentialException(40001, "一键调货失败：" + e.getMessage());
        }
    }

    public void save(OrderDistribute distribute, UserClaim uc){
        distribute.setLastUpdatedBy(uc.getId());
        distribute.setLastUpdatedTime(new Date());
        orderDistributeRepository.save(distribute);
    }

    @Transactional
    public void delete(OrderDistributeCriteria criteria){
        List<OrderDistribute> list = orderDistributeRepository.findAll(criteria.buildSpecification());
        if(list != null && list.size() > 0 && list.size() < 20)
            orderDistributeRepository.deleteAll(list);
    }

    @Transactional
    public void deleteAll(List<OrderDistribute> list){
        orderDistributeRepository.deleteAll(list);
    }


    @Autowired
    EntityManager em;

    public Long sumQuantity(OrderDistributeCriteria criteria){
        String sql = "select sum(d.quantity) from t_order_distribute d, t_order o where d.order_id=o.id and d.status=? ";
        if(StringUtils.nonNull(criteria.getOrderNo())){
            sql += "and o.order_no like ? ";
        }
        if(StringUtils.nonNull(criteria.getStoreId())){
            sql += "and d.store_id=? ";
        }

        int i = 1;

        Query query =  em.createNativeQuery(sql);
        query.setParameter(i++, criteria.getStatus());
        if(StringUtils.nonNull(criteria.getOrderNo())){
            query.setParameter(i++, "%" + criteria.getOrderNo() + "%");
        }
        if(StringUtils.nonNull(criteria.getStoreId())){
            query.setParameter(i++, criteria.getStoreId());
        }

        List<BigDecimal> result = query.getResultList();
        return result.get(0).longValue();
    }

    public List<Map> getSameQuantifyInfo(List<Map> receivers, List<String> statuses){
        String sql = "select receiver, receiver_phone, receiver_address, count(id) as quantity from t_order where ";
        for(int i = 0; i < receivers.size(); i++){
            if(i != 0)
                sql += "or ";
            sql += "(receiver=? and receiver_phone=? and receiver_address=?) ";
        }

        if(statuses != null && statuses.size() > 0){
            for(int i = 0; i < statuses.size(); i++){
                if(i != 0)
                    sql += "or ";
                else sql += "and ( ";
                sql += "status = ? ";
            }
            sql += ") ";
        }

        sql += "group by receiver, receiver_phone, receiver_address ";
        int i = 1;

        Query query =  em.createNativeQuery(sql);
        for(Map rec: receivers){
            query.setParameter(i++, rec.get("receiver"));
            query.setParameter(i++, rec.get("receiverPhone"));
            query.setParameter(i++, rec.get("receiverAddress"));
        }
        if(statuses != null && statuses.size() > 0){
            for(String status: statuses)
                query.setParameter(i++, status);
        }
        return query.getResultList();
    }
}
