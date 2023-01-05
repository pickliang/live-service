package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.ActivityEntity;
import io.live_mall.modules.server.model.ActivityModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/4 14:01
 * @description
 */
@Mapper
public interface ActivityDao extends BaseMapper<ActivityEntity> {
    IPage<ActivityModel> activityPages(@Param("pages") IPage<ActivityModel> pages, @Param("params") Map<String, Object> params);
    IPage<JSONObject> customerActivityPages(@Param("pages") IPage<ActivityModel> pages, @Param("params") Map<String, Object> params);
}
