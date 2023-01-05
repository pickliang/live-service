package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/4 13:56
 * @description
 */
@Data
@TableName("activity")
public class ActivityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

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
     * 宣传图
     */
    private String image;
    /**
     * 活动时间 yyyy-MM-dd
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateTime;
    /**
     * 审核状态 1-审核通过 2-审核拒绝 3-审核中
     */
    private Integer status;
    /**
     * 二维码
     */
    private String qrCode;

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
