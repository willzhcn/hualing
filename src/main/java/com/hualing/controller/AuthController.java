package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.domain.Org2User;
import com.hualing.domain.User;
import com.hualing.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ActionResult login(@RequestBody User u, HttpSession session) {

        String account = u.getAccount()
                          .indexOf('\\') > 0 ? StringUtils.substringAfterLast(u.getAccount(), "\\") : u.getAccount();

        User user = userService.authenticate(u);

        ActionResult actionResult = new ActionResult();
        UserClaim uc = new UserClaim();
        if (Objects.isNull(user) || user.isDeleted()) {
            actionResult.setSuccess(false);
            actionResult.setCode(Constants.ERROR_LOGIN_FAILED);
            actionResult.setMessage("login fail due to incorrect id and password.");
        } else {
            session.setAttribute(Constants.CURRENT_USER_ENTITY, user);
            actionResult.setSuccess(true);
            //issue the token and set the data back.
            Map<String, Object> values = new LinkedHashMap<String, Object>();
            uc.setAccount(user.getAccount());
            uc.setId(user.getId());
            Set<Org2User> org2Users = user.getOrg2UserList();
            Iterator<Org2User> it = org2Users.iterator();
            if(it.hasNext()){
                Org2User org2User = it.next();
                uc.setRole(org2User.getRole().getRole());
                uc.setOrgId(org2User.getOrg().getId());
                uc.setOrgType(org2User.getOrg().getOrgType().getType());
                if(org2User.getStore() != null)
                    uc.setStoreId(org2User.getStore().getId());
            }
            values.put("Bearer", uc.toToken());
            values.put("user", user);
            actionResult.setData(values);
        }

        return actionResult;
    }

}
