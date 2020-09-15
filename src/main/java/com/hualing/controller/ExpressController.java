package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.ExpressCriteria;
import com.hualing.domain.Express;
import com.hualing.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Will on 07/07/2019.
 */
@RestController
@RequestMapping("/express")
public class ExpressController {
    @Autowired
    private ExpressService expressService;

    @GetMapping("/all")
    public ActionResult getAll(){
        ActionResult actionResult = new ActionResult();
        actionResult.setData(this.expressService.getAll());
        actionResult.setSuccess(true);
        return actionResult;
    }

    @PostMapping("/queryExpresses")
    public ActionResult queryOrgs(@RequestBody ExpressCriteria expressCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.expressService.queryExpresses(expressCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/getExpress")
    public ActionResult getOrg(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(expressService.getExpress(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/saveExpress")
    public ActionResult saveOrg(@RequestBody Express express, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.expressService.saveExpress(express, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("exist")
    public ActionResult exist(@RequestParam String name){
        ActionResult ar = new ActionResult();
        ar.setSuccess(this.expressService.exist(name));
        return ar;
    }
}
