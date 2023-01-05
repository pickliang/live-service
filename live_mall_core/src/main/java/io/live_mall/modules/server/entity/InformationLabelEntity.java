package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/3 16:14
 * @description
 */
@Data
@TableName("information_label")
public class InformationLabelEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 分类：1-白话财经 2-固收资讯 3-股权资讯 4-二级市场资讯
     */
    private Integer classify;
    /**
     * 标签名称
     */
    private String label;

    /**
     * 删除状态 0-未删除 1-已删除
     */
    private Integer delFlag;

    private Date delTime;

    private Long createUser;

    private Date createTime;
}
