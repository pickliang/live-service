package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.YjViewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-20 01:15:19
 */
@Mapper
public interface YjViewDao extends BaseMapper<YjViewEntity> {

	IPage<YjViewEntity> selectYjPage(IPage<YjViewEntity> page, Map<String, Object> params);
	
}
