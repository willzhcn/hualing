package com.hualing.service;

import com.hualing.common.ActionResult;
import com.hualing.common.UserClaim;
import com.hualing.criteria.UserCriteria;
import com.hualing.domain.Org2User;
import com.hualing.domain.Role;
import com.hualing.domain.User;
import com.hualing.repository.Org2UserRepository;
import com.hualing.repository.RoleRepository;
import com.hualing.repository.UserRepository;
import com.hualing.util.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Will on 21/06/2019.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Org2UserRepository org2UserRepository;
    @Autowired
    private RoleRepository roleRepository;

    public User authenticate(User user){
        String password = CryptoUtils.encrypt(user.getPassword());
        User result = this.userRepository.findFirstByAccountAndPassword(user.getAccount(), password);
        if(result != null){
            result.setOrg2UserList(this.org2UserRepository.findAllByUserId(result.getId()));
        }
        return result;
    }

    public Page<User> queryUsers(UserCriteria userCriteria){
        Page<User> page = this.userRepository.findAll(userCriteria.buildSpecification(), userCriteria.buildPageRequest());
        List<User> list = page.getContent();
        for(User user: list){
            user.setOrg2UserList(this.org2UserRepository.findAllByUserId(user.getId()));
        }
//        return this.userRepository.findAll(userCriteria.buildSpecification(), userCriteria.buildPageRequest());
        return page;
    }

    public User getUser(Long id){
        User user = this.userRepository.findById(id).get();
        user.setOrg2UserList(this.org2UserRepository.findAllByUserId(id));
        return user;
    }

    public List<Role> queryAllRoles(){
        return this.roleRepository.findAll();
    }

    @Transactional
    public void saveUser(User user, UserClaim uc){
        user.setLastUpdatedBy(uc.getId());
        Date date = new Date();
        user.setLastUpdatedTime(date);
        if(user.getId() < 1){
            //新建user
            user.setPassword("123456");
        }
        if(Objects.nonNull(user.getPassword())){
            user.setPassword(CryptoUtils.encrypt(user.getPassword()));
        } else if(user.getId() > 0){
            User temp = this.userRepository.findById(user.getId()).get();
            user.setPassword(temp.getPassword());
        }
        User result = this.userRepository.save(user);
        user.setId(result.getId());


        Set<Org2User> list = user.getOrg2UserList();
        if(list != null && list.size() > 0) {
            //remove existing
            Org2User org2User = new Org2User();
            org2User.setUserId(user.getId());
            Set<Org2User> existList = this.org2UserRepository.findAllByUserId(user.getId());
            if(existList.size() > 0)
                this.org2UserRepository.deleteAll(existList);
            for(Org2User o: list){
                o.setUserId(user.getId());
                o.setLastUpdatedBy(uc.getId());
                o.setLastUpdatedTime(date);
            }
            this.org2UserRepository.saveAll(user.getOrg2UserList());
        }
    }

    public ActionResult changePassword(Map<String, Object> map, UserClaim userClaim){
        ActionResult ar = new ActionResult();
        User temp = this.userRepository.findById(userClaim.getId()).get();
        String encryptedPassword = CryptoUtils.encrypt((String)map.get("oldPassword"));
        if(encryptedPassword.equals(temp.getPassword())){
            temp.setPassword(CryptoUtils.encrypt((String)map.get("newPassword")));
            temp.setLastUpdatedBy(userClaim.getId());
            temp.setLastUpdatedTime(new Date());
            this.userRepository.save(temp);
            ar.setSuccess(true);
        } else {
            ar.setSuccess(false);
            ar.setMessage("用户旧密码错误！");
        }
        return ar;
    }

    public void resetPassword(Map<String, Object> map, UserClaim userClaim){
        User temp = this.userRepository.findById(new Long((Integer)map.get("id"))).get();
        if(temp != null){
            temp.setPassword(CryptoUtils.encrypt((String)map.get("password")));
            temp.setLastUpdatedBy(userClaim.getId());
            temp.setLastUpdatedTime(new Date());
            this.userRepository.save(temp);
        }
    }

    public Boolean userExist(String account){
        User user = this.userRepository.findFirstByAccount(account);
        if(user != null){
            return true;
        } else return false;
    }
}
