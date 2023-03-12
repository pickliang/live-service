package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/3/11 19:14
 * @description
 */
@Data
@TableName("p_order_purchase")
public class OrderPurchaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
    * 订单id
    */
    private String orderId;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 身份证号
     */
    private String cardNum;

    /**
    * 申购日期
    */
    private Date date;

    /**
    * 申购金额
    */
    private Integer money;

    /**
    * 申购份额
    */
    private Integer portion;

    /**
    * 申购净值
    */
    private String worth;

    /**
    * 交易确认单
    */
    private String appendix;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateUser;
}