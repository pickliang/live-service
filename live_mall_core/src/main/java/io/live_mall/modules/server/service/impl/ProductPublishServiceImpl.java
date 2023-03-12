package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ProductPublishDao;
import io.live_mall.modules.server.entity.ProductPublishEntity;
import io.live_mall.modules.server.service.ProductPublishService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/3/12 11:16
 * @description
 */
@Service("productPublishService")
public class ProductPublishServiceImpl extends ServiceImpl<ProductPublishDao, ProductPublishEntity> implements ProductPublishService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.pages(new Query<JSONObject>().getPage(params)));
    }
}
