package com.hualing.common;

/**
 * Created by Will on 19/06/2019.
 */
public class Constants {
    public static final String CURRENT_USER_ENTITY = "CURRENT_USER_ENTITY";

    public static final String CURRENT_USER_CLAIM = "CURRENT_USER_CLAIM";

    public static int ERROR_LOGIN_FAILED = 20001;

    public static final String STORE_IMPORT_DIR = "store_import";

    public static final String ORDER_STATUS_INIT = "init";      //初始
    public static final String ORDER_STATUS_REFUSE = "refuse";  //已拒绝
    public static final String ORDER_STATUS_CONFIRM = "confirm"; //已确认
    public static final String ORDER_STATUS_WAIT_BACK = "wait_back"; //已退货
    public static final String ORDER_STATUS_BACK = "back"; //已退货

    public static final String ORDER_DISTRIBUTE_WAIT_DELIVERY = "wait_delivery"; //等待调货
    public static final String ORDER_DISTRIBUTE_WAIT_SEND = "wait_send"; //等待发货
    public static final String ORDER_DISTRIBUTE_PREPARED = "prepared"; //预处理完成
    public static final String ORDER_DISTRIBUTE_SENT = "sent"; //已发货
    public static final String ORDER_DISTRIBUTE_REFUSE = "refuse_delivery"; //已发货


    public static final String ROLE_STORE_SUPERVISOR = "StoreSupervisor"; //店铺人员
    public static final String ROLE_SUPER_ADMIN = "SuperAdmin"; //超级管理员

    public static final String REQUEST_STATUS_REQUESTED = "requested";
    public static final String REQUEST_STATUS_APPROVED  = "approved";

    public static final String DEAL_TYPE_RECHARGE  = "充值";
    public static final String DEAL_TYPE_CONSUME  = "消费";
    public static final String DEAL_TYPE_BACK  = "退款";
    public static final String DEAL_TYPE_CANCEL  = "取消订单";

    public static final String ORG_TYPE_HUALING = "Hualing";

    public static final int BATCH_SIZE = 500;
}

