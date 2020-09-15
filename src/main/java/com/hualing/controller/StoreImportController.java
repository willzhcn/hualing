package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.CredentialException;
import com.hualing.common.UserClaim;
import com.hualing.criteria.StoreImportCriteria;
import com.hualing.service.StoreImportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Will on 20/07/2019.
 */
@RestController
@RequestMapping("/storeImport")
public class StoreImportController {
    @Autowired
    private StoreImportService storeImportService;

    @PostMapping("/query")
    public ActionResult query(@RequestBody StoreImportCriteria criteria){
        ActionResult ar = new ActionResult();
        ar.setData(this.storeImportService.query(criteria));
        ar.setSuccess(true);
        return ar;
    }

    @PostMapping("/import")
    public ActionResult importFile(@RequestParam("data") String data,
                                   @RequestPart(value = "file", required = false) MultipartFile file,
                                   @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();

        try {
            this.storeImportService.importStore(data, file, uc);
            ar.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ar.setSuccess(false);
            ar.setMessage(e.getMessage());
        }
        return ar;
    }

    @GetMapping("/removeUseless")
    public ActionResult removeUseless(@RequestParam String date, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc){
        ActionResult ar = new ActionResult();
        if(Constants.ROLE_SUPER_ADMIN.equals(uc.getRole())) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                this.storeImportService.removeUselessData(sf.parse(date));
                ar.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                ar.setSuccess(false);
                ar.setMessage(e.getMessage());
            }
        } else {
            ar.setSuccess(false);
            ar.setMessage("用户不是超级管理员，不能操作！");
        }
        return ar;
    }

    @GetMapping("/download")
    public void download(@RequestParam long id, HttpServletRequest request, HttpServletResponse response) throws CredentialException, UnsupportedEncodingException {
        String filePath = this.storeImportService.getFilePathById(id);
        if(filePath != null){
            File file = new File(filePath);
            String fileName = StringUtils.substringAfterLast(filePath, "/");
            String downloadFileName = URLEncoder.encode(fileName,"UTF-8");
            if(file.exists()){
//                response.setContentType("application/force-download");
                response.reset();
//                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + downloadFileName);

                byte[] buffer = new byte[1024];
                FileInputStream fis = null; //文件输入流
                BufferedInputStream bis = null;

                OutputStream os = null; //输出流
                try {
                    os = response.getOutputStream();
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int length = 0;
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer, 0, i);
                        length += i;
                        i = bis.read(buffer);
                    }
                    response.setHeader("Content-Length", String.valueOf(length));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    throw new CredentialException(20005, "File stream write failed!");
                }
                System.out.println("----------file download" + fileName);
                try {
                    os.flush();
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    throw new CredentialException(20005, "File stream close failed!");
                }
            } else {
                throw new CredentialException(20005, "File not exist!");
            }
        } else {
            throw new CredentialException(20005, "Can't find file path by id!");
        }
    }
}
