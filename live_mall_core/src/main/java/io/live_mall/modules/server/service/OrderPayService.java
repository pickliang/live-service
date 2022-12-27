package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.OrderPayEntity;

import java.util.Map;

/**
 * 结算信息
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-02-18 23:16:37
 */
public interface OrderPayService extends IService<OrderPayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

