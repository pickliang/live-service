package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/19 13:16
 * @description
 */
@Data
@TableName("t_youzan_order")
public class YouZanOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 订单号
     */
    @Id
    @TableId(value = "tid", type = IdType.INPUT)
    private String tid;
    /**
     * 有赞唯一id
     */
    private String yzOpenId;
    /**
     * remark_info
     */
    private String addressInfo;
    /**
     * 交易明细
     */
    private String orders;
    /**
     * 商品优惠后总价 单位:元
     */
    private Double totalFee;
    /**
     * 交易明细
     */
    private String orderInfo;
    /**
     * 支付类型 具体参考数据库备注
     */
    private Long payType;
    /**
     * 订单更新时间
     */
    private Date updateTime;
    /**
     * 订单发货时间（当所有商品发货后才会更新）
     */
    private Date consignTime;

    /**
     * 订单创建时间
     */
    private Date created;
    /**
     * 主订单状态 具体备注参考数据库
     */
    private String status;
    /**
     * 订单过期时间（未付款将自动关单）
     */
    private Date expiredTime;
    /**
     * 退款状态 0:未退款; 2:部分退款成功; 12:全额退款成功
     */
    private Long refundState;
    /**
     * 订单支付时间
     */
    private Date payTime;
    /**
     * 主订单状态 描述
     */
    private String statusStr;
    /**
     * 订单买家信息 加密的
     */
    private String buyerInfo;
    /**
     * 订单买家信息
     */
    private String sourceInfo;
    /**
     * 订单支付信息
     */
    private String payInfo;
    /**
     * 送礼订单子
     */
    private String childInfo;
    /**
     * 标记信息
     */
    private String remarkInfo;
    /**
     * 数据创建时间
     */
    private Date createTime;
}
