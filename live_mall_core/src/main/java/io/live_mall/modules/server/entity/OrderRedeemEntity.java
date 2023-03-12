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
 * @date 2023/3/11 19:14
 * @description
 */
@Data
@TableName("p_order_redeem")
public class OrderRedeemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 订单
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
     * 赎回日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date date;

    /**
     * 赎回金额
     */
    private BigDecimal money;

    /**
     * 赎回份额
     */
    private Integer portion;

    /**
     * 赎回净值
     */
    private BigDecimal worth;

    /**
     * 赎回收益
     */
    private String income;

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
