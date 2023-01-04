package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/3 14:42
 * @description
 */
@Data
@TableName("finance")
public class FinanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 封面图
     */
    private String coverImg;
    /**
     * 内容
     */
    private String content;
    /**
     * 删除状态 0-未删除 1-已删除
     */
    private Integer delFlag;
    /**
     * 状态 0-上架 1-下架
     */
    private Integer status;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;
}
