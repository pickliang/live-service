package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.modules.server.entity.CustomerUserIntegralItemEntity;

/**
 * @author yewl
 * @date 2023/2/2 10:38
 * @description
 */
public interface CustomerUserIntegralItemService extends IService<CustomerUserIntegralItemEntity> {
    String saveCustomerUserIntegralItem(String userId, String cardNum);
}
