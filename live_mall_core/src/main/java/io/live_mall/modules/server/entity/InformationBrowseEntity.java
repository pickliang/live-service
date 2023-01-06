package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/6 11:08
 * @description
 */
@Data
@TableName("information_browse")
public class InformationBrowseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 资讯id
     */
    private String informationId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 浏览时长,以毫秒为单位
     */
    private Long duration;
    /**
     * 浏览进度
     */
    private Long progress;
    /***
     * 创建时间
     */
    private Date createTime;
}
