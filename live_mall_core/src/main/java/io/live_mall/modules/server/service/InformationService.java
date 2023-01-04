package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.InformationEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/3 16:20
 * @description
 */
public interface InformationService extends IService<InformationEntity> {
    PageUtils informationPages(Map<String, Object> params);
}
