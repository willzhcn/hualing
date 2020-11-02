package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderWithDistributeCriteria;
import com.hualing.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 08/10/2019.
 */
@Service
public class ReportService {
    @Autowired
    EntityManager em;

    public List getOrderReportData(Date startDate, Date endDate, long brandId, long orgId, Date orderDate, String status, ArrayList<String> statuses){
        String sql = "select o.order_date , g.company_name, s.name as store_name, o.category,o.commodity_no, o.size, o.year, o.quarter , o.discount, o.price,d.quantity, "
                   + "o.receiver, o.receiver_phone, o.receiver_address, e.name as express_name, "
                   + "oe.express_no, o.order_no, o.quantity as order_quantity, o.comments, g.dh, g.bh , o.express, o.special_discount from t_order o left join t_order_distribute d on o.id = d.order_id "
                   + "left join t_express_distribute ed on ed.distribute_id = d.id left join t_order_express oe "
                   + "on oe.id = ed.order_express_id left join t_expresses e on oe.express_id = e.id "
                   + "left join t_stores s on d.store_id = s.id left join t_org g on o.org_id = g.id where 1=1 ";
        if(startDate != null){
            sql += "and o.create_date >= ? ";
        }

        if(endDate != null){
            sql += "and o.create_date <= ?";
        }

        if(brandId > 0){
            sql += "and o.brand_id = ? ";
        }
        if(orgId > 0){
            sql += "and o.org_id = ? ";
        }
        if(orderDate != null){
            sql += "and o.order_date = ? ";
        }

        if(StringUtils.nonNull(status)){
            sql += "and o.status = ? ";
        }

        if(statuses != null && statuses.size() > 0){
            StringBuffer sf = new StringBuffer();
            for(String s: statuses){
                sf.append("or o.status = ? ");
            }
            sql += " and (" + sf.substring(2) + ") ";
        }

        sql += " order by g.company_name, o.order_date desc ";

        int i = 1;

        Query query =  em.createNativeQuery(sql);

        if(startDate != null){
            query.setParameter(i++, startDate, TemporalType.DATE);
        }

        if(endDate != null){
            query.setParameter(i++, endDate, TemporalType.DATE);
        }

        if(brandId > 0){
            query.setParameter(i++, brandId);
        }
        if(orgId > 0){
            query.setParameter(i++, orgId);
        }
        if(orderDate != null){
            query.setParameter(i++, orderDate, TemporalType.DATE);
        }

        if(StringUtils.nonNull(status)){
            query.setParameter(i++, status);
        }

        if(statuses != null && statuses.size() > 0){
            for(String s: statuses){
                query.setParameter(i++, s);
            }
        }
        return query.getResultList();
    }

    public List getOrderReportData2(OrderWithDistributeCriteria map){
        String sql = "select o.order_date , g.company_name, s.name as store_name, o.category,o.commodity_no, o.size, o.year, o.quarter , o.discount, o.price,d.quantity, "
                + "o.receiver, o.receiver_phone, o.receiver_address, e.name as express_name, "
                + "oe.express_no, o.order_no, o.quantity as order_quantity, o.express  from t_order o left join t_order_distribute d on o.id = d.order_id "
                + "left join t_express_distribute ed on ed.distribute_id = d.id left join t_order_express oe "
                + "on oe.id = ed.order_express_id left join t_expresses e on oe.express_id = e.id "
                + "left join t_stores s on d.store_id = s.id left join t_org g on o.org_id = g.id where 1=1 ";
//        if(startDate != null){
//            sql += "and o.create_date >= ? ";
//        }
//
//        if(endDate != null){
//            sql += "and o.create_date <= ?";
//        }

        if(StringUtils.nonNull(map.getBrandId())){
            sql += "and o.brand_id = ? ";
        }
        if(StringUtils.nonNull(map.getOrgId())){
            sql += "and o.org_id = ? ";
        }
        if(map.getOrderDate() != null){
            sql += "and o.order_date = ? ";
        }

        if(StringUtils.nonNull(map.getStatus())){
            sql += "and o.status = ? ";
        }

        if(StringUtils.nonNull(map.getStoreId())){
            sql += "and s.id = ? ";
        }

        if(StringUtils.nonNull(map.getOrderNo())){
            sql += "and o.order_no like ? ";
        }
        if(StringUtils.nonNull(map.getExpressNo())){
            sql += "and oe.express_no like ? ";
        }
        if(StringUtils.nonNull(map.getCommodityNo())){
            sql += "and o.commodity_no like ? ";
        }
        if(StringUtils.nonNull(map.getSizeNo())){
            sql += "and o.size = ? ";
        }
        if(StringUtils.nonNull(map.getReceiver())){
            sql += "and o.receiver like ? ";
        }
        if(StringUtils.nonNull(map.getReceiverPhone())){
            sql += "and o.receiver_phone like ? ";
        }
        int i = 1;

        Query query =  em.createNativeQuery(sql);

//        if(startDate != null){
//            query.setParameter(i++, startDate, TemporalType.DATE);
//        }
//
//        if(endDate != null){
//            query.setParameter(i++, endDate, TemporalType.DATE);
//        }

        System.out.println(sql);
        if(StringUtils.nonNull(map.getBrandId())){
            query.setParameter(i++, map.getBrandId());
        }
        if(StringUtils.nonNull(map.getOrgId() )){
            query.setParameter(i++, map.getOrgId());
        }
        if(map.getOrderDate() != null){
            query.setParameter(i++, map.getOrderDate(), TemporalType.DATE);
        }

        if(StringUtils.nonNull(map.getStatus())){
            query.setParameter(i++, map.getStatus());
        }

        if(StringUtils.nonNull(map.getStoreId() )){
            query.setParameter(i++, map.getStoreId());
        }

        if(StringUtils.nonNull(map.getOrderNo())){
            query.setParameter(i++, "%" + map.getOrderNo() + "%");
        }
        if(StringUtils.nonNull(map.getExpressNo())){
            query.setParameter(i++, "%" + map.getExpressNo() + "%");
        }
        if(StringUtils.nonNull(map.getCommodityNo())){
            query.setParameter(i++, "%" + map.getCommodityNo() + "%");
        }
        if(StringUtils.nonNull(map.getSizeNo())){
            query.setParameter(i++, map.getSizeNo());
        }
        if(StringUtils.nonNull(map.getReceiver())){
            query.setParameter(i++, "%" + map.getReceiver() + "%");
        }
        if(StringUtils.nonNull(map.getReceiverPhone())){
            query.setParameter(i++, "%" + map.getReceiverPhone() + "%");
        }
        return query.getResultList();
    }

    @Transactional
    public void setComplete(List list, UserClaim uc){
        String sql = "update t_order set status=?, last_updated_by=?, last_updated_time=current_timestamp() where order_no in (";
        StringBuffer sf = new StringBuffer();
        for(int i = 0; i < list.size(); i++){
            sf.append(",?");
        }
        sf.append(")");
        sql = sql + sf.substring(1);
        Query query =  em.createNativeQuery(sql);
        int index = 1;
        query.setParameter(index++, Constants.ORDER_DISTRIBUTE_SENT);
        query.setParameter(index++, uc.getId());
        for(int j = 0; j < list.size(); j++){
            String orderNo = (String) ((Object[])list.get(j))[16];
            query.setParameter(index++, orderNo);
        }
        query.executeUpdate();
    }

    public List getOrderOnlyData(Date startDate, Date endDate, long brandId, long orgId, Date orderDate, String status, String backDate, ArrayList<String> statuses){
        String sql = "select o.order_date , o.commodity_no, o.size, o.year, o.quarter , o.discount, o.price, o.quantity, "
                + "o.receiver, o.receiver_phone, o.receiver_address, o.comments, r.thdh, r.thbh , o.express, o.special_discount, r.company_name, (select DATE_FORMAT(max(requested_time), '%Y-%m-%d') from t_order_request where  order_id = o.id) as back_request_time, o.back_count  from t_order o left JOIN t_org r on o.org_id = r.id where 1=1 ";
        if(startDate != null){
            sql += "and o.create_date >= ? ";
        }

        if(endDate != null){
            sql += "and o.create_date <= ?";
        }

        if(brandId > 0){
            sql += "and o.brand_id = ? ";
        }
        if(orgId > 0){
            sql += "and o.org_id = ? ";
        }
        if(orderDate != null){
            sql += "and o.order_date = ? ";
        }

        if(StringUtils.nonNull(status)){
            sql += "and o.status = ? ";
        }

        if(statuses != null && statuses.size() > 0){
            StringBuffer sf = new StringBuffer();
            for(String s: statuses){
                sf.append("or o.status = ? ");
            }
            sql += " and (" + sf.substring(2) + ") ";
        }

        if(StringUtils.nonNull(backDate)){
            sql += "and (select DATE_FORMAT(max(requested_time), '%Y-%m-%d') from t_order_request where  order_id = o.id) = ? ";
        }

        sql += " order by r.dh ";
        int i = 1;

        Query query =  em.createNativeQuery(sql);

        if(startDate != null){
            query.setParameter(i++, startDate, TemporalType.DATE);
        }

        if(endDate != null){
            query.setParameter(i++, endDate, TemporalType.DATE);
        }

        if(brandId > 0){
            query.setParameter(i++, brandId);
        }
        if(orgId > 0){
            query.setParameter(i++, orgId);
        }
        if(orderDate != null){
            query.setParameter(i++, orderDate, TemporalType.DATE);
        }

        if(StringUtils.nonNull(status)){
            query.setParameter(i++, status);
        }

        if(statuses != null && statuses.size() > 0){
            for(String s: statuses){
                query.setParameter(i++, s);
            }
        }

        if(StringUtils.nonNull(backDate)){
            query.setParameter(i++, backDate);
        }
        return query.getResultList();
    }

}
