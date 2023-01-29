package io.live_mall.modules.server.model;

import lombok.Data;

import java.util.List;

/**
 * @author yewl
 * @date 2023/1/29 15:05
 * @description
 */
@Data
public class CustomerQuestionnaireModel {
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
     * 选项
     */
    private List<CustomerQuestionnaireOptionModel> optionList;
}
