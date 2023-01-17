package io.live_mall.modules.server.model;

import io.live_mall.modules.server.entity.InformationDisclosureAnnexEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yewl
 * @date 2023/1/16 15:56
 * @description
 */
@Data
public class InformationDisclosureModel {
    private String id;

    /**
     * 信息披露名称
     */
    private String name;

    /**
     * 产品id
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;

    /**
     * 附件地址
     */
    private List<InformationDisclosureAnnexEntity> annexes;

    private Date createTime;
}
