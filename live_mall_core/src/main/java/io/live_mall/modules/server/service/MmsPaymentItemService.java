package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MmsPaymentItemEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/23 13:25
 * @description
 */
public interface MmsPaymentItemService extends IService<MmsPaymentItemEntity> {
    PageUtils pages(Map<String, Object> params);
}
