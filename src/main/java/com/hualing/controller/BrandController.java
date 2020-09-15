package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.domain.Brand;
import com.hualing.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Will on 07/07/2019.
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/all")
    public ActionResult queryBrands(){
        ActionResult ar = new ActionResult();
        ar.setData(this.brandService.getAllBrands());
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/save")
    public ActionResult saveBrand(@RequestBody Brand brand, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.brandService.saveBrand(brand, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/getBrand")
    public ActionResult getBrand(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(this.brandService.getBrand(id));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("exist")
    public ActionResult exist(@RequestParam String name){
        ActionResult ar = new ActionResult();
        ar.setSuccess(this.brandService.exist(name));
        return ar;
    }
}
