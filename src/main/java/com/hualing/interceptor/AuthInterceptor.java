package com.hualing.interceptor;

import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Value("${api.token.expired.web}")
    private int webTokenExpiredHours = 24;

    @Value("${api.token.expired.mobile}")
    private int mobileTokenExpiredHours = 720;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader("Authorization");
        String authenticationHeader = request.getHeader("Authentication");

        String authHeader = StringUtils.isBlank(authorizationHeader) ? authenticationHeader : authorizationHeader;


        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new CredentialException(10001, "Missing or Invalid Authorization Header.");
        }


        final String token = authHeader.substring(7); // The part after "Bearer "

        UserClaim uc = null;

        try {

            uc = UserClaim.fromToken(token);

        } catch (Exception e) {
            throw new CredentialException(10002, "Parse fail due to invalid Token : " + token, e);
        }

        Date issuedAt = uc.getIssuedAt();

        //identify the device type. Mobile or PC
        String requestHeader = request.getHeader("user-agent");
        if (this.isMobileDevice(requestHeader)) {
            if (System.currentTimeMillis() - issuedAt.getTime() > mobileTokenExpiredHours * 60 * 60 * 1000) {
                throw new CredentialException(10003, "Token Expired on Mobile");
            }
        } else {
            if (System.currentTimeMillis() - issuedAt.getTime() > webTokenExpiredHours * 60 * 60 * 1000) {
                throw new CredentialException(10004, "Token Expired in PC");
            }
        }

        request.setAttribute(Constants.CURRENT_USER_CLAIM, uc);
        response.addHeader("Authorization", "Bearer " + uc.toToken());

        return true;
    }


    public boolean isMobileDevice(String requestHeader) {
        /**
         * android : 所有android设备
         * MQQBrowser : 手机QQ浏览器
         * windows phone: windows系统的手机
         */
        String[] deviceArray = new String[]{"Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser", "BlackBerry"};
        if (requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for (int i = 0; i < deviceArray.length; i++) {
            if (requestHeader.indexOf(deviceArray[i]) > 0) {
                return true;
            }
        }
        return false;
    }


}
