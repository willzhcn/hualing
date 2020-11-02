package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderRequestCriteria;
import com.hualing.domain.OrderRequest;
import com.hualing.service.OrderRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Will on 12/08/2019.
 */
@RestController
@RequestMapping("/orderRequest")
public class OrderRequestController {
    @Autowired
    private OrderRequestService orderRequestService;

    @PostMapping("/query")
    public ActionResult queryOrders(@RequestBody OrderRequestCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderRequestService.query(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/approve")
    public ActionResult approve(@RequestBody OrderRequest orderRequest, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            orderRequestService.approve(orderRequest, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }

        return ar;
    }

    @PostMapping("approveAll")
    public ActionResult approveAll(@RequestBody Map map, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            ArrayList<Integer> ids = (ArrayList<Integer>) map.get("ids");
            ArrayList<Long> longList = new ArrayList();
            for(Integer i : ids)
                longList.add(i.longValue());
            orderRequestService.approveAll(longList, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }

        return ar;
    }
}
