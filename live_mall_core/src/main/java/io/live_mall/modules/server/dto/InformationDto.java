package io.live_mall.modules.server.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yewl
 * @date 2023/1/4 13:18
 * @description
 */
@Data
public class InformationDto {
    private Long id;
    /**
     * 标题
     */
    @NotBlank(message = "标题不可为空")
    private String title;
    /**
     * 简介
     */
    private String outline;
    /**
     * 内容
     */
    @NotBlank(message = "内容不可为空")
    private String content;
    /**
     * 标签id
     */
    private String labelId;
    /**
     * 是否需要登录 0-不需要 1-需要
     */
    private Integer auth;
    /**
     * 分类：1-白话财经 2-固收资讯 3-股权资讯 4-二级市场资讯
     */
    private Integer classify;
}
