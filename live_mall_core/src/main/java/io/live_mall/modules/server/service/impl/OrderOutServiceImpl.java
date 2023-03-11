package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.OrderOutDao;
import io.live_mall.modules.server.entity.OrderOutEntity;
import io.live_mall.modules.server.service.OrderOutService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/3/10 22:34
 * @description
 */
@Service("orderOutService")
@AllArgsConstructor
@Slf4j
public class OrderOutServiceImpl extends ServiceImpl<OrderOutDao, OrderOutEntity> implements OrderOutService {
}
