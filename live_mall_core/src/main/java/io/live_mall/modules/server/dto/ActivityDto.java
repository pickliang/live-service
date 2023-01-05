package io.live_mall.modules.server.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/4 14:05
 * @description
 */
@Data
public class ActivityDto {
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
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date dateTime;
}
