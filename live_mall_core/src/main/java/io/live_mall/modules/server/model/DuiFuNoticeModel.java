package io.live_mall.modules.server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/23 11:07
 * @description
 */
@Data
public class DuiFuNoticeModel {
    /**
     * 订单号
     */
    private String id;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 认购金额
     */
    private Integer appointMoney;
    /**
     * 客户手机号
     */
    private String phone;
    /**
     *还本付息金额
     */
    private Double sumMoney;
    /**
     * 理财师id
     */
    private Long saleId;
    /**
     * 理财师姓名
     */
    private String realname;
    /**
     * 理财师电话
     */
    private String mobile;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 对付日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date date;
    /**
     * 第几次付息
     */
    private String name;
    /**
     * 付息日期
     */
    private String payDate;
    /**
     * 付息金额
     */
    private BigDecimal payMoney;
}
