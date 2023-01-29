package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/1/29 14:20
 * @description
 */
@Data
@TableName("customer_user_questionnaire")
public class CustomerUserQuestionnaireEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 客户id
     */
    private String customerUserId;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 评级
     * C1保守型 20分以下
     * C2稳健型 20-37
     * C3平衡型 38-53
     * C4成长型 54-82
     * C5积极型 83分以上
     */
    private String grade;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 删除状态；0-未删除 1-已删除
     */
    private Integer delFlag;
}
