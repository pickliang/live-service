package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youzan.cloud.open.sdk.gen.v1_0_0.model.YouzanScrmCustomerDetailGetResult;
import io.live_mall.modules.server.entity.YouZanUserEntity;

/**
 * @author yewl
 * @date 2023/2/19 11:13
 * @description
 */
public interface YouZanUserService extends IService<YouZanUserEntity> {
    boolean save(String userId, String yzOpenId, YouzanScrmCustomerDetailGetResult.YouzanScrmCustomerDetailGetResultData data);
}
