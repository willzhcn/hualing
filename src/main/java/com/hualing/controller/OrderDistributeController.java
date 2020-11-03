package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderDistributeCriteria;
import com.hualing.domain.OrderDistribute;
import com.hualing.service.OrderDistributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Will on 02/08/2019.
 */
@RestController
@RequestMapping("/orderDistribute")
public class OrderDistributeController {
    @Autowired
    private OrderDistributeService orderDistributeService;

    @PostMapping("/query")
    public ActionResult query(@RequestBody OrderDistributeCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderDistributeService.query(criteria));
        ar.setSuccess(true);
        return ar;
    }
    @PostMapping("/count")
    public ActionResult count(@RequestBody OrderDistributeCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderDistributeService.count(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/queryAll")
    public ActionResult queryAll(@RequestBody OrderDistributeCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderDistributeService.queryAll(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/save")
    public ActionResult save(@RequestBody OrderDistribute orderDistribute, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        orderDistributeService.save(orderDistribute, uc);
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/updateStatus")
    public ActionResult updateStatus(@RequestBody OrderDistribute orderDistribute, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        orderDistributeService.updateStatus(orderDistribute, uc);
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/deliverAll")
    public ActionResult deliverAll(@RequestBody Map map, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            ArrayList<Integer> ids = (ArrayList<Integer>) map.get("ids");
            ArrayList<Long> longList = new ArrayList();
            for(Integer i : ids)
                longList.add(i.longValue());
            orderDistributeService.deliverAll(longList, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            ar.setMessage(e.getMessage());
            ar.setSuccess(false);
        }
        return ar;
    }

    @PostMapping("/cancelAll")
    public ActionResult cancelAll(@RequestBody List<OrderDistribute> list, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            orderDistributeService.cancelAll(list, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            ar.setMessage(e.getMessage());
            ar.setSuccess(false);
        }
        return ar;
    }

    @PostMapping("sumQuantity")
    public ActionResult sumQuantity(@RequestBody OrderDistributeCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setSuccess(true);
        ar.setData(orderDistributeService.sumQuantity(criteria));
        return ar;
    }

    @PostMapping("sameQuantifyInfo")
    public ActionResult sumQuantity(@RequestBody Map map){
        ActionResult ar = new ActionResult();
        ar.setSuccess(true);
        List<Map> list = (List<Map>) map.get("list");
        List<String> statuses = (List<String>) map.get("statuses");
        ar.setData(orderDistributeService.getSameQuantifyInfo(list, statuses));
        return ar;
    }
}
