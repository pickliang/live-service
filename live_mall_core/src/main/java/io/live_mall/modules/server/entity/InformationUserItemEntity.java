package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/27 11:21
 * @description
 */
@Data
@TableName("information_user_item")
public class InformationUserItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 理财师id
     */
    private Long userId;
    /**
     * 资讯id
     */
    private Long informationId;
    /**
     * 资讯标题
     */
    private String title;
    /**
     * 客户id
     */
    private String customerUserId;
    /**
     * 浏览进度 1-100
     */
    private Integer progress;
    /**
     * 浏览次数
     */
    private Long frequency;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
