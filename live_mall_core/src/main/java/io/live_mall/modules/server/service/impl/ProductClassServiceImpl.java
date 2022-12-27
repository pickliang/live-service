package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.ProductClassDao;
import io.live_mall.modules.server.entity.ProductClassEntity;
import io.live_mall.modules.server.service.ProductClassService;


@Service("productClassService")
public class ProductClassServiceImpl extends ServiceImpl<ProductClassDao, ProductClassEntity> implements ProductClassService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductClassEntity> page = this.page(
                new Query<ProductClassEntity>().getPage(params),
                new QueryWrapper<ProductClassEntity>().orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

}