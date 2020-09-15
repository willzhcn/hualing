package com.hualing.service;

import com.hualing.common.UserClaim;
import com.hualing.domain.Notice;
import com.hualing.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Will on 10/10/2019.
 */
@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices(){
        return this.noticeRepository.findByIsDeletedOrderByIdDesc(false);
    }

    public void saveItem(Notice item, UserClaim uc){
        item.setLastUpdatedBy(uc.getId());
        item.setLastUpdatedTime(new Date());
        this.noticeRepository.save(item);
    }

    public Notice getItem(Long id){
        return this.noticeRepository.findById(id).get();
    }
}
