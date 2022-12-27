package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.RaiseEntity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 募集期
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 22:19:57
 */
@Mapper
public interface RaiseDao extends BaseMapper<RaiseEntity> {

	IPage<JSONObject> getRaiseSuccessPage(IPage<ProductEntity> page,@Param("params") Map<String, Object> params);

	RaiseEntity getNewRaiseRun(String productId);
	
	RaiseEntity getNewRaiseLast(String productId);

	Date getMaxEndDate(String productId);
	
}
