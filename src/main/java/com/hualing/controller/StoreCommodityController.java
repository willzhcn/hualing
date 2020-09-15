package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.StoreCommodityCriteria;
import com.hualing.criteria.StoreCommodityVCriteria;
import com.hualing.domain.StoreCommodity;
import com.hualing.service.StoreCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Will on 20/07/2019.
 */
@RestController
@RequestMapping("/storeCommodity")
public class StoreCommodityController {
    @Autowired
    private StoreCommodityService storeCommodityService;

    @PostMapping("/query")
    public ActionResult query(@RequestBody StoreCommodityCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeCommodityService.query(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/agentQuery")
    public ActionResult agentQuery(@RequestBody StoreCommodityVCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeCommodityService.agentQuery(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/agentQueryAll")
    public ActionResult agentQueryAll(@RequestBody StoreCommodityVCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeCommodityService.agentQueryAll(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/matchOne")
    public ActionResult matchOne(@RequestBody StoreCommodityCriteria criteria){
        ActionResult ar = new ActionResult();
        StoreCommodity s = this.storeCommodityService.matchOne(criteria);
        if(s != null) {
            ar.setData(s);
            ar.setSuccess(true);
        } else ar.setSuccess(false);

        return ar;
    }

    @PostMapping("/queryAll")
    public ActionResult queryAll(@RequestBody StoreCommodityCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeCommodityService.queryAll(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/get")
    public ActionResult get(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeCommodityService.getStoreCommodity(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/save")
    public ActionResult save(@RequestBody StoreCommodity storeCommodity, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.storeCommodityService.save(storeCommodity, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/exist")
    public ActionResult exist(@RequestParam String commodityNo, @RequestParam String size){
        ActionResult ar = new ActionResult();
        ar.setSuccess(storeCommodityService.exist(commodityNo, size));
        return ar;
    }
}
