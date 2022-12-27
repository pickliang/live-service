package io.live_mall.modules.server.model;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class OrderUtils {
	public  static final  Integer[]  YX_ORDER_STATUS= {2,4};
	public  static final  Integer[]  YY_ORDER_STATUS= {0,2,4,5};

	// 待报备-1: 状态0:待审核 1:已驳回 2：已过审3 ：已退款 4：已成立 5：已结束 6：已退回
    public static   void orderStup(RaiseEntity raiseEntity,OrderEntity order,Integer status,String bhRemark) {
    	try {
        	if(status == -1) {
        		JSONArray jsonObject = new JSONArray();
    			OrderStup.add("预约申请成功，请协助客户完成打款及签订合同后提交报备信息。请于"+getEndDate(raiseEntity, order)+"之前完成报备，以免额度被释放。", DateUtil.formatDateTime(new Date()), jsonObject);
    			order.setOrderStup(jsonObject.toJSONString());
        	}else {
        		 String msg="";
        		 if(status == 0){
         			msg = "报备成功，等待订单审核。";
         		}
        		if(status == 6){
        			msg = "订单被驳回，驳回原因："+bhRemark;
        		}
        		if(status == -2){
        			msg = "订单被驳回，驳回原因：超过订单报备截止时间，系统自动驳回！";
        		}
        		if(status == 2){
        			msg = "订单审核通过，等待产品成立。";
        		}
        		if(status == 5){
        			msg = "订单审核报备超时,已结束。";
        		}
        		if(status == 4){
        			msg = "订单已成立，于"+DateUtil.formatDate(raiseEntity.getEstablishTime())+"进入存续。";
        		}
        		if(status == 1){
        			msg = "订单被驳回，驳回原因："+bhRemark;
        		}
        		if(status == 3){
        			msg = "订单已退款";
        		}
        		String orderStup = order.getOrderStup();
    			JSONArray jsonObject;
    			if(StringUtils.isNotBlank(orderStup)) {
    				jsonObject = JSONArray.parseArray(orderStup);
    			}else {
    				jsonObject= new JSONArray();
    			}
    			OrderStup.add(msg, DateUtil.formatDateTime(new Date()), jsonObject);
    			order.setOrderStup(jsonObject.toJSONString());
        	}
	} catch (Exception e) {
		// TODO: handle exception
		log.error("OrderUtils错误",e);
	}}


    public static String getEndDate(RaiseEntity raiseEntity,OrderEntity orderEntity) {
		// TODO Auto-generated method stub
    	if("0".equals(raiseEntity.getDeadlineTimeType())) {
			Date endDate = orderEntity.getAppointTime();
			String[] split = raiseEntity.getDeadlineTime().split(",");
			if(split.length > 3){
				endDate = DateUtil.offsetDay(endDate,Integer.valueOf(split[0]));
				endDate =DateUtil.offsetHour(endDate, Integer.valueOf(split[1]));
				endDate =DateUtil.offsetMinute(endDate, Integer.valueOf(split[2]));
				return DateUtil.formatDateTime(endDate);
			}
			return DateUtil.formatDateTime(DateUtil.offsetMinute(endDate,24));
		}else {
			DateTime endDate = DateUtil.offsetHour(orderEntity.getAppointTime(), Integer.valueOf(raiseEntity.getDeadlineTime()));
			return DateUtil.formatDateTime(endDate);
		}
	}

    public static Double getRate(String data,Integer money){
   	 List<JSONObject> list = JSONArray.parseArray(data, JSONObject.class);
  	  //认购金额
  	  Collections.sort(list, (stu1, stu2) -> stu2.getInteger("date") > stu1.getInteger("date") ? -1:1 );
  	  JSONObject item=null;
  	  for (JSONObject jsonObject : list) {
  		 if(jsonObject.getInteger("date")  <= money ){
  			item= jsonObject;
  		 }else {
  			 break;
  		 }
  	  }
  	  if(item !=null) {
  		  return item.getDouble("name");
  	  }
  	  return 0.0d;

	}


    public static BigDecimal getRateBigDecimal(String data,Integer money){
      	 List<JSONObject> list = JSONArray.parseArray(data, JSONObject.class);
     	  //认购金额
     	  Collections.sort(list, (stu1, stu2) -> stu2.getInteger("date") > stu1.getInteger("date") ? -1:1 );
     	  JSONObject item=null;
     	  for (JSONObject jsonObject : list) {
     		 if(jsonObject.getInteger("date") <= money ){
     			item= jsonObject;
     		 }else {
     			 break;
     		 }
     	  }
     	  if(item !=null) {
     		  return item.getBigDecimal("name");
     	  }
     	  return null;

   	}


    public static BigDecimal getYjRateBigDecimal(String data,Integer money){
     	 List<JSONObject> list = JSONArray.parseArray(data, JSONObject.class);

     	 if(list == null || list.size() == 0){
     	   return  null;
       }
    	  //认购金额
    	  Collections.sort(list, (stu1, stu2) -> stu2.getInteger("name") > stu1.getInteger("name") ? -1:1 );
    	  JSONObject item=null;
    	  for (JSONObject jsonObject : list) {
    		 if(jsonObject.getInteger("name") <= money ){
    			item= jsonObject;
    		 }else {
    			 break;
    		 }
    	  }
    	  if(item !=null) {

          String parsernt = item.getString("parsernt").replaceAll("%","");

          return  new BigDecimal(parsernt);
    	  }
    	  return null;

  	}




    /**
	 * @param method 类型
	 * @param clrz 成立时间
	 * @param ys   余数
	 * @param gg   期数
	 * @param dayNum 日期
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String>  getClrz(String method,Date clrz,Integer ys,Integer dayNum){
		List<String>  qisj=new ArrayList<String>();
		if("1".equals(method) ){
			Integer  js= ys/3;//季数
			for (int i = 0; i < js; i++) {
				DateTime offsetMonth = DateUtil.offsetMonth(clrz,(i+1)*3);
				if(dayNum!=null) {
					offsetMonth.setDate(clrz.getDate());
				}
				qisj.add(DateUtil.formatDate(offsetMonth));
			}

		}else if("2".equals(method)){
			Integer js = ys/3;
			Date fist_mouth = new Date();
			fist_mouth.setMonth(DateUtil.quarter(clrz)*3-1);
			if(dayNum!=null) {
				fist_mouth.setDate(dayNum);
			}
			qisj.add(DateUtil.formatDate(fist_mouth));
			for (int i = 1; i < js; i++) {
				String string = qisj.get(i-1);
				DateTime offsetMonth = DateUtil.offsetMonth(DateUtil.parseDate(string),3);
				qisj.add(DateUtil.formatDate(offsetMonth));
			}
		}else if("3".equals(method)){
			Integer  js= ys/6;//季数
			for (int i = 0; i < js; i++) {
				DateTime offsetMonth = DateUtil.offsetMonth(clrz,(i+1)*6);
				if(dayNum!=null) {
					offsetMonth.setDate(dayNum);
				}
				qisj.add(DateUtil.formatDate(offsetMonth));
			}
		}else if("4".equals(method)){
			Integer js = ys/6;
			Date fist_mouth = new Date();
			fist_mouth.setMonth(DateUtil.quarter(clrz)*6-1);
			if(dayNum!=null) {
				fist_mouth.setDate(dayNum);
			}
			qisj.add(DateUtil.formatDate(fist_mouth));
			for (int i = 1; i < js; i++) {
				qisj.add(DateUtil.formatDate( DateUtil.offsetMonth(DateUtil.parseDate(qisj.get(i-1)),6)));
			}
		}
		return qisj;

	}



public static String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String unit[] = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String dst = "";
        int count = 0;
        while(src > 0) {
            dst = (num[src % 10] + unit[count]) + dst;
            src = src / 10;
            count++;
        }
        return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }


    public static   class OrderStup{
    	private  String title;
    	private  String desc;



		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public OrderStup(String title ,String date) {
    		this.title=title;
    		this.desc=date;
    	}

    	public static void add(String text ,String date,JSONArray array ) {
    		array.add(new OrderStup(text,date));
    	}



    }
}
