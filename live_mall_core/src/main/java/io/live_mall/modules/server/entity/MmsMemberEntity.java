package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/24 15:28
 * @description
 */
@Data
@TableName("mms_member")
public class MmsMemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String mmsLogId;
    /**
     * p_member主键
     */
    private String memberNo;
    /**
     * p_member主键
     */
    private String customerName;
    /**
     * 客户生日
     */
    private String birthday;
    /**
     * 客户电话
     */
    private String phone;
    /**
     * 理财师id
     */
    private Long saleId;
    /**
     * 理财师姓名
     */
    private String realname;
    /**
     * 短信发送状态
     */
    private Integer code;
    /**
     * 短信发送结果
     */
    private String result;

    private Date createTime;
    private Long createUser;

}
