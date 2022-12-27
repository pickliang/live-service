package io.live_mall.modules.server.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.collection.CollectionUtil;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ProductLabelDao;
import io.live_mall.modules.server.entity.ProductLabelEntity;
import io.live_mall.modules.server.service.ProductLabelService;


@Service("productLabelService")
public class ProductLabelServiceImpl extends ServiceImpl<ProductLabelDao, ProductLabelEntity> implements ProductLabelService {

    @Override
    public PageUtils queryPage( JSONObject params) {
    	QueryWrapper<ProductLabelEntity> queryWrapper = new QueryWrapper<ProductLabelEntity>();
    	if(StringUtils.isNotBlank(params.getString("onetype"))) {
        	queryWrapper.in("onetype",CollectionUtil.toList( params.getString("onetype").split(",")));
    	}
    	
    	if(StringUtils.isNotBlank(params.getString("key"))) {
        	queryWrapper.like("key",params.getString("key"));
    	}
    	
        IPage<ProductLabelEntity> page = this.page(
                new Query<ProductLabelEntity>().getPage(params),
                queryWrapper.orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

}