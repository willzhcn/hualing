package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.UserCriteria;
import com.hualing.domain.User;
import com.hualing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Created by Will on 21/06/2019.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/queryUsers")
    public ActionResult queryUsers(@RequestBody UserCriteria userCriteria){
        ActionResult ar = new ActionResult();
        userCriteria.setDeleted(false);
        ar.setData(this.userService.queryUsers(userCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/getUser")
    public ActionResult getOrg(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(userService.getUser(id));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/allRoles")
    public ActionResult getAllTypes(){
        ActionResult actionResult = new ActionResult();
        actionResult.setData(this.userService.queryAllRoles());
        actionResult.setSuccess(true);
        return actionResult;
    }

    @PostMapping("/saveUser")
    public ActionResult saveUser(@RequestBody User user, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.userService.saveUser(user, uc);
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/changePassword")
    public ActionResult changePassword(@RequestBody Map<String, Object> user, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        return this.userService.changePassword(user, uc);
    }

    @PostMapping("/resetPassword")
    public ActionResult resetPassword(@RequestBody Map<String, Object> user, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        this.userService.resetPassword(user, uc);
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/exist")
    public ActionResult userExist(@RequestParam String account){
        ActionResult ar = new ActionResult();
        ar.setSuccess(this.userService.userExist(account));
        return ar;
    }
}
