package io.live_mall.modules.server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/4 16:14
 * @description
 */
@Data
public class ActivityModel {
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 活动日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date dateTime;
    /**
     * 审核状态 1-审核通过 2-审核拒绝 3-审核中
     */
    private Integer status;

    /**
     * 提交人
     */
    private String realname;
    /**
     * 报名人数
     */
    private Long nums;
}
