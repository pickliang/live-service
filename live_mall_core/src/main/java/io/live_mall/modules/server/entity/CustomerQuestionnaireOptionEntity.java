package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yewl
 * @date 2023/1/29 11:16
 * @description
 */
@Data
@TableName("customer_questionnaire_option")
public class CustomerQuestionnaireOptionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 问卷id
     */
    private String questionnaireId;
    /**
     * 选项
     */
    private String questionnaireOption;
    /**
     * 分数
     */
    private Integer score;

    /**
     * 删除状态；0-未删除 1-已删除
     */
    private Integer delFlag;
}
