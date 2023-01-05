package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.InformationEntity;
import io.live_mall.modules.server.model.InformationModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/3 16:16
 * @description
 */
@Mapper
public interface InformationDao extends BaseMapper<InformationEntity> {
    IPage<InformationModel> informationPages(@Param("pages") IPage<InformationModel> pages, @Param("params") Map<String, Object> params);

    List<InformationModel> customerInformation(@Param("classify") Integer classify);

    IPage<InformationModel> customerInformationPages(@Param("pages") IPage<InformationModel> pages, @Param("params") Map<String, Object> params);
}
