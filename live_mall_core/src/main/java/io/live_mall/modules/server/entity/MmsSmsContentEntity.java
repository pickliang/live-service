package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/26 14:23
 * @description
 */
@Data
@TableName("mms_sms_content")
public class MmsSmsContentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 1-手动发送 2-批量发送
     */
    private Integer type;
    /**
     * 接收人id
     */
    private String receiveId;
    /**
     * 接收手机号
     */
    private String phone;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间
     */
    private Long createUser;
}
