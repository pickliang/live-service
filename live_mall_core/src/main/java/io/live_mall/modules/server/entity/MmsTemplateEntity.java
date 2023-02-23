package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/21 19:12
 * @description
 */
@Data
@TableName("mms_template")
public class MmsTemplateEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 短信模板类型 1-内部兑付通知；2-内部兑付预警通知；3-内部付息通知；4-内部付息预警通知
     */
    private Integer type;
    /**
     * 小程序链接
     */
    private String url;
    /**
     * 短信模板id
     */
    private String mmsId;
    /**
     * 短信模板审核状态
     */
    private Integer code;
    /**
     * 短信审核内容
     */
    private String result;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createUser;
}
