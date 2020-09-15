package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.BarCodeCriteria;
import com.hualing.domain.BarCode;
import com.hualing.service.BarCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Will on 15/07/2019.
 */
@RestController
@RequestMapping("/barcode")
public class BarCodeController {
    @Autowired
    private BarCodeService barCodeService;

    @PostMapping("/save")
    public ActionResult saveBarCode(@RequestBody BarCode barCode, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.barCodeService.saveBarCode(barCode, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/get")
    public ActionResult getBarCode(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(this.barCodeService.getBarCode(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/query")
    public ActionResult queryBarCodes(@RequestBody BarCodeCriteria barCodeCriteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.barCodeService.queryBarCodes(barCodeCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/saveList")
    public ActionResult saveList(@RequestBody List<BarCode> list, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.barCodeService.insertInBatch(list, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/removeAll")
    public ActionResult removeAll(@RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.barCodeService.removeBarCodes();
        ar.setSuccess(true);
        return ar;
    }
}
