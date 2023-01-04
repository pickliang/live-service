package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.InformationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/1/3 16:16
 * @description
 */
@Mapper
public interface InformationDao extends BaseMapper<InformationEntity> {
}
