package com.hualing.service;

import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderRequestCriteria;
import com.hualing.criteria.OrgMoneyCriteria;
import com.hualing.domain.Order;
import com.hualing.domain.OrderRequest;
import com.hualing.domain.OrgMoney;
import com.hualing.domain.User;
import com.hualing.repository.OrderRepository;
import com.hualing.repository.OrderRequestRepository;
import com.hualing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 12/08/2019.
 */
@Service
public class OrderRequestService {
    @Autowired
    private OrderRequestRepository orderRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    public Page<OrderRequest> query(OrderRequestCriteria criteria){
        return this.orderRequestRepository.findAll(criteria.buildSpecification(), criteria.buildPageRequest());
    }

    public void request(Order order, int quantity, UserClaim uc){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setQuantity(quantity);
        orderRequest.setOrder(order);
        User user = userRepository.findById(uc.getId()).get();
        orderRequest.setRequestedTime(new Date());
        orderRequest.setRequestedBy(user);
        orderRequest.setOrg(user.getOrg());
        orderRequest.setStatus(Constants.REQUEST_STATUS_REQUESTED);
        this.orderRequestRepository.save(orderRequest);
    }

    @Transactional
    public void approve(OrderRequest info, UserClaim uc) throws CredentialException {
        OrderRequest orderRequest = orderRequestRepository.findById(info.getId()).get();
        if(!Constants.REQUEST_STATUS_REQUESTED.equals(orderRequest.getStatus()))
            return;
        orderRequest.setStatus(Constants.REQUEST_STATUS_APPROVED);
        User user = new User();
        user.setId(uc.getId());
        orderRequest.setApprovedBy(user);
        orderRequest.setApprovedTime(new Date());
        Order order = orderRequest.getOrder();
        orderService.approveOrderRequest(order, orderRequest.getQuantity(), uc);
        orderRequestRepository.save(orderRequest);

    }

    @Transactional
    public void approveAll(ArrayList<Long> ids,  UserClaim uc){
        List<OrderRequest> list = orderRequestRepository.findAllById(ids);
        if(list != null && list.size() > 0) {
            for (OrderRequest req : list) {
                if(!Constants.REQUEST_STATUS_REQUESTED.equals(req.getStatus()))
                    continue;
                User user = new User();
                user.setId(uc.getId());
                req.setApprovedBy(user);
                req.setApprovedTime(new Date());
                req.setStatus(Constants.REQUEST_STATUS_APPROVED);
                Order order = req.getOrder();
                orderService.approveOrderRequest(order, req.getQuantity(), uc);
                orderRequestRepository.save(req);
            }
        } else {
            throw new CredentialException(50001, "没有退货请求！");
        }
    }
}
