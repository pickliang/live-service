package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/15 13:58
 * @description
 */
@Data
@TableName("customer_banner")
public class CustomerBannerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
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
     * 状态：0-启用；1-停用
     */
    private Integer status;

    private Long createUser;

    private Date createTime;

    private Long updateUser;

    private Date updateTime;

}
