package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yewl
 * @date 2023/1/29 11:14
 * @description
 */
@Data
@TableName("customer_questionnaire")
public class CustomerQuestionnaireEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 序号
     */
    private Integer orderNumber;
    /**
     * 问题
     */
    private String question;
    /**
     * 删除状态；0-未删除 1-已删除
     */
    private Integer delFlag;
}
