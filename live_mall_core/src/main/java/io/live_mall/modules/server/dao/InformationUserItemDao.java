package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.InformationUserItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/2/27 11:23
 * @description
 */
@Mapper
public interface InformationUserItemDao extends BaseMapper<InformationUserItemEntity> {
    IPage<JSONObject> pages(@Param("pages") IPage<JSONObject> pages, @Param("userId") Long userId);
}
