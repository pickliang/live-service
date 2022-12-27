package io.live_mall;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

public class ProductUnit {
	/**
	 * 1、按季付息（以成立日作为付息日），即以（成立日后每3个月）作为付息日。比如采用该规则一个产品成立日（也是起息日）是2019年10月12日，该产品期限是12个月，则付息日分别为：2020年1月12日、2020年4月12日，2020年7月12日，2020年10月12日，2021年1月12日。产品到期日（也就是2021年1月12日）兑付最后一次预期收益（如有）及本金。
2、按季付息（以每季末月固定日作为起息日），即以每年3、6、9、12月的指定日期作为付息日。如采用该规则一个产品成立日是2020/5/8，期限是12个月，设置的日期是23日，则产品到期日是2021/5/8，付息日分别是2020/6/23，2020/9/23，2020/12/23，2021/3/23。
再举一个例子，该规则下，还有一个产品成立日2020/6/5，期限是12个月，设置的日期是23日，则产品到期日是2021/6/5，则付息日是2020/9/23，2020/12/23，2021/3/23。（注意，这种情况下只付息三次）
3、按半年付息（以成立日作为付息日），即以成立后每半年作为付息日，如一个产品成立日是2019年10月12日，该产品期限是24个月，则付息日分别为：2020年4月12日，2020年10月12日，2021年4月12日，2021年10月12日
4、按半年付息（以半年末月固定日作为付息日），即以每年6、12月的指定日期作为付息日。如采用该规则一个产品成立日是2020/5/8，期限是24个月，设置的日期是23日，则产品到期日是2022/5/8，付息日分别是2020/6/23， 2020/12/23，2021/6/23， 2021/12/23
再举一个例子，该规则下，还有一个产品成立日2020/6/5，期限是24个月，设置的日期是23日，则产品到期日是2022/6/5，则付息日是2020/12/23，2021/6/23，2021/12/23。
	 * @param args
	 */
	public static void main(String[] args) {
	  String str=" [{\"date\":\"50\",\"isOK\":true,\"name\":\"8.5\",\"address\":\"\"},{\"date\":\"40\",\"isOK\":true,\"name\":\"8.8\",\"address\":\"\"},{\"date\":\"300\",\"isOK\":true,\"name\":\"9.0\",\"address\":\"\"}]";
	  Double rate = getRate(str, 45);
	  System.err.println(rate);
	}
	
	
    public static Double getRate(String data,Integer money){
    	 List<JSONObject> list = JSONArray.parseArray(data, JSONObject.class);
   	  //认购金额
   	  Collections.sort(list, (stu1, stu2) -> stu2.getInteger("date") > stu1.getInteger("date") ? -1:1 );
   	  JSONObject item=null;
   	  for (JSONObject jsonObject : list) {
   		 if(jsonObject.getInteger("date") > money ){
   			item= jsonObject;
   			 break;
   		 }
   	  }
   	  if(item !=null) {
   		  return item.getDouble("name");
   	  }
   	  return null;
		
	}
	
	/**
	 * 
	 * @param clrz
	 * @param qs
	 * @param dayNum
	 * @return
	 */
	private static List<String>  getClrz(Date clrz,Integer ys,Integer gg,Integer dayNum){
		Integer  js= ys/gg;//季数
		Integer  ysjs= ys%gg;//季数
		List<String>  qisj=new ArrayList<String>();
		for (int i = 0; i < js; i++) {
			DateTime offsetMonth = DateUtil.offsetMonth(clrz,(i+1)*gg);
			if(dayNum!=null) {
				offsetMonth.setDate(dayNum);
			}
			qisj.add(DateUtil.formatDate(offsetMonth));
		}
		//如果有余数。。。
		if(ysjs > 0) {
			DateTime offsetMonth = DateUtil.offsetMonth(clrz,js*gg+ysjs);
			if(dayNum!=null) {
				offsetMonth.setDate(dayNum);
			}
			qisj.add(DateUtil.formatDate(offsetMonth));
		}
		return qisj;
	}
	
	
	@Test
	public void testName() throws Exception {
		
	}
}
