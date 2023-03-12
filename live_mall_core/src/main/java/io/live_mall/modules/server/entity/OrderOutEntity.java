package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/3/10 22:31
 * @description
 */
@Data
@TableName("p_order_out")
public class OrderOutEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    /**
     * id
     */
    private String id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 身份证号
     */
    private String cardNum;

    /**
     * 认购金额
     */
    private BigDecimal appointMoney;

    /**
     * 退出金额
     */
    private int money;

    /**
     * 退出日期
     */
    private Date date;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;
}
