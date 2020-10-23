package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderCriteria;
import com.hualing.criteria.OrderWithDistributeCriteria;
import com.hualing.domain.Order;
import com.hualing.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Will on 22/07/2019.
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    public ActionResult saveOrder(@RequestBody Order order, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            this.orderService.save(order, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }



    @GetMapping("/get")
    public ActionResult getOrder(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderService.get(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/queryAll")
    public ActionResult queryAllOrders(@RequestBody OrderCriteria orderCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderService.queryAll(orderCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/query")
    public ActionResult queryOrders(@RequestBody OrderCriteria orderCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderService.query(orderCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/queryWithDistribute")
    public ActionResult queryWithDistribute(@RequestBody OrderWithDistributeCriteria orderCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderService.queryWithDistribute(orderCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/saveList")
    public ActionResult saveList(@RequestBody List<Order> list, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            this.orderService.saveList(list, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }

        return ar;
    }

    @PostMapping("/orderBackList")
    public ActionResult orderBackList(@RequestBody List<Order> list, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            this.orderService.orderBackList(list, uc);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @PostMapping("/orderConfirm")
    public ActionResult orderConfirm(@RequestBody Order order, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {

            orderService.orderConfirm(order, uc);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @PostMapping("/orderBack")
    public ActionResult orderBack(@RequestBody Order order, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {

            orderService.orderBack(order, uc);
            ar.setSuccess(true);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @PostMapping("/requestBack")
    public ActionResult requestOrderBack(@RequestBody Order order, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {

            orderService.requestBack(order, uc);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @PostMapping("/cancel")
    public ActionResult requestOrderCancel(@RequestParam Long orderId, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {

            orderService.orderCancel(orderId, uc);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @PostMapping("/count")
    public ActionResult count(@RequestBody OrderCriteria orderCriteria){
        ActionResult ar = new ActionResult();
        try {
            ar.setData(orderService.count(orderCriteria));
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @PostMapping("/checkExistStore")
    public ActionResult checkExistStore(@RequestBody Order order){
        ActionResult ar = new ActionResult();
        ar.setSuccess(orderService.checkExistStore(order));
        return ar;
    }
}
