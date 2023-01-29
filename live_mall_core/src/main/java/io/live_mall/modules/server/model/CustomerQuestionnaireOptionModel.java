package io.live_mall.modules.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author yewl
 * @date 2023/1/29 15:11
 * @description
 */
@Data
public class CustomerQuestionnaireOptionModel {
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
     * 选中的为true
     */
    private Boolean check;
}
