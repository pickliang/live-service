package io.live_mall.modules.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@Mapper
public interface EcharsDao  {
	JSONObject geTj1(JSONObject data);

	List<JSONObject> geTj2(JSONObject data);
	List<JSONObject> getRaiseDay(JSONObject data);
	List<JSONObject> getDuifu(JSONObject data);
	List<JSONObject> getRaiseDayMoeny(JSONObject data);
	List<JSONObject> getProductMoeny(JSONObject data);
	JSONObject geTj4(JSONObject data);
}
