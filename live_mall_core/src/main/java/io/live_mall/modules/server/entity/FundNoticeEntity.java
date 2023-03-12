package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/3/12 14:04
 * @description
 */
@Data
@TableName("p_fund_notice")
public class FundNoticeEntity implements Serializable {
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
     * 删除状态 0-未删除 1-已删除
     */
    private int delFlag;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;
}
