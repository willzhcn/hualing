package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrgMoneyCriteria;
import com.hualing.criteria.OrgMoneyDetailCriteria;
import com.hualing.domain.OrgMoney;
import com.hualing.service.OrgMoneyDetailService;
import com.hualing.service.OrgMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Will on 25/07/2019.
 */
@RestController
@RequestMapping("/orgMoney")
public class OrgMoneyController {
    @Autowired
    private OrgMoneyService orgMoneyService;

    @Autowired
    private OrgMoneyDetailService orgMoneyDetailService;

    @PostMapping("/query")
    public ActionResult query(@RequestBody OrgMoneyCriteria orgMoneyCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orgMoneyService.query(orgMoneyCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/get")
    public ActionResult get(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(orgMoneyService.get(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/save")
    public ActionResult save(@RequestBody OrgMoney orgMoney, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.orgMoneyService.save(orgMoney, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/exist")
    public ActionResult exist(@RequestParam Long orgId){
        ActionResult ar = new ActionResult();
        ar.setSuccess(this.orgMoneyService.exist(orgId));
        return ar;
    }

    @PostMapping("/queryDetail")
    public ActionResult query(@RequestBody OrgMoneyDetailCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orgMoneyDetailService.query(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/queryAllDetail")
    public ActionResult queryAllDetail(@RequestBody OrgMoneyDetailCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.orgMoneyDetailService.queryAll(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/recharge")
    public ActionResult recharge(@RequestParam Long orgId, @RequestParam Double sum, @RequestParam String remarks, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.orgMoneyService.recharge(orgId, sum, remarks, uc);
        ar.setSuccess(true);
        return ar;
    }
}
