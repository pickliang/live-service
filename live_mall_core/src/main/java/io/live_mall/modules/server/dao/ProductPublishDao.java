package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.ProductPublishEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/3/12 11:15
 * @description
 */
@Mapper
public interface ProductPublishDao extends BaseMapper<ProductPublishEntity> {
    IPage<JSONObject> pages(IPage<JSONObject> pages);
}
