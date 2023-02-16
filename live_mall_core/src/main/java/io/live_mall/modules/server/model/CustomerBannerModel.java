package io.live_mall.modules.server.model;

import lombok.Data;

/**
 * @author yewl
 * @date 2023/2/16 10:51
 * @description
 */
@Data
public class CustomerBannerModel {
    private String id;

    /**
     * banner地址
     */
    private String url;
    /**
     * 文章类型：1-公司动态；2-资讯信息；3-公司活动
     */
    private Integer articleType;
    /**
     * 文章id
     */
    private String articleId;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 状态：0-启用；1-停用
     */
    private Integer status;

}
