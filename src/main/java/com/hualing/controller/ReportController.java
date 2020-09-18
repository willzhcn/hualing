package com.hualing.controller;

import com.hualing.common.ActionResult;
import com.hualing.common.Constants;
import com.hualing.common.UserClaim;
import com.hualing.criteria.OrderWithDistributeCriteria;
import com.hualing.service.ReportService;
import com.hualing.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Will on 08/10/2019.
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/order")
    public ActionResult getOrderReportDate(@RequestBody Map map) throws ParseException {
        ActionResult ar = new ActionResult();
        ar.setSuccess(true);
        Object obj = map.get("startDate");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        if(StringUtils.nonNull(obj)){
            startDate = sf.parse(obj.toString().substring(0, 10) + " 00:00:00");
        }
        obj = map.get("endDate");
        Date endDate = null;
        if(StringUtils.nonNull(obj)){
            endDate = sf.parse(obj.toString().substring(0, 10) + " 00:00:00");
        }
        obj = map.get("brandId");
        long brandId = 0;
        if(StringUtils.nonNull(obj)){
            brandId = Long.valueOf(obj.toString());
        }
        obj = map.get("orgId");
        long orgId = 0;
        if(StringUtils.nonNull(obj)){
            orgId = Long.valueOf(obj.toString());
        }
        obj = map.get("orderDate");
        Date orderDate = null;
        if(StringUtils.nonNull(obj)){
            orderDate = sf.parse(obj.toString().substring(0, 10) + " 00:00:00");
        }
        String status = null;
        obj = map.get("status");
        if(StringUtils.nonNull(obj)){
            status = obj.toString();
        }
        List list = reportService.getOrderReportData(startDate, endDate, brandId, orgId, orderDate, status);
        ar.setData(list);
        return ar;
    }

    @PostMapping("/order2")
    public ActionResult getOrderReportDate2(@RequestBody OrderWithDistributeCriteria map, @RequestAttribute(Constants.CURRENT_USER_CLAIM) UserClaim uc) throws ParseException {
        ActionResult ar = new ActionResult();
        ar.setSuccess(true);

        List list = reportService.getOrderReportData2(map);

        //如果状态是已确认，则需要将状态改为已完成
//        if(Constants.ORDER_STATUS_CONFIRM.equals(status)){
//            reportService.setComplete(list, uc);
//        }

        ar.setData(list);
        return ar;
    }

    @PostMapping("/orderOnly")
    public ActionResult getOrderData(@RequestBody Map map) throws ParseException {
        ActionResult ar = new ActionResult();
        ar.setSuccess(true);
        Object obj = map.get("startDate");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        if(StringUtils.nonNull(obj)){
            startDate = sf.parse(obj.toString().substring(0, 10) + " 00:00:00");
        }
        obj = map.get("endDate");
        Date endDate = null;
        if(StringUtils.nonNull(obj)){
            endDate = sf.parse(obj.toString().substring(0, 10) + " 00:00:00");
        }
        obj = map.get("brandId");
        long brandId = 0;
        if(StringUtils.nonNull(obj)){
            brandId = Long.valueOf(obj.toString());
        }
        obj = map.get("orgId");
        long orgId = 0;
        if(StringUtils.nonNull(obj)){
            orgId = Long.valueOf(obj.toString());
        }
        obj = map.get("orderDate");
        Date orderDate = null;
        if(StringUtils.nonNull(obj)){
            orderDate = sf.parse(obj.toString().substring(0, 10) + " 00:00:00");
        }
        String status = null;
        obj = map.get("status");
        if(StringUtils.nonNull(obj)){
            status = obj.toString();
        }
        String backDate = null;
        obj = map.get("backDate");
        if(StringUtils.nonNull(obj)){
            backDate = obj.toString();
        }

        List list = reportService.getOrderOnlyData(startDate, endDate, brandId, orgId, orderDate, status, backDate);
        ar.setData(list);
        return ar;
    }

}
