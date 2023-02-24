package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.MmsMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/24 15:31
 * @description
 */
@Mapper
public interface MmsMemberDao extends BaseMapper<MmsMemberEntity> {
    IPage<JSONObject> pages(@Param("pages") IPage<JSONObject> pages, @Param("params") Map<String, Object> params);
}
