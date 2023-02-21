package io.live_mall.modules.server.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/20 20:59
 * @description
 */
@Data
@TableName("mms_log")
public class MmsLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 开始日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 发送条数
     */
    private Integer rowNum;
    /**
     * 通知类型 1-兑付通知；2-付息通知
     */
    private Integer type;
    /**
     * 发送条数
     */
    private Date createTime;
    /**
     * 操作人
     */
    private Long createUser;
}
