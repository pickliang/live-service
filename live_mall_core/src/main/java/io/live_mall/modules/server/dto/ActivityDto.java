package io.live_mall.modules.server.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/4 14:05
 * @description
 */
@Data
public class ActivityDto {
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
     * 宣传图
     */
    private String image;
    /**
     * 活动时间 yyyy-MM-dd
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateTime;
}
