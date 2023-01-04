package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/3 16:11
 * @description
 */
@Data
@TableName("information")
public class InformationEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 简介
     */
    private String outline;
    /**
     * 内容
     */
    private String content;
    /**
     * 标签id
     */
    private Long labelId;
    /**
     * 是否需要登录 0-不需要 1-需要
     */
    private Integer auth;
    /**
     * 删除状态 0-未删除 1-已删除
     */
    private Integer delFlag;

    private Date delTime;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;
}
