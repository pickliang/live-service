package io.live_mall.modules.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.MmsSmsLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yewl
 * @date 2023/2/25 15:55
 * @description
 */
@Mapper
public interface MmsSmsLogDao extends BaseMapper<MmsSmsLogEntity> {
}
