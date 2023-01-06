package io.live_mall.modules.server.model;

import lombok.Data;

/**
 * @author yewl
 * @date 2023/1/6 13:54
 * @description
 */
@Data
public class InformationBrowserModel {
    /**
     * 客户姓名
     */
    private String name;
    /**
     * 浏览时长,以毫秒为单位
     */
    private Long duration;
    /**
     * 浏览进度
     */
    private Long progress;
    /**
     * 浏览次数
     */
    private Long frequency;
}
