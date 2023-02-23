package io.live_mall.modules.server.model;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/22 20:59
 * @description
 */
public class YouZanOrderModel {
    /**
     * 累计消费金额
     */
    private double consumerAmount;
    /**
     * 累计消费订单数
     */
    private long consumerOrderNum;
    /**
     * 最近下单时间
     */
    private Date recentlyOderTime;
    /**
     * 累计退款金额
     */
    private double refundAmount;
    /**
     * 累计退款单数
     */
    private long refundOrderNum;
}
