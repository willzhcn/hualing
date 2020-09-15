package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrgCriteria;
import com.hualing.domain.Org;
import com.hualing.domain.User;
import java.util.Date;
import com.hualing.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Will on 21/06/2019.
 */
@RestController
@RequestMapping("/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @GetMapping("/allTypes")
    public ActionResult getAllTypes(){
        ActionResult actionResult = new ActionResult();
        actionResult.setData(this.orgService.getAllTypes());
        actionResult.setSuccess(true);
        return actionResult;
    }

    @GetMapping("/all")
    public ActionResult getAll(){
        ActionResult actionResult = new ActionResult();
        actionResult.setData(this.orgService.getAllOrgs());
        actionResult.setSuccess(true);
        return actionResult;
    }

    @PostMapping("/queryOrgs")
    public ActionResult queryOrgs(@RequestBody OrgCriteria orgCriteria){
        ActionResult ar = new ActionResult();
        orgCriteria.setDeleted(false);
        ar.setData(this.orgService.queryOrgs(orgCriteria));
        ar.setSuccess(true);
        return ar;
    }

    @GetMapping("/getOrg")
    public ActionResult getOrg(@RequestParam Long id){
        ActionResult ar = new ActionResult();
        ar.setData(orgService.getOrgById(id));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/saveOrg")
    public ActionResult saveOrg(@RequestBody Org org, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        org.setLastUpdatedBy(uc.getId());
        org.setLastUpdatedTime(new Date());
        try {
            this.orgService.saveOrg(org);
            ar.setSuccess(true);
        } catch (CredentialException e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }

        return ar;
    }

    @GetMapping("exist")
    public ActionResult exist(@RequestParam String name){
        ActionResult ar = new ActionResult();
        ar.setSuccess(this.orgService.exist(name));
        return ar;
    }

}
