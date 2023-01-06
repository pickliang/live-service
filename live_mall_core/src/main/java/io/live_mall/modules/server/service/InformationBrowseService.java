package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.InformationBrowseEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/6 12:43
 * @description
 */
public interface InformationBrowseService extends IService<InformationBrowseEntity> {
    PageUtils informationBrowserList(Map<String, Object> params);

    Long countPeopleNumber(String informationId);
}
