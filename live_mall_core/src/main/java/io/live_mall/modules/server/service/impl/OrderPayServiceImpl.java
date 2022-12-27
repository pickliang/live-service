package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.OrderPayDao;
import io.live_mall.modules.server.entity.OrderPayEntity;
import io.live_mall.modules.server.service.OrderPayService;


@Service("orderPayService")
public class OrderPayServiceImpl extends ServiceImpl<OrderPayDao, OrderPayEntity> implements OrderPayService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderPayEntity> page = this.page(
                new Query<OrderPayEntity>().getPage(params),
                new QueryWrapper<OrderPayEntity>()
        );

        return new PageUtils(page);
    }

}