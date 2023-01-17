package io.live_mall.modules.server.dto;

import io.live_mall.modules.server.entity.InformationDisclosureAnnexEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yewl
 * @date 2023/1/16 16:06
 * @description
 */
@Data
public class InformationDisclosureDto {
    private String id;
    /**
     * 信息披露名称
     */
    @NotBlank(message = "名称不可为空")
    private String name;
    /**
     * 产品id
     */
    @NotBlank(message = "请选择产品")
    private String productId;
    /**
     * 文件
     */
    private List<InformationDisclosureAnnexEntity> annexes;
}
