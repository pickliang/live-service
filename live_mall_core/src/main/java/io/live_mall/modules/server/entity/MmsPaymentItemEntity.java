package io.live_mall.modules.server.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/23 13:21
 * @description
 */
@Data
@TableName("mms_payment_item")
public class MmsPaymentItemEntity implements Serializable {
    private final static long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String mmsLogId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 1-付息完成 2-付息预警
     */
    private Integer type;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 投资金额
     */
    private Integer appointMoney;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户电话
     */
    private String customerPhone;
    /**
     * 理财师姓名
     */
    private String saleName;
    /**
     * 理财师手机号
     */
    private String saleMobile;
    /**
     * 第几次付息
     */
    private String name;
    /**
     * 付息日
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date payDate;
    /**
     * 付息金额
     */
    private BigDecimal payMoney;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 创建人
     */
    private Date createTime;
    /**
     * 短信发送状态
     */
    private Integer code;
    /**
     * 短信发送结果
     */
    private String result;
}
