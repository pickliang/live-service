package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.InformationBrowseEntity;
import io.live_mall.modules.server.model.InformationBrowserModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/6 12:42
 * @description
 */
@Mapper
public interface InformationBrowseDao extends BaseMapper<InformationBrowseEntity> {
    IPage<InformationBrowserModel> informationBrowserList(IPage<InformationBrowserModel> pages, Map<String, Object> params);

    Long countPeopleNumber(String informationId);
}
