package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.dto.InformationDisclosureDto;
import io.live_mall.modules.server.entity.InformationDisclosureEntity;
import io.live_mall.modules.server.model.InformationDisclosureModel;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/16 15:15
 * @description
 */
public interface InformationDisclosureService extends IService<InformationDisclosureEntity> {
    PageUtils pages(Map<String, Object> params);

    int saveOrUpdateInformationDisclosure(InformationDisclosureDto disclosureDto, Long userId);

    PageUtils customerPages(Map<String, Object> params);

    InformationDisclosureModel informationDisclosureInfo(String id);
}
