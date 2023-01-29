package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/29 11:19
 * @description
 */
@Data
@TableName("customer_questionnaire_option_user")
public class CustomerQuestionnaireOptionUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 客户id
     */
    private String customerUserId;
    /**
     * 问卷id
     */
    private String questionnaireId;
    /**
     * 选项id
     */
    private String questionnaireOptionId;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 答题时间
     */
    private Date createTime;
    /**
     * 删除状态；0-未删除 1-已删除
     */
    private Integer delFlag;

}
