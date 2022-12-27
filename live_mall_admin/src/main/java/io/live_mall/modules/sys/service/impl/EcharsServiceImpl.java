package io.live_mall.modules.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import io.live_mall.modules.server.dao.EcharsDao;
import io.live_mall.modules.server.utils.EcharUtils;
import io.live_mall.modules.sys.service.EcharsService;

@Service
public class EcharsServiceImpl  implements EcharsService {

	
	@Autowired
	EcharsDao echarsDao;
	
	
	@Override
	public JSONObject geTj1(JSONObject data) {
		// TODO Auto-generated method stub
		return echarsDao.geTj1(data);
	}

	@Override
	public List<JSONObject> geTj2(JSONObject data) {
		// TODO Auto-generated method stub
		return echarsDao.geTj2(data);
	}

	@Override
	public JSONObject geTj3(JSONObject data) {
		// TODO Auto-generated method stub
		String type = data.getString("date");
		/*if("month".equals(type)) {
			EcharUtils.getDateMonth(data);
		}
		if("quarter".equals(type)) {
			EcharUtils.getDateQuarter(data);
		}
		if("year".equals(type)) {
			EcharUtils.getDateYear(data);
		}*/
		data.put("startDate", "2019-06-06");
		data.put("endDate", "2021-06-06");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("raiseDay", echarsDao.getRaiseDay(data));
		jsonObject.put("dufu", echarsDao.getDuifu(data));
		jsonObject.put("raiseDayMoeny", echarsDao.getRaiseDayMoeny(data));
		jsonObject.put("productMoeny", echarsDao.getProductMoeny(data));
		return jsonObject;
	}

	@Override
	public JSONObject geTj4(JSONObject data) {
		// TODO Auto-generated method stub
		return echarsDao.geTj4(data);
	}

}
