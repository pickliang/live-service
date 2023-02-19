package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youzan.cloud.open.sdk.gen.v4_0_2.model.YouzanTradesSoldGetResult;
import io.live_mall.modules.server.entity.YouZanOrderEntity;

/**
 * @author yewl
 * @date 2023/2/19 13:24
 * @description
 */
public interface YouZanOrderService extends IService<YouZanOrderEntity> {
    void save( YouzanTradesSoldGetResult.YouzanTradesSoldGetResultData data);
}
