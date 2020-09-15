package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.domain.Notice;
import com.hualing.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Will on 10/10/2019.
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/all")
    public ActionResult queryBrands(){
        ActionResult ar = new ActionResult();
        ar.setData(this.noticeService.getAllNotices());
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/save")
    public ActionResult saveBrand(@RequestBody Notice notice, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.noticeService.saveItem(notice, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/get")
    public ActionResult getBrand(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(this.noticeService.getItem(id));
        ar.setSuccess(true);
        return ar;
    }
}
