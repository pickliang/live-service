package io.live_mall.modules.server.utils;

import java.util.Date;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
/**
 * 获取季度的起始时间
 * @author daitao
 *
 */
public class EcharUtils {
	
	 public static void getDateMonth(Map<String,Object> param) {
		 param.put("startDate",DateUtil.formatDateTime( DateUtil.beginOfMonth(new Date())));
		 param.put("endDate",DateUtil.formatDateTime( DateUtil.endOfMonth(new Date())));
	 }
	 
	 
	 public static void getDateQuarter(Map<String,Object> param) {
		 param.put("startDate", DateUtil.formatDateTime(DateUtil.beginOfQuarter(new Date())));
		 param.put("endDate",DateUtil.formatDateTime( DateUtil.endOfQuarter(new Date())));
	 }
	
	 
	 public static void getDateYear(Map<String,Object> param) {
		 param.put("startDate",DateUtil.formatDateTime( DateUtil.beginOfYear(new Date())));
		 param.put("endDate", DateUtil.formatDateTime(DateUtil.endOfYear(new Date())));
	 }
}
