package com.hualing.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Will on 19/07/2019.
 */
@Service
public class FileService {
    @Value("${api.file.store.server}")
    private String fileStoreServer;

    public String uploadFile(MultipartFile file, String baseServer){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
        String stamp = sf.format(new Date());
        File f = null;
        String destination = "";
        String fileName = "";
        try {
            String originalName = URLDecoder.decode(file.getOriginalFilename(), StandardCharsets.UTF_8.name()) ;
            fileName = StringUtils.substringBeforeLast(originalName, ".") + "_" +  stamp + "." +StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            destination = generateURL(baseServer);
            String filePath = fileStoreServer + destination + fileName;
            f = new File(fileStoreServer + destination);
            if (!f.exists()) {
                f.mkdirs();
            }

            file.transferTo(new File(filePath));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return destination + fileName;
    }

    private String generateURL(String baseServer) {
        String url = baseServer;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        return url + "/" + year + "/" + month + "/";
    }
}
