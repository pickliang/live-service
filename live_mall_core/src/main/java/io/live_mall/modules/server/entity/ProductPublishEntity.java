package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/3/12 11:14
 * @description
 */
@Data
@TableName("p_product_publish")
public class ProductPublishEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 披露时间
     */
    private Date date;

    /**
     * 当日净值
     */
    private BigDecimal worth;

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

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;
}
