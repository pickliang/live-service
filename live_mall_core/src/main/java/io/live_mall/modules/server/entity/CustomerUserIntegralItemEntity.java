package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/2 10:32
 * @description
 */
@Data
@TableName("customer_user_integral_item")
public class CustomerUserIntegralItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    private String customerUserId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 募集id
     */
    private String raiseId;
    /**
     * 金额
     */
    private Integer appointMoney;
    /**
     * 赠送积分
     */
    private Integer integral;
    /**
     * 描述
     */
    private String description;
    /**
     * 有赞返回的状态码
     */
    private Integer code;
    /**
     * 有赞增加积分返回的结果
     */
    private String result;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 删除状态；0-未删除 1-已删除
     */
    private Integer delFlag;


}
