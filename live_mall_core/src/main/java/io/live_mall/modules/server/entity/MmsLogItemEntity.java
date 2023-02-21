package io.live_mall.modules.server.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/20 21:04
 * @description
 */
@Data
@TableName("mms_log_item")
public class MmsLogItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String mmsLogId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 订单号
     */
    private Integer appointMoney;
    /**
     * 订单号
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
     * 兑付到期日
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;
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
    private Integer status;
    /**
     * 短信发送结果
     */
    private String result;
}
