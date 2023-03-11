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
 * @date 2023/3/10 22:17
 * @description
 */
@Data
@TableName("p_order_bonus")
public class OrderBonusEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
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
     * 客户身份证号
     */
    private String cardNum;

    /**
     * 类型 1-股权 2-证券
     */
    private int type;

    /**
     * 分红金额
     */
    private BigDecimal money;

    /**
     * 分红日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date date;

    /**
     * 分红次数
     */
    private Integer frequency;
    /**
     * 备注
     */
    private String remark;

    /**
     * 附件
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
