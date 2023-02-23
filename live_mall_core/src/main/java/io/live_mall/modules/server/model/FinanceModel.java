package io.live_mall.modules.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/3 15:37
 * @description
 */
@Data
public class FinanceModel {
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
     * 提交时间
     */
    private Date createTime;
    /**
     * 提交人
     */
    private String realname;
    /**
     * 状态 0-上架 1-下架
     */
    private Integer status;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 作者
     */
    private String author;
}
