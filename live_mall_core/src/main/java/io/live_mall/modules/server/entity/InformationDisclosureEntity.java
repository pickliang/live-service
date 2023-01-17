package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/16 15:09
 * @description
 */
@Data
@TableName("information_disclosure")
public class InformationDisclosureEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 信息披露名称
     */
    private String name;
    /**
     * 产品id
     */
    private String productId;

    private Integer delFlag;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;
}
