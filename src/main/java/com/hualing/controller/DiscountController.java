package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.DiscountCriteria;
import com.hualing.domain.Discount;
import com.hualing.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Will on 22/07/2019.
 */
@RestController
@RequestMapping("/discount")
public class DiscountController {
    @Autowired
    private DiscountService discountService;

    @PostMapping("/query")
    public ActionResult query(@RequestBody DiscountCriteria discountCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.discountService.query(discountCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/get")
    public ActionResult get(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(discountService.get(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/save")
    public ActionResult saveOrg(@RequestBody Discount discount, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.discountService.save(discount, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/match")
    public ActionResult matchDiscount(@RequestParam long orgId, @RequestParam String orderDate, @RequestParam int year, @RequestParam String quarter){
        ActionResult ar = new ActionResult();
        Discount discount = null;
        try {
            discount = this.discountService.matchDiscount(orgId, orderDate, year, quarter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(discount != null) {
            ar.setSuccess(true);
            ar.setData(discount);
        } else {
            ar.setSuccess(false);
            ar.setMessage("无法匹配折扣！");
        }
        return ar;
    }
}
