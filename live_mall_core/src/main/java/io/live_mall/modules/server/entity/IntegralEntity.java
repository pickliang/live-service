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
 * @date 2023/1/30 16:16
 * @description
 */
@Data
@TableName("integral")
public class IntegralEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 积分开始日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @NotNull(message = "开始日期不可为空")
    private Date beginDate;
    /**
     * 年化额兑换积分
     */
    @NotNull(message = "年化额兑换积分不可为空")
    private Integer integral;
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
