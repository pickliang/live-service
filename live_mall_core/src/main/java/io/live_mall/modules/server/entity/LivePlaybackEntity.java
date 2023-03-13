package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/25 9:49
 * @description
 */
@Data
@TableName("live_playback")
public class LivePlaybackEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 链接
     */
    private String url;
    /**
     * 封面图
     */
    private String image;
    /**
     * 0-首页展示 1-不展示
     */
    private Integer isShow;
    /**
     * 删除状态 0-未删除 1-已删除
     */
    private Integer delFlag;
    /**
     * 删除状态 0-未删除 1-已删除
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createUser;
}
