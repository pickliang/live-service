package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2022/12/19 11:34
 * @description
 */
@Data
@TableName("customer_user")
public class CustomerUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 客户名称
     */
    private String name;

    /**
     * 证件号
     */
    private String cardNum;
    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件正面照
     */
    private String cardPhotoR;
    /**
     * 证件反面照
     */
    private String cardPhotoL;
    /**
     * 证件有效期
     */
    private String cardTime;
    /**
     * 销售id 用户表id
     */
    private Integer saleId;
    /**
     * 邀请码
     */
    private Integer code;
    /**
     * 被邀请码
     */
    private Integer askCode;
    /**
     * 删除状态(1删除  0未删除)
     */
    private Integer delFlag;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date uptDate;

    /**
     * token
     */
    private String token;
    /**
     * token过期时间
     */
    private Date expireTime;
    /**
     * token更新时间
     */
    private Date tokenUptTime;
}
