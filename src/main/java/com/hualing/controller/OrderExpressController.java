package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderExpressCriteria;
import com.hualing.domain.OrderExpress;
import com.hualing.service.OrderExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Will on 04/08/2019.
 */
@RestController
@RequestMapping("/orderExpress")
public class OrderExpressController {
    @Autowired
    private OrderExpressService orderExpressService;

    @PostMapping("/saveAll")
    public ActionResult save(@RequestBody List<OrderExpress> list, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            orderExpressService.saveList(list, uc);
            ar.setSuccess(true);
        } catch (Exception e){
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage("保存信息失败！");
        }

        return ar;
    }

    @PostMapping("/query")
    public ActionResult query(@RequestBody OrderExpressCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderExpressService.query(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/queryAll")
    public ActionResult queryAll(@RequestBody OrderExpressCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orderExpressService.queryAll(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("get")
    public ActionResult get(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(orderExpressService.get(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/send")
    public ActionResult send(@RequestBody OrderExpress orderExpress, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        try {
            this.orderExpressService.send(orderExpress, uc);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }

        return ar;
    }
}
