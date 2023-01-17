package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.InformationDisclosureEntity;
import io.live_mall.modules.server.model.InformationDisclosureModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/16 15:12
 * @description
 */
@Mapper
public interface InformationDisclosureDao extends BaseMapper<InformationDisclosureEntity> {
    IPage<InformationDisclosureModel> pages(@Param("pages") IPage<InformationDisclosureModel> pages, @Param("params") Map<String, Object> params);

    IPage<JSONObject> customerPages(@Param("pages") IPage<JSONObject> pages, @Param("params") Map<String, Object> params);

    InformationDisclosureModel informationDisclosureInfo(String id);
}
