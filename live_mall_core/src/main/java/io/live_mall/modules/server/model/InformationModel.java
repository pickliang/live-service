package io.live_mall.modules.server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/4 14:52
 * @description
 */
@Data
public class InformationModel {
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 提交时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 提交人
     */
    private String realname;
    /**
     * 浏览人数
     */
    private Long visitors;

    /**
     * 简介
     */
    private String outline;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否需要登录 0-不需要 1-需要
     */
    private Integer auth;
    /**
     * 作者
     */
    private String author;
}
