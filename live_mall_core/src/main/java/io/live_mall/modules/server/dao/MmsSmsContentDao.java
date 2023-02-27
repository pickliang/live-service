package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.MmsSmsContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yewl
 * @date 2023/2/26 14:25
 * @description
 */
@Mapper
public interface MmsSmsContentDao extends BaseMapper<MmsSmsContentEntity> {
    IPage<JSONObject> pages(@Param("pages") IPage<JSONObject> pages, @Param("receiveId") String receiveId, @Param("type") Integer type);
}
