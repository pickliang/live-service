package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/5 16:51
 * @description
 */
@Data
@TableName("activity_user")
public class ActivityUserEntity implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 报名状态 1-报名成功 2-取消报名
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
