package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/25 15:53
 * @description
 */
@Data
@TableName("mms_sms_log")
public class MmsSmsLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 电话
     */
    private String phone;
    /**
     * 验证码
     */
    private Integer verificationCode;
    /**
     * 短信发送状态
     */
    private Integer code;
    /**
     * 短信发送结果
     */
    private String result;
    /**
     * 创建时间
     */
    private Date createTime;
}
