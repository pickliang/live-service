package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.CustomerUserIntegralItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/2/2 10:36
 * @description
 */
@Mapper
public interface CustomerUserIntegralItemDao extends BaseMapper<CustomerUserIntegralItemEntity> {
    IPage<JSONObject> customerUserIntegralPages(@Param("page") IPage<JSONObject> page, @Param("userId") String userId);

    Long customerUserIntegralTotal(String userId);
}
