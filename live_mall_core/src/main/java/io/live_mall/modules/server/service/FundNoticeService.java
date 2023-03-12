package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.FundNoticeEntity;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/12 14:06
 * @description
 */
public interface FundNoticeService extends IService<FundNoticeEntity> {
    PageUtils pages(Map<String, Object> params);
}
