package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.StoreCriteria;
import com.hualing.domain.Store;
import com.hualing.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Will on 07/07/2019.
 */
@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/all")
    public ActionResult getAll(){
        ActionResult actionResult = new ActionResult();
        actionResult.setData(this.storeService.getAll());
        actionResult.setSuccess(true);
        return actionResult;
    }

    @GetMapping("/allExpressNotRequired")
    public ActionResult getAllExpressRequired(){
        ActionResult actionResult = new ActionResult();
        actionResult.setData(this.storeService.allExpressNotRequired());
        actionResult.setSuccess(true);
        return actionResult;
    }

    @PostMapping("/queryStores")
    public ActionResult queryOrgs(@RequestBody StoreCriteria storeCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeService.queryStores(storeCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/getStore")
    public ActionResult getOrg(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(storeService.getStore(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/saveStore")
    public ActionResult saveOrg(@RequestBody Store store, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.storeService.saveStore(store, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("exist")
    public ActionResult exist(@RequestParam String name){
        ActionResult ar = new ActionResult();
        ar.setSuccess(this.storeService.exist(name));
        return ar;
    }
}
