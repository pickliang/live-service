package io.live_mall.modules.server.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/30 16:34
 * @description
 */
@Data
@TableName("integral_activity")
public class IntegralActivityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 活动开始日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "开始日期不可为空")
    private Date beginDate;
    /**
     * 活动结束日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "结束日期不可为空")
    private Date endDate;
    /**
     * 年化额兑换积分
     */
    @NotNull(message = "年化额兑换积分不可为空")
    private Integer integral;
    /**
     * 邀约人奖励积分比例
     */
    @NotNull(message = "邀约人奖励积分比例不可为空")
    private Integer integralProportion;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 删除状态；0-未删除 1-已删除
     */
    private Integer delFlag;
}
