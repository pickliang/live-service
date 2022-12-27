package io.live_mall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.db.sql.Order;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import io.live_mall.modules.server.entity.ManagerUserEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.ProductUnitEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.entity.SysConfigEntity;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import io.live_mall.modules.server.service.ManagerUserService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.ProductUnitService;
import io.live_mall.modules.server.service.RaiseService;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysConfigService;
import io.live_mall.modules.sys.service.SysUserService;



@RunWith(SpringRunner.class)
@SpringBootTest
public class SmzTest {
	
	@Autowired
	SysConfigService sysConfigService;
	
	
	@Autowired
	RaiseService raiseService;
	@Autowired
	ProductService productService;
	@Autowired
	ProductUnitService productUnitService;
	
	@Autowired
	SysOrgGroupService orgserverice;
	
	@Autowired
	SysUserService userserice;
	
	
	@Autowired
	OrderService orderService;
	
	
	@Autowired
	ManagerUserService managerUserService;
	
	
	/**
	 * 获取数据
	 */
	@Test
	public  void go() {
		//产品列表
		JSONObject sendGet = sendGet("https://sc.fangyuaninfo.com/api/product/api/products?offset=0&limit=100&_=kj2y3at2");
		JSONArray jsonArray = sendGet.getJSONObject("result").getJSONArray("rows");
		List<JSONObject>  productList=new ArrayList<JSONObject>();
		int i=0;
		for (Object object : jsonArray) {
			if(i <10) {
				JSONObject item = (JSONObject)object;
				//产品详情 
				//编辑url https://sc.fangyuaninfo.com/api/product/api/product/fund?id=a6782c3146004c22b727757c5cf7dd06&_=kj2yiiaq
				JSONObject prdouct = null;
				if(item.getIntValue("category") == 1){
					prdouct = sendGet("https://sc.fangyuaninfo.com/api/product/api/product/fund?id="+item.getString("id")+"&_=kj2yiiaq").getJSONObject("result");

				}
				
				if(item.getIntValue("category") == 2){
					prdouct = sendGet("https://sc.fangyuaninfo.com/api/product/api/product/trust?id="+item.getString("id")+"&_=kj2yiiaq").getJSONObject("result");

				}
				
				if(item.getIntValue("category") == 3){
					prdouct = sendGet("https://sc.fangyuaninfo.com/api/product/api/product/bond?id="+item.getString("id")+"&_=kj2yiiaq").getJSONObject("result");

				}
				
				if(item.getIntValue("category") == 4){
					prdouct = sendGet("https://sc.fangyuaninfo.com/api/product/api/product/assets?id="+item.getString("id")+"&_=kj2yiiaq").getJSONObject("result");

				}
				
			    //产品单元列表
				JSONArray units = sendGet("https://sc.fangyuaninfo.com/api/product/api/product/units?proId="+item.getString("id")+"&offset=0&limit=100&_=kj2yk7r0").getJSONObject("result").getJSONArray("rows");;
				if(units !=null && units.size()> 0 ){
					prdouct.put("unit", sendGet("https://sc.fangyuaninfo.com/api/product/api/product/unit?id="+((JSONObject)units.get(0)).getString("id")+"&_=kj2ykev4").getJSONObject("result"));
				}
				//募集期
				JSONArray raise = sendGet("https://sc.fangyuaninfo.com/api/operation/api/scheme/schemes?productId="+item.getString("id")+"&offset=0&limit=100&_=kj301jaw").getJSONObject("result").getJSONArray("rows");;
				List<JSONObject>  raiseList=new ArrayList<JSONObject>();
				for (Object raise_object :  raise) {
					JSONObject raiseitem = (JSONObject)raise_object;
					raiseList.add(sendGet("https://sc.fangyuaninfo.com/api/operation/api/scheme/scheme?id="+raiseitem.getString("id")+"&processType=edit&_=kj3087yo").getJSONObject("result"));
				}
				//订单
				JSONArray orders = sendGet("https://sc.fangyuaninfo.com/api/operation/api/order/orders?timeCategory=1&productName="+item.getString("name")+"&offset=0&limit=100&_=kj30fl9s").getJSONObject("result").getJSONArray("rows");;
				List<JSONObject>  orderList=new ArrayList<JSONObject>();
				for (Object order_object :  orders) {
					JSONObject orderitem = (JSONObject)order_object;
					orderList.add(sendGet("https://sc.fangyuaninfo.com/api/operation/api/order/order?id="+orderitem.getString("id")+"&_=kj30m3dr").getJSONObject("result"));
				}
				prdouct.put("orderList", orderList);
				prdouct.put("raiseList", raiseList);
				productList.add(prdouct);
			}
			i++;
		}
		
		SysConfigEntity  item =new SysConfigEntity();
		item.setParamKey("prdouct");
		item.setParamValue(JSONObject.toJSONString(productList));
		sysConfigService.save(item);
	}
	
	
	
	@Test
	public  void parse() {
		SysConfigEntity sysConfigEntity = sysConfigService.getById("9");
		String paramValue = sysConfigEntity.getParamValue();
		JSONArray jsonObject = JSONObject.parseArray(paramValue);
		for (Object object : jsonObject) {
			JSONObject item =(JSONObject)object;
			ProductEntity productEntity = product(item);
			if(productEntity == null ) {
				continue;
			}
			/*ProductUnitEntity productUnitEntity = unit(item);*/
			/*	List<RaiseEntity> raiseList = raise(item);*/
			List<OrderEntity> order = order(item);
//			raiseService.saveBatch(raiseList);
//			if(productUnitEntity !=null) {
//				productUnitService.save(productUnitEntity);
//			}
//			productService.save(productEntity);
			/*orderService.updateBatchById(order);*/
		}
	}
	
	private List<OrderEntity> order(JSONObject item) {
		List<OrderEntity> list = new ArrayList<OrderEntity>();
		JSONArray orderList = item.getJSONArray("orderList");
		for (Object object : orderList) {
			JSONObject order = (JSONObject)object;
			OrderEntity orderEntity = new OrderEntity();
			orderEntity.setProductId(order.getString("productId"));
			orderEntity.setRaiseId(order.getString("schemeId"));
			// 待报备-1: 状态0:待审核 1:已驳回 2：已过审3 ：已退款 4：已成立 5：已结束 6：已退回 
			if("200".equals(order.getString("state"))) {
				orderEntity.setStatus(5);
			}
			if("100".equals(order.getString("state"))) {
				System.out.println(JSON.toJSONString(order));
				orderEntity.setStatus(4);			
			}
			if("90".equals(order.getString("state"))) {
				orderEntity.setStatus(3);		
			}if("80".equals(order.getString("state"))) {
				orderEntity.setStatus(2);	
			}if("60".equals(order.getString("state"))) {
				orderEntity.setStatus(1);	
			}if("40".equals(order.getString("state"))) {
				orderEntity.setStatus(0);	
			}if("20,50".contains(order.getString("state"))  ) {
				orderEntity.setStatus(-1);	
			}
			
			orderEntity.setProductUnitId(order.getString("unitId"));
			if(order.getString("salesName") !=null) {
				SysUserEntity userEntity = userserice.getByReaName(order.getString("salesName") );
				if(userEntity !=null) {
					orderEntity.setSaleId(userEntity.getUserId());
					if(userEntity.getOrg() !=null) {
						orderEntity.setOrgId(userEntity.getOrg().getOrgId());
					}
				}
			}
			String string = order.getString("custPhone");
			if(string !=null) {
				System.out.println(string);
			}
			orderEntity.setPhone(string);
			orderEntity.setOrderNo(order.getString("orderCode"));
			orderEntity.setCreateDate(item.getDate("applicationTime"));
			orderEntity.setCustomerName(order.getString("custName"));
			orderEntity.setAppointMoney(order.getInteger("amount"));
			orderEntity.setAppointTime(order.getDate("applicationTime"));
			orderEntity.setCardType(order.getString("custCertificationType"));
			orderEntity.setCardNum(order.getString("custCertificationNumber"));
			
			if(order.getJSONArray("custCertificationFront") !=null && order.getJSONArray("custCertificationFront").size()>0) {
				orderEntity.setCardPhotoR(order.getJSONArray("custCertificationFront").getJSONObject(0).toJSONString());
			}
			
			if(order.getJSONArray("custCertificationBack") !=null && order.getJSONArray("custCertificationBack").size()>0) {
				orderEntity.setCardPhotoL(order.getJSONArray("custCertificationBack").getJSONObject(0).toJSONString());
			}
			if(order.get("custCertificationPeriodForever") !=null &&  order.getInteger("custCertificationPeriodForever") == 1 ) {
				orderEntity.setCardTimeType("长期");
			}else {
				orderEntity.setCardTimeType("固定期");
				orderEntity.setCardTime(order.getString("custCertificationPeriodFrom")+"-"+order.getString("custCertificationPeriodTo"));
			}
			orderEntity.setEmail(order.getString("email"));
			orderEntity.setAgreementNo(order.getString("protocolNumber"));
			orderEntity.setSignDate(order.getString("contractDate"));
			
			orderEntity.setBankNo(order.getString("bankAccount"));
			orderEntity.setOpenBank(order.getString("bank"));
			orderEntity.setBranch(order.getString("bankBranch"));
			if(order.getJSONArray("bankCardFront") !=null && order.getJSONArray("bankCardFront").size()>0) {
				orderEntity.setBankCardFront(order.getJSONArray("bankCardFront").getJSONObject(0).toJSONString());
			}
			if(order.getJSONArray("bankCardBack") !=null && order.getJSONArray("bankCardBack").size()>0) {
				orderEntity.setBankCardBack(order.getJSONArray("bankCardBack").getJSONObject(0).toJSONString());
			}
			
			if(order.getJSONArray("applicationFormOpenAccount") !=null && order.getJSONArray("applicationFormOpenAccount").size()>0) {
				orderEntity.setOpenAccount(order.getJSONArray("applicationFormOpenAccount").getJSONObject(0).toJSONString());
			}
			if(order.getJSONArray("subscriptionAgreement") !=null && order.getJSONArray("subscriptionAgreement").size()>0) {
				orderEntity.setSubAgree(order.getJSONArray("subscriptionAgreement").getJSONObject(0).toJSONString());
			}
			if(order.getJSONArray("paymentVoucher") !=null && order.getJSONArray("paymentVoucher").size()>0) {
				orderEntity.setPaymentSlip(order.getJSONArray("paymentVoucher").getJSONObject(0).toJSONString());
			}
			
			if(order.getJSONArray("custAssets") !=null && order.getJSONArray("custAssets").size()>0) {
				orderEntity.setAssetsPro(order.getJSONArray("custAssets").getJSONObject(0).toJSONString());
			}
			
			if(order.getJSONArray("otherAccessories") !=null && order.getJSONArray("otherAccessories").size()>0) {
				orderEntity.setOtherFile(order.getJSONArray("otherAccessories").getJSONObject(0).toJSONString());
			}
			orderEntity.setAduitTime(order.getDate("approveTime"));
			if(order.getString("approveUserName") !=null) {
				SysUserEntity userEntity = userserice.getOne(new QueryWrapper<SysUserEntity>().eq("realname", order.getString("approveUserName")));
				if(userEntity !=null) {
					orderEntity.setAduitId(userEntity.getUserId());
				}
			}
			orderEntity.setAduitResult(order.getString("rejectReason"));
			list.add(orderEntity);
		}
		return list;
	}
	
	
	
	private List<RaiseEntity> raise(JSONObject item) {
		List<RaiseEntity> list = new ArrayList<RaiseEntity>();
		JSONArray raiseList = item.getJSONArray("raiseList");
		for (Object object : raiseList) {
			/*<ul style="">
			<li class="w-default-selected w-selected" data-text="全部" data-value="">全部</li>
			<li data-text="草稿" data-value="10">草稿</li><li data-text="待发行" data-value="20">待发行</li>
			<li data-text="发行中" data-value="30">发行中</li><li data-text="已叫停" data-value="40">已叫停</li>
			<li data-text="已封帐" data-value="50">已封帐</li><li data-text="已成立" data-value="60">已成立</li>
			<li data-text="已结束" data-value="70">已结束</li></ul>*/
			JSONObject raise = (JSONObject)object;
			RaiseEntity raiseEntity = new RaiseEntity();
			//-1 草稿 0：待发行 1：发行中 2：已叫停 3：已封账 4：已成立 5：已结束
			if("10".equals(raise.getString("state"))) {
				raiseEntity.setStatus(-1);
			}
			if("20".equals(raise.getString("state"))) {
				raiseEntity.setStatus(0);			
			}
			if("30".equals(raise.getString("state"))) {
				raiseEntity.setStatus(1);		
				
			}if("40".equals(raise.getString("state"))) {
				raiseEntity.setStatus(2);	
				
			}if("50".equals(raise.getString("state"))) {
				raiseEntity.setStatus(3);	
			}if("60".equals(raise.getString("state"))) {
				raiseEntity.setStatus(4);	
				System.out.println(JSONObject.toJSON(raise));
			}if("70".equals(raise.getString("state"))) {
				raiseEntity.setStatus(5);	
			}
			
			raiseEntity.setCreateDate(raise.getDate("raiseTimeMin"));
			raiseEntity.setId(raise.getString("id"));
			raiseEntity.setProductId(raise.getString("productId"));
			raiseEntity.setIsShow(raise.getInteger("isVisible"));
			raiseEntity.setRaiseName(raise.getString("name"));
			raiseEntity.setRaiseMoney(raise.getInteger("raisingAmt"));
			raiseEntity.setEstablishScale(raise.getString("foundedScale"));
			raiseEntity.setInvestorNum(raise.getInteger("maxInvestors"));
			if(raise.getInteger("transactionType") ==0 ) {
				raiseEntity.setTranType("认购（首募）");
			}
			if(raise.getInteger("transactionType") ==1 ) {
				raiseEntity.setTranType("申购");			
			}
			if(raise.getInteger("transactionType") ==2 ) {
				raiseEntity.setTranType("份额转让");
			}
			
			raiseEntity.setBeginDate(raise.getDate("raiseTimeMin"));
			raiseEntity.setEndDate(raise.getDate("raiseTimeMax"));
			
			if(raise.getInteger("reportEndTimeType") ==1 ) {
				raiseEntity.setDeadlineTimeType("0");
				raiseEntity.setDeadlineTime( raise.getString("reportEndTimeDays")+","+	raise.getString("reportEndTimeHour")+","+raise.getString("reportEndTimeMinute"));
			}
			
			if(raise.getInteger("reportEndTimeType") ==2 ) {
				raiseEntity.setDeadlineTimeType("1");
				raiseEntity.setDeadlineTime(raise.getString("reportEndTimeHours"));
			}
			
			String orderStandard="";
			if(raise.getInteger("orderAmtScaleMinType") ==0 ) {
				orderStandard="不限制";
			}
			if(raise.getInteger("orderAmtScaleMinType") ==3 ) {
				orderStandard="等于";
			}
			if(raise.getInteger("orderAmtScaleMinType") ==4 ) {
				orderStandard="大于等于";
			}
			if(raise.getInteger("orderAmtScaleMinType") ==5 ) {
				orderStandard="大于";
			}
			orderStandard=orderStandard+","+raise.getString("orderAmtScaleMin");
			
			if(raise.getInteger("orderAmtScaleMaxType") !=null && raise.getInteger("orderAmtScaleMaxType") ==0 ) {
				orderStandard+=",不限制上限";
			}
			if(raise.getInteger("orderAmtScaleMaxType") !=null && raise.getInteger("orderAmtScaleMaxType") ==1 ) {
				orderStandard+=",小于";
			}
			if(raise.getInteger("orderAmtScaleMaxType")  !=null  && raise.getInteger("orderAmtScaleMaxType") ==2 ) {
				orderStandard+=",小于等于";
			}
			
			if(raise.getString("orderAmtScaleMax") !=null) {
				orderStandard+=raise.getString("orderAmtScaleMax");
			}
			raiseEntity.setOrderStandard(orderStandard);
			raiseEntity.setSmallOrderNum(raise.getInteger("smallOrderNumberMax"));
			raiseEntity.setBigOrderMoney(raise.getBigDecimal("bigOrderAmtMin"));
			raiseEntity.setUpdateImage(raise.getInteger("isSignUpPhoto"));
			
			JSONArray jsonArray = raise.getJSONArray("units");
			if(jsonArray !=null && jsonArray.size() > 0 ) {
				JSONObject unit = jsonArray.getJSONObject(0);
				raiseEntity.setCommissionCalculationMethod(unit.getInteger("feeType"));
				JSONArray advisorFeeList = unit.getJSONArray("advisorFee");
				if(advisorFeeList !=null) {
					for (Object advisorFee : advisorFeeList) {
						JSONObject item_fee =(JSONObject)advisorFee;
						item_fee.put("parsernt",item_fee.get("frontEndRate"));
						item_fee.put("name", item_fee.get("amt"));
						item_fee.put("desc",  item_fee.get("backEndRate"));
					}
					raiseEntity.setCommission(advisorFeeList.toJSONString());
				}
				raiseEntity.setPerformanceCoefficient(unit.getString("performanceRatio"));
				raiseEntity.setRateDesc(unit.getString("advisorFeeRemark"));
				raiseEntity.setProductUnitId(unit.getString("unitId"));
				
			}
			list.add(raiseEntity);
		}
		return list;
	}
	
	
	
	private ProductUnitEntity unit(JSONObject item) {
		JSONObject unit = item.getJSONObject("unit");
		ProductUnitEntity productUnit= new ProductUnitEntity();
		if(unit !=null) {
			productUnit.setUnitName(unit.getString("name"));
			productUnit.setIncomeDesc(unit.getString("incomeDistributionRemark"));
			productUnit.setProductId(unit.getString("proId"));
			productUnit.setId(unit.getString("id"));
			JSONObject income = unit.getJSONObject("income");
			if(income !=null) {
				if(income.getInteger("type") == 2) {
					productUnit.setIncomeType(0);
					JSONArray fixed = income.getJSONArray("fixed");
					for (Object object : fixed) {
						//[{"date":"50","isOK":true,"name":"9","address":""},{"date":"100","isOK":true,"name":"9.5","address":""}]
						JSONObject fixeditem = (JSONObject)object;
						fixeditem.put("name",fixeditem.get("rate"));
						fixeditem.put("isOK",true);
						fixeditem.put("date", fixeditem.get("amt"));
					}
					productUnit.setIncomeData(fixed.toJSONString());
				}
				if(income.getInteger("type") == 5) {
					productUnit.setIncomeType(1);
					JSONArray fixed = income.getJSONArray("combined");
					for (Object object : fixed) {
						//[{"date":"50","isOK":true,"name":"9","address":""},{"date":"100","isOK":true,"name":"9.5","address":""}]
						JSONObject fixeditem = (JSONObject)object;
						fixeditem.put("name",fixeditem.get("rate"));
						fixeditem.put("isOK",true);
						fixeditem.put("date", fixeditem.get("amt"));
					}
					productUnit.setIncomeData(fixed.toJSONString());
				}
				if(income.getInteger("type") ==3) {
					productUnit.setIncomeType(2);
					productUnit.setIncomeData(income.getString("floatingRemark"));
				}
			}
			
			productUnit.setDeadlineDesc(unit.getString("durationRemark"));
			
			
			if(unit.getJSONObject("duration") !=null){
				JSONObject duration = unit.getJSONObject("duration");
				if(duration.getInteger("type") == 3) {
					productUnit.setDeadlineSetType(0);
				}
				if(duration.getInteger("type") == 1) {
					productUnit.setDeadlineSetType(1);		
					if("1".equals(duration.getString("afterFoundedDateUnit"))) {
						productUnit.setDeadlineData(duration.getString("afterFoundedDateNumber")+","+"年");
					}else if("2".equals(duration.getString("afterFoundedDateUnit"))) {
						productUnit.setDeadlineData(duration.getString("afterFoundedDateNumber")+","+"月");
					}else if("3".equals(duration.getString("afterFoundedDateUnit"))) {
						productUnit.setDeadlineData(duration.getString("afterFoundedDateNumber")+","+"日");
					}
				}
				if(duration.getInteger("type") == 5) {
					productUnit.setDeadlineSetType(2);
					if("1".equals(duration.getString("afterFoundedDateMaxUnit"))) {
						productUnit.setDeadlineData(duration.getString("afterFoundedDateMaxNumber")+","+"年");
					}else if("2".equals(duration.getString("afterFoundedDateMaxUnit"))) {
						productUnit.setDeadlineData(duration.getString("afterFoundedDateMaxNumber")+","+"月");
					}else if("3".equals(duration.getString("afterFoundedDateMaxUnit"))) {
						productUnit.setDeadlineData(duration.getString("afterFoundedDateMaxNumber")+","+"日");
					}
				}
				
				if(duration.getInteger("type") == 2) {
					productUnit.setDeadlineSetType(3);
					productUnit.setDeadlineData(duration.getString("scheduledDate"));
				}
				
				if(duration.getInteger("type") == 4) {
					productUnit.setDeadlineSetType(4);
					productUnit.setDeadlineData(duration.getString("remark"));
				}
			}
			
			
			
			if(unit.getJSONObject("lockInPeriod") !=null){
				JSONObject lockInPeriod = unit.getJSONObject("lockInPeriod");
				if(lockInPeriod.getInteger("type") == 1) {
					productUnit.setCloseType(0);
				}
				if(lockInPeriod.getInteger("type") == 2) {
					productUnit.setCloseType(1);		
					if("1".equals(lockInPeriod.getString("afterFoundedDateUnit"))) {
						productUnit.setClosedDetail(lockInPeriod.getString("afterFoundedDateNumber")+","+"年");
					}else if("2".equals(lockInPeriod.getString("afterFoundedDateUnit"))) {
						productUnit.setClosedDetail(lockInPeriod.getString("afterFoundedDateNumber")+","+"月");
					}else if("3".equals(lockInPeriod.getString("afterFoundedDateUnit"))) {
						productUnit.setClosedDetail(lockInPeriod.getString("afterFoundedDateNumber")+","+"日");
					}
				}
				if(lockInPeriod.getInteger("type") == 3) {
					productUnit.setCloseType(2);
					productUnit.setClosedDetail(lockInPeriod.getString("scheduledDate"));
				}
				if(lockInPeriod.getInteger("type") == 4) {
					productUnit.setCloseType(3);
					
				}
			}
			
			
			if(unit.getJSONObject("renewable") !=null){
				JSONObject renewable = unit.getJSONObject("renewable");
				if(renewable.getInteger("type") == 1) {
					productUnit.setCloseType(0);
				}
				if(renewable.getInteger("type") == 2) {
					productUnit.setCloseType(1);		
				}
				if(renewable.getInteger("type") == 3) {
					productUnit.setCloseType(2);
					productUnit.setClosedDetail(renewable.getString("interestRate"));
				}
			}
		}
		return productUnit;
	}
	
	private  ProductEntity product(JSONObject item){
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(item.getString("id"));
		
		//10 草稿
		//20 可发行
		//30 募集完成 
		//70 已清盘
		//状态 0：为提交 1：可发行 2：募集完成 3：已清盘
		if("10".equals(item.getString("state"))) {
			productEntity.setStatus("0");
		}
		if("20".equals(item.getString("state"))) {
			productEntity.setStatus("1");
		}
		if("30".equals(item.getString("state"))) {
			productEntity.setStatus("2");
		}
		if("70".equals(item.getString("state"))) {
			productEntity.setStatus("3");
		}
		
		if(item.getInteger("category") == 3){
			productEntity.setOnetype("私募债");
		}else {
			return null;
		}
		productEntity.setProductName(item.getString("name"));
		productEntity.setProductBrif(item.getString("shortName"));
		productEntity.setProductType("私募债");
		productEntity.setRecordNo(item.getString("productCode"));
		productEntity.setFinancingParty(item.getString("financier"));
		productEntity.setCreateDate(item.getDate("updateTime"));
		/*
		if("11".equals(item.getString("subCategory"))) {
			productEntity.setProductType("私募股权类");
		}else if("12".equals(item.getString("subCategory"))) {
			productEntity.setProductType("私募证券类");
		}else if("13".equals(item.getString("subCategory"))) {
			productEntity.setProductType("私募其他类");
		}else if("14".equals(item.getString("subCategory"))) {
			productEntity.setProductType("资产配置类");
		}*/
		productEntity.setProductLabel("[\""+item.getString("label")+"\"]");
		
		/**
		 * <li data-text="类固收" data-value="3">类固收</li>
		 * <li data-text="权益类" data-value="1">权益类</li>
		 * <li data-text="净值型" data-value="2" class="w-selected">净值型</li>
		 * <li data-text="无分类" data-value="0">无分类</li>
		 */
		if("0".equals(item.getString("customeizeCategory"))) {
			productEntity.setProductClass("无分类");
		}else if("1".equals(item.getString("customeizeCategory"))) {
			productEntity.setProductClass("权益类");
		}else if("2".equals(item.getString("customeizeCategory"))) {
			productEntity.setProductClass("净值型");
		}else if("3".equals(item.getString("customeizeCategory"))) {
			productEntity.setProductClass("类固收");
		}
		
		
		if(item.getString("managerId") !=null) {
			productEntity.setManagerPeople("[\""+getManger(item.getString("managerId"))+"\"]");
		}
		productEntity.setTrustee(item.getString("trustee"));
		if(item.getInteger("currencyType") == 1) {
			productEntity.setCurrencyType("¥");
		}else {
			productEntity.setCurrencyType("$");
		}
		
		productEntity.setProductScale(item.getString("raisingAmt"));
		productEntity.setInvestmentField(item.getString("investmentField"));
		productEntity.setInvestmentMode(item.getString("investmentStyle"));
		productEntity.setRiskLevel("R"+item.getString("riskLevel"));
		productEntity.setPeopleLimit(item.getString("maxInvestors"));
		
		if("1".equals(item.getString("incomeShowType"))) {
			productEntity.setProductRevenue("业绩比较基准");
		}else if("2".equals(item.getString("incomeShowType"))) {
			productEntity.setProductRevenue("净值");
		}
		productEntity.setTermDetail(item.getString("termRemark"));
		
		
		JSONObject object = item.getJSONObject("term");
		
		if(object.getString("type").equals("2")) {
			if("1".equals(object.getString("afterFoundedDateMaxUnit"))) {
				productEntity.setProductTerm(object.getString("afterFoundedDateMaxNumber")+","+"年");
			}else if("2".equals(object.getString("afterFoundedDateMaxUnit"))) {
				productEntity.setProductTerm(object.getString("afterFoundedDateMaxNumber")+","+"月");
			}else if("3".equals(object.getString("afterFoundedDateMaxUnit"))) {
				productEntity.setProductTerm(object.getString("afterFoundedDateMaxNumber")+","+"日");
			}
			productEntity.setProductTermType(1);
		}else if(object.getString("type").equals("1")) {
			if("1".equals(object.getString("afterFoundedDateUnit"))) {
				productEntity.setProductTerm(object.getString("afterFoundedDateNumber")+","+"年");
			}else if("2".equals(object.getString("afterFoundedDateUnit"))) {
				productEntity.setProductTerm(object.getString("afterFoundedDateNumber")+","+"月");
			}else if("3".equals(object.getString("afterFoundedDateUnit"))) {
				productEntity.setProductTerm(object.getString("afterFoundedDateNumber")+","+"日");
			}
			productEntity.setProductTermType(0);
			
		}else if(object.getString("type").equals("3")) {
				productEntity.setProductTerm(object.getString("scheduledDate"));
				productEntity.setProductTermType(2);
		}else if(object.getString("type").equals("4")) {
			if("1".equals(object.getString("alterableUnit"))) {
				productEntity.setProductTerm("年"+","+object.getString("alterableA")+","+object.getString("alterableB")+","+object.getString("alterableC")+","+object.getString("alterableD"));
			}else if("2".equals(object.getString("alterableUnit"))) {
				productEntity.setProductTerm("月"+","+object.getString("alterableA")+","+object.getString("alterableB")+","+object.getString("alterableC")+","+object.getString("alterableD"));
			}else if("3".equals(object.getString("alterableUnit"))) {
				productEntity.setProductTerm("日"+","+object.getString("alterableA")+","+object.getString("alterableB")+","+object.getString("alterableC")+","+object.getString("alterableD"));
			}
			
			productEntity.setProductTermType(3);
		}else if(object.getString("type").equals("5")) {
			productEntity.setProductTerm(object.getString("unspecifiedRemark"));
			productEntity.setProductTermType(4);
		}
		productEntity.setOpenHouse(item.getString("openDayRemark"));
		JSONObject openDay = item.getJSONObject("openDay");
		if(openDay.getString("type").equals("2")){
			productEntity.setOpenHouseType("0");
		}else if(openDay.getString("type").equals("3")){
			productEntity.setTermTwo(openDay.getString("periodicallyMonth")+","+openDay.getString("periodicallyDate"));
			productEntity.setOpenHouseType("1");
		}else if(openDay.getString("type").equals("4")){
			productEntity.setTermTwo(openDay.getString("scheduledDate"));
			productEntity.setOpenHouseType("2");
		}else if(openDay.getString("type").equals("5")){
			productEntity.setOpenHouseType("3");
		}
		productEntity.setInvestmentConsultant(item.getString("consignee"));
		productEntity.setInvestmentTarget(item.getString("investmentSubject"));
		productEntity.setUseFound(item.getString("investmentTargets"));
		productEntity.setSourcePay(item.getString("repaymentSource"));
		productEntity.setRiskMeasures(item.getString("riskControl"));
		productEntity.setProductHighlights(item.getString("highlights"));
		productEntity.setIntroductionDetail(item.getString("financierIntroduction"));
		productEntity.setGuarantorDetail(item.getString("guarantorIntroduction"));
		productEntity.setConsultantDetail(item.getString("consultantIntroduction"));
		productEntity.setFundManager(item.getString("director"));
		productEntity.setManagerDetail(item.getString("directorIntroduction"));
		if(item.getJSONArray("portfolio") !=null ) {
			List<JSONObject> portfoliolist = item.getJSONArray("portfolio").toJavaList(JSONObject.class);
			for (JSONObject object2 : portfoliolist) {
				object2.put("parsernt", object2.get("proportion"));
				object2.put("desc", object2.get("type"));
				object2.put("isOK", true);
			}
			productEntity.setInvestmentPortfolio(JSONObject.toJSONString(portfoliolist));//组合
		}
		
		productEntity.setFrequency(item.getString("navReleaseFrequency"));
		productEntity.setProductEdition(item.getString("productIntroduction"));
		productEntity.setNoteEdition(item.getString("productIntroductionSms"));
		productEntity.setAgreementDetatil(item.getString("contract"));
		
		productEntity.setInstructions(item.getString("prospectus"));
		productEntity.setFormualTable(item.getString("publicityTable"));
		productEntity.setOtherData(item.getString("otherAttachment"));
		productEntity.setRaiseBank(item.getString("raiseBank"));
		productEntity.setRaiseCount(item.getString("raiseAccountName"));
		productEntity.setRaiseAccount(item.getString("raiseAccount"));
		productEntity.setPayRemark(item.getString("raiseRemark"));
		productEntity.setStartingPoint(item.getString("subscriptionMinAmt"));
		productEntity.setIncrementalAmount(item.getString("increasingAmt"));
		productEntity.setBuyRate(item.getString("subscriptionFee"));
		productEntity.setRateDetail(item.getString("rateDetail"));
		if(item.getJSONArray("otherFee") !=null ) {
			List<JSONObject> list = item.getJSONArray("otherFee").toJavaList(JSONObject.class);
			for (JSONObject object2 : list) {
				object2.put("parsernt", object2.get("fee"));
				object2.put("desc", object2.get("remark"));
				object2.put("isOK", true);
			}
			productEntity.setInvestmentPortfolio(JSONObject.toJSONString(list));//组合
		}
		return productEntity;
	}
	
	
	private String  getManger(String id) {
		String joson="{\"code\":200,\"message\":\"操作成功\",\"result\":{\"total\":\"24\",\"rows\":[{\"id\":\"5f43135beb084d0791de572e20074198\",\"type\":\"1\",\"name\":\"上海觅总投资管理有限公司\",\"registrationNumber\":\"\",\"profile\":\"<p>上海觅总投资管理有限公司</p>\\n\",\"createTime\":1608618028000,\"updateTime\":1608618028000,\"updateUserId\":\"ddec790c97fe4b4d9974a502e90463c5\",\"updateUserName\":\"李亦雷\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"08d027d20a034b40bd835b1ebf93831b\",\"type\":\"2\",\"name\":\"测试信托公司1\",\"registrationNumber\":\"\",\"profile\":\"<p>测试信托公司1</p>\\n\",\"createTime\":1607409308000,\"updateTime\":1607409308000,\"updateUserId\":\"44cf4381b75346169d1bfbd9d2695c23\",\"updateUserName\":\"方圆测试2\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"f11486891eea4c0286073fae62311ead\",\"type\":\"3\",\"name\":\"安阳国实信用资产登记备案中心有限公司\",\"registrationNumber\":\"\",\"profile\":\"<p>安阳国实信用资产登记备案中心有限公司（以下简称&ldquo;安阳登记中心&rdquo;）是经安阳市文峰区人民政府批准设立（文政文【2020】64号，经安阳市文峰区金融服务办公室同意设立（文金办【2020】11号）的一家信用资产登记、备案及咨询服务机构。</p>\\n\\n<p>安阳登记中心的股东之一安阳高新区管委会财政局通过两个平台共持股安阳登记中心40%的股权，安阳高新区位于市区东南，成立于1992年8月，1995年3月被批准为省级高新区，2010年9月被国务院批准为国家高新区。</p>\\n\",\"createTime\":1607324763000,\"updateTime\":1607324763000,\"updateUserId\":\"97315041f3374c549d814ad17d10ae28\",\"updateUserName\":\"何静\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"572fce7abbba4351a0bfa8d3c652d344\",\"type\":\"2\",\"name\":\"111\",\"registrationNumber\":\"111\",\"profile\":\"<p>11111</p>\\n\",\"createTime\":1600421470000,\"updateTime\":1600421470000,\"updateUserId\":\"ec8d71ce8c8b48a6bc142e8253dfaa35\",\"updateUserName\":\"测试\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"80a19f332f9946cbbfc56f8a1df3828d\",\"type\":\"1\",\"name\":\"Anlan Capital\",\"registrationNumber\":\"\",\"profile\":\"<p>&nbsp; &nbsp; 安澜资本（Anlan Capital）致力于打造全国领先的多元化海外资产管理公司。公司的基金产品包含私募股权基金、房地产基金、对冲基金、固收产品等多个投资领域，公司的股东和投资团队具有强劲的网罗优质资产的能力，合作伙伴和投资标的均聚焦于行业中第一梯队企业，致力于为投资者带来大量独家投资机会。<br />\\n&nbsp; &nbsp;&nbsp; 公司已成为国内外投资者最值得信赖的长期合作伙伴，受托管理的资金主要来自于超过1.2万个家族办公室、三方理财机构及40万个超级理财师。<br />\\n&nbsp;</p>\\n\",\"createTime\":1599720952000,\"updateTime\":1599720952000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"376aea8344324df89dc7492c8ceef406\",\"type\":\"3\",\"name\":\"深圳文化产权交易所\",\"registrationNumber\":\"\",\"profile\":\"<p>深圳文化产权交易所有限公司（以下简称深圳文交所）于2009年发起，深圳市委市政府于2009年11月4日召开会议，决定组建深圳文化产权交易所（《市政府常务会议纪要》 深圳市人民政府办公厅二零零九年十一月十九日），2010年4月，深圳文交所正式注册并开始运营，2010年4月正式注册成立。以&ldquo;文化对接资本、交易创造价值&rdquo;为经营理念，是一个面向全国及全球的文化产权交易平台、文化产业投融资平台、文化企业孵化平台与文化产权登记托管平台。</p>\\n\",\"createTime\":1598856129000,\"updateTime\":1598856129000,\"updateUserId\":\"97315041f3374c549d814ad17d10ae28\",\"updateUserName\":\"何静\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"92cacd94d930426986146937cef1c4e2\",\"type\":\"2\",\"name\":\"中国民生信托有限公司\",\"registrationNumber\":\"\",\"profile\":\"<p>&nbsp; &nbsp; &nbsp; 中国民生信托有限公司的前身&mdash;&mdash;中国旅游国际信托投资有限公司成立于1994年10月。2012年12月，中国银保监会批复公司重组及股权变更方案。2013年4月16日完成重新登记，名称变更为中国民生信托有限公司。2013年4月28日，公司正式复牌开业。注册资本为人民币70亿元，控股股东为武汉中央商务区股份有限公司。<br />\\n&nbsp; &nbsp; &nbsp; 公司属于混合所有制企业，在经营决策、风险管控、人才建设、激励约束、文化导向等多个方面，都形成了灵活、高效、实用的市场化机制，混合所有制优势得以充分展现。<br />\\n&nbsp; &nbsp; &nbsp; 自2013年4月复业以来，公司始终保持稳健高速发展，主动管理能力不断提高，在财富、投资、投行、资管、融资等五大领域深入布局，主动管理资产规模占比接近80%，位居行业前列。六年来，公司累计分配信托收益超过400亿元，在刷新行业发展速度的同时，为高净值人群和机构投资者创造了丰厚的投资回报。<br />\\n&nbsp;</p>\\n\",\"createTime\":1595399030000,\"updateTime\":1595399030000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"bf74ae6e0b7d46339dd12abcba975c28\",\"type\":\"1\",\"name\":\"Starlight Asset Management Limited\",\"registrationNumber\":\"\",\"profile\":\"<p>Starlight Asset致力于为境内企业或超高净值个人提供资产跨境证券化，及变现解决方案；</p>\\n\\n<p>Starlight&nbsp;Asset致力于为资产合作方提供资产管理、全面的资本支持及运作服务；</p>\\n\\n<p>Starlight&nbsp;Asset通过专业的经验为投资者提供全球资产配置及税务筹 划、移民等解决方案。</p>\\n\",\"createTime\":1591069350000,\"updateTime\":1591076079000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":1},{\"id\":\"9c520c0b66db4e419787d37810cbbb62\",\"type\":\"1\",\"name\":\"深圳阳宏资产管理有限公司\",\"registrationNumber\":\"P1020656\",\"profile\":\"<p><em><em>深圳阳宏资产管理有限公司</em></em><span style=\\\"background-color:#ffffff; color:#666666; font-family:&quot;Open Sans&quot;,Arial,&quot;Microsoft YaHei&quot;,&quot;微软雅黑&quot;,&quot;STHeiti&quot;,&quot;WenQuanYi Micro Hei&quot;,SimSun,sans-serif; font-size:15px\\\">成立于2015年5月，注册资本1000万元人民币。</span></p>\\n\",\"createTime\":1587093314000,\"updateTime\":1587093314000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"73f616e928e543d1affbc83426308a94\",\"type\":\"1\",\"name\":\"Vfund Investment Limited\",\"registrationNumber\":\"\",\"profile\":\"<p><span style=\\\"color:#333333; font-family:微软雅黑,&quot;Microsoft YaHei&quot;; font-size:18px\\\">云晖资本（V Fund）是一家由五位前高盛集团的资深银行家于2015年创立的私募股权投资管理公司。截至目前，云晖资本管理基金规模70亿人民币，已投资规模达到40亿元</span><span style=\\\"color:#333333; font-family:微软雅黑,&quot;Microsoft YaHei&quot;; font-size:18px\\\">人民币</span><span style=\\\"color:#333333; font-family:微软雅黑,&quot;Microsoft YaHei&quot;; font-size:18px\\\">，主要投资方向以硬科技为主，其中包括汽车新四化领域、人工智能、智能制造、航空航天等。我们投资了多个具有市场影响力的项目，主要包括：宁德时代、孚能科技、容百锂电、地平线机器人、四维图新、竹间智能、西恩科技、韵达快递、康鹏化学、紫建电子、中科宇航等。&nbsp;</span></p>\\n\",\"createTime\":1587093114000,\"updateTime\":1587093114000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"0ff8c49eb0b440eaad40751424df09d4\",\"type\":\"1\",\"name\":\"Atlas Capital阿特列斯资本\",\"registrationNumber\":\"\",\"profile\":\"<p>&bull; 成立于2017年4月，聚焦中国市场，投资于新娱乐、新消费、新科技领域的优质公司。</p>\\n\\n<p>&bull; 依托于团队对于行业基本面及发展演进逻辑的深入研究，以及对国内资本市场运作规则</p>\\n\\n<p>及退出机制的独到理解，力求在专业能力及实操经验方面打造核心竞争力。</p>\\n\\n<p>&bull; 致力于打造Professional Investor&rsquo;s Fund（专业投资者基金），基石LP均为国内过去十</p>\\n\\n<p>年一二级市场成功的投资人之一。</p>\\n\",\"createTime\":1577671324000,\"updateTime\":1577671324000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"427722b8af93487184eb139f6188fe64\",\"type\":\"3\",\"name\":\"菏泽市国泰产权交易中心\",\"registrationNumber\":\"\",\"profile\":\"<p>&nbsp; &nbsp; &nbsp; <span style=\\\"color:#000000; font-family:微软雅黑; font-size:16px\\\">菏泽市国泰产权交易中心有限公司是由菏泽市人民政府国有资产监督管理委员会批准，菏泽市铁路发展集团有限公司及菏泽市城市开发投资有限公司投资成立的一家从事国有资产产权交易的机构，公司注册资本5000万元，公司类型：国有独资。</span></p>\\n\\n<p><span style=\\\"color:#000000; font-family:微软雅黑; font-size:16px\\\">&nbsp; &nbsp;&nbsp; 菏泽市国泰产权交易中心有限公司秉承&ldquo;严谨、规范、高效、创新&rdquo;的理念，强化服务意识，规范交易行为，联合拍卖、招投标、评估、审计、律师等中介机构，共同搭建&ldquo;一站式&rdquo;产权交易服务平台。积极为各类企事业单位的产权交易，增资扩股，资产转让以及实物资产交易，投融资等进行合理的优化配置服务。</span></p>\\n\",\"createTime\":1574246437000,\"updateTime\":1574246437000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"f3916c42a75b4029a529a1a0c6d2f63f\",\"type\":\"1\",\"name\":\"北京睿中资本管理有限公司\",\"registrationNumber\":\"P1005058\",\"profile\":\"<p><span style=\\\"background-color:transparent; color:#333333; font-family:&quot;微软雅黑&quot;,&quot;宋体&quot;,tahoma,arial,sans-serif; font-size:16px\\\">睿中资本是由中国资深金融专家创办，总部设立于北京，主营业务为房地产基金、基础设施基金及财务顾问三项业务，旨在打造&ldquo;中国最富想象力的资本服务机构&rdquo;，团队成员具有近20只基金的发起、募集、设立、投资及投后管理资历。 </span></p>\\n\",\"createTime\":1572330482000,\"updateTime\":1572330482000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"c0ab6db657094208aff21bf9ecee0fb4\",\"type\":\"1\",\"name\":\"深圳长城汇理资产管理有限公司\",\"registrationNumber\":\"P1000630\",\"profile\":\"<p>深圳长城汇理资产管理有限公司是国内最为知名的并购基金管理机构及投资机构之一&mdash;&mdash;长城汇理旗下的私募基金管理人。长城汇理一直以&ldquo;产业整合、价值提升&rdquo;为核心理念，管理团队于2008年创设了中国最早的上市公司并购基金，拥有大量知名投资案例，投资的&ldquo;天目药业&rdquo;、&rdquo;亚星化学&rdquo;等项目被选为国内知名商学院经典案例。长城汇理公司（08315.HK）在香港联交所上市，未来将是长城汇理整体上市的国际化平台。</p>\\n\\n<p>截至2019年3月31日，长城汇理管理团队总管理基金规模逾三十亿人民币，已累计发行基金产品30只左右，先后对多家上市公司进行投资和重组操作，过去6年已退出项目平均年化收益率超过40%。</p>\\n\",\"createTime\":1569227620000,\"updateTime\":1570519530000,\"updateUserId\":\"87a292b5e51a4a5ab67d7c684876692f\",\"updateUserName\":\"冯晓辉\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":1},{\"id\":\"48a25073a3a74d7b90578be71712129c\",\"type\":\"1\",\"name\":\"上海久期量和投资有限公司\",\"registrationNumber\":\"P1031301\",\"profile\":\"<p>久期投资成立于2013年12月，是一家以固定收益和宏观对冲为主的多策略资产管理机构，为基金业协会正式会员<br />\\n⚫ 久期投资秉承&ldquo;守正、顺势、均衡&rdquo;的投资理念，通过债券、股票和黑色大宗商品市场的大类资产配置和价值发掘，为投资者<br />\\n创造长期可持续的稳健回报<br />\\n⚫ 公司产品线分为固定收益类、股票量化类、宏观对冲类<br />\\n固定收益类，主要投资于债券市场，追求较低风险下的稳健收益<br />\\n股票量化类，阿尔法策略提供稳定超额收益，专注于指数增强和市场中性两类产品<br />\\n宏观对冲类，通过债券、股票和大宗商品的资产配置，实现风险可控前提下的长期较高收益<br />\\n&nbsp;</p>\\n\",\"createTime\":1568883878000,\"updateTime\":1568884662000,\"updateUserId\":\"87a292b5e51a4a5ab67d7c684876692f\",\"updateUserName\":\"冯晓辉\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":2},{\"id\":\"09cf632b7a534811b5d0b717d07f39fe\",\"type\":\"3\",\"name\":\"贵州场外机构间市场有限公司\",\"registrationNumber\":\"黔府金函【2015】132号\",\"profile\":\"<p><span style=\\\"font-size:14px\\\">贵州场外机构间市场有限公司由贵州省政府批准（黔府金函【2015】132号）设立，是国内专门从事场外衍生品交易的创新型金融交易平台。公司的宗旨是，以互联网为交易手段，搭建综合管理服务平台，面向实体企业和金融机构提供服务，通过衍生产品交易管理其经营风险。 公司的业务范围包括：大宗商品类、权益类、金融资产类场外衍生品交易结算、登记、托管、研究开发、咨询培训等服务，互联网金融业务，金融产品创新和交易，衍生品应用软件开发和电子商务。</span></p>\\n\\n<p><span style=\\\"font-size:14px\\\">公司定位：</span></p>\\n\\n<p><span style=\\\"font-size:14px\\\">1. 场内互补市场：<em>做标准化场内市场的补充，满足投资者个性化需求，定位为&ldquo;场内互补市场&rdquo;。</em></span></p>\\n\\n<p><span style=\\\"font-size:14px\\\">2. 批发商市场:<em>为机构提供大额、大宗、集中交易，实质是&quot;批发商市场&quot;。</em></span></p>\\n\\n<p><span style=\\\"font-size:14px\\\">3. 市场中的市场:<em>为国内众多大宗商品现货市场和金融资产交易市场提供后端的衍生品交易服务，形成&quot;市场中的市场&quot;。</em></span></p>\\n\\n<p><span style=\\\"font-size:14px\\\">组合投资市场:<em>将五大类不同市场的众多产品归为一体，方便投资者实现组合投资，打破了交易不同类型产品要到不同交易所的传统做法，实现&quot;组合投资市场&quot;。</em></span></p>\\n\\n<p><span style=\\\"font-size:14px\\\">4. 统一风控市场:<em>投资者可将登记托管在公司的基础现货资产作为抵押物，从事衍生品交易，保证金可以组合计算和使用，由公司综合评估控制风险，形成了&quot;统一风控市场&quot;。</em></span></p>\\n\",\"createTime\":1567577954000,\"updateTime\":1567577954000,\"updateUserId\":\"97315041f3374c549d814ad17d10ae28\",\"updateUserName\":\"何静\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"c14be040ce5244f4860ab1937b7bdd32\",\"type\":\"3\",\"name\":\"西部金融资产交易中心（贵州）有限公司\",\"registrationNumber\":\"\",\"profile\":\"<p><span style=\\\"font-size:16px\\\">西部金融资产交易中心<span style=\\\"background-color:transparent; color:#333333; font-family:arial\\\">是一家提供投融资、互联网金融、金融资产交易、债权股权转让服务生态圈。</span></span></p>\\n\",\"createTime\":1567412525000,\"updateTime\":1567412525000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"8a008916c67a41c6b0a547174eedcb5b\",\"type\":\"1\",\"name\":\"HC GLOBAL\",\"registrationNumber\":\"\",\"profile\":\"<p>HC Global是一家位于 美国旧金山的基金管理 公司。</p>\\n\\n<p>该公司的主要管理人拥 有超过35年的金融投资 和基金管理经验。</p>\\n\\n<p>为全球100余只知名基 金提供基金管理服务</p>\\n\",\"createTime\":1565443937000,\"updateTime\":1565443937000,\"updateUserId\":\"e289c08749de4f65969e24ce103e2fbc\",\"updateUserName\":\"地里木拉提·帕尔哈提\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"ec068b594ce249c0aaadb59f2aca0dd9\",\"type\":\"1\",\"name\":\"RWHoldings\",\"registrationNumber\":\"\",\"profile\":\"<p>RW Holdings是由全球知名的商业地产公司世邦魏理仕CBRE的前董事长Ray Wirta 先生创立的房地产投 资公募基金。于2015年5月14日成立在美国马里兰州，Ray带领其团队先后发行了两款房地产投资基金 CAREITs（2012年发行） 和 NNNREITs（2015年发行）。 其核心理念是将传统的主要由机构投资者获得的高质量的单租户净租赁商业地产，通过分散投资的模式让 广大投资者享有。两款REITs均在美国证监会合规注册，按照当地监管要求，需将投资组合中的应税收入的 90%以现金分红的方式支付给投资者。<br />\\n旗下包括推广品牌RichUncles和主体公司RWHoldings。</p>\\n\\n<p>RWHoldings已收购并管理了约4亿美元资产，分别是约3亿美元NNNREITS以及1亿多美元的 CAREITS。这些包括了美国各地的 办公室、工业和零售地产，其选择的物业都是有六年及以上优质租约的商业物业，这些物业由单一租户承租，租户包括如EXP、温德姆 酒店集团、富士胶片集团，EMCOR，富世华、AvAir、3M等在美国具有投资等级的知名企业。 NNNREITS每月收购金额保持1000万美元的增长，预计到2019年4月NNNREITS累计管理资产约达到4亿美元,累计总资产管理规 模将超越5亿美元。<br />\\n&nbsp;</p>\\n\",\"createTime\":1565440842000,\"updateTime\":1565440903000,\"updateUserId\":\"e289c08749de4f65969e24ce103e2fbc\",\"updateUserName\":\"地里木拉提·帕尔哈提\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":1},{\"id\":\"9e937a3027ec4de689979d13fdfc6615\",\"type\":\"2\",\"name\":\"XX信托股份有限公司\",\"registrationNumber\":\"\",\"profile\":\"<p>长安国际信托股份有限公司前身为&ldquo;西安国际信托有限公司&rdquo;，1986年经中国人民银行批准成立。1999年12月公司增资改制为有限责任公司，更名为&ldquo;西安国际信托投资有限公司&rdquo;。2002年4月，经中国人民银行总行批准重新登记申请，公司获准单独保留。2008年2月经中国银行业监督管理委员会批准，公司换领新的金融许可证，同时更名为&ldquo;西安国际信托有限公司&rdquo;。2011年11月经中国银行业监督管理委员会批准，并经工商登记，公司整体变更为股份有限公司，同时更名为&ldquo;长安国际信托股份有限公司&rdquo;。目前，公司注册资本为人民币33.3亿元。</p>\\n\\n<p>公司主要从事<strong>资金信托业务、投资银行业务、融资租赁业务和其他金融业务</strong>。公司业务涉及货币市场、资本市场和金融衍生品市场等领域。公司全心打造高净值客户的最佳金融生活服务商，成立多年来，为国内外数百家企业和机构，数万余名自然人投资者提供了专业的信托金融服务。公司已建立了完善的法人治理机制，拥有健全的风险控制体系，有一支在金融、投资、资本运作等领域具有丰富经验和突出能力的专业团队，可以为委托人和投资客户提供全面的信托金融和理财服务。</p>\\n\",\"createTime\":1565337907000,\"updateTime\":1565337949000,\"updateUserId\":\"d87e05dc826d4ef9a0a145e9d2421955\",\"updateUserName\":\"张怡\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":1},{\"id\":\"759677d9b24f4b179e16e2a3f7d31298\",\"type\":\"3\",\"name\":\"安徽省金融资产交易所有限责任公司\",\"registrationNumber\":\"\",\"profile\":\"<p><span style=\\\"color:#2e2e2e; font-family:微软雅黑,Microsoft YaHei; font-size:100%\\\">安徽省金融资产交易所有限责任公司于2010年7月成立，2012年经清理整顿各类交易场所部际联席会议验收通过，于2015年4月完成增资扩股，现股东为安徽省产权交易中心有限责任公司、安粮期货有限公司、<span style=\\\"font-family:微软雅黑; font-size:18px\\\">安徽华时信息科技有限公司</span>。</span><span style=\\\"background-color:#ffffff; color:#2e2e2e; font-family:微软雅黑,Microsoft YaHei; font-size:18px\\\">&nbsp;</span></p>\\n\",\"createTime\":1565171326000,\"updateTime\":1565171326000,\"updateUserId\":\"02b3b5e71b8e4242823ce53b32e31554\",\"updateUserName\":\"张煜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"ff44d3efe8fc45e09cbebdb63be83b3e\",\"type\":\"1\",\"name\":\"北京乾丰同德投资有限公司\",\"registrationNumber\":\"P1064529\",\"profile\":\"<p>北京龙德文创投资基金管理有限公司由北京市文化科技融资担保有限公司牵头组建，成立于2014年4月，公司注册资本3000万元。龙德文创基金为多元化的股东结构，北京市文化科技融资担保有限公司作为国有第一大股东，占比33%，此外，5家在文化产业内资深的民营资本股东占比67%，龙德文创基金借助国有背景的同时进行市场化的运作，拥有专业化团队以及专业化的投资决策机制。</p>\\n\",\"createTime\":1564732138000,\"updateTime\":1564732138000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":0},{\"id\":\"b88a4bfeb73a477d90d0ea2c23c4566e\",\"type\":\"1\",\"name\":\"国投科创基金管理（北京）有限公司\",\"registrationNumber\":\"P1018036\",\"profile\":\"<p>国投科创基金管理（北京）有限公司，&nbsp;于 2015 年 7 月 16 日取得中国证券投资基金业协会备案，备案号：P1018036，管理基金主要类别为私募股权和创业投资基金管理人。 公司先后与上海浦东区、江苏省南京市、宿迁市、南通市、山东省枣庄市建立良好的合作关系，围绕上述地区正在发起成立产业引导基金及管理部分母基金的子基金。</p>\\n\",\"createTime\":1564713325000,\"updateTime\":1564715811000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":2},{\"id\":\"1af07dc578264fa7bec8534a0599fa11\",\"type\":\"1\",\"name\":\"珠海汉睿投资管理有限责任公司\",\"registrationNumber\":\"P1064523\",\"profile\":\"<p>汉能投资集团成立于2003年，是一家专注于中国互联网经济、消费及医疗服务领域的顶级投资银行。业务涉及财务顾问、私募股权投资、家族办公室等，合力为中国新经济领军企业提供综合金融服务。<br />\\n截至目前，汉能投资集团已累计帮助超过200家企业完成逾580亿美金的私募融资及并购交易；管理及共同管理的私募股权投资基金规模总计约100亿元人民币。<br />\\n&nbsp;</p>\\n\",\"createTime\":1564647544000,\"updateTime\":1564713512000,\"updateUserId\":\"dc5124d42e4144cf8bb6cc213e7c3f6b\",\"updateUserName\":\"张茜\",\"fundCount\":0,\"raisingAmountCNY\":0,\"raisingAmountUSD\":0,\"totalRaisedAmtCNY\":0,\"totalRaisedAmtUSD\":0,\"dataVersion\":2}]}}";
		JSONObject parseObject = JSONObject.parseObject(joson);
		JSONArray array = parseObject.getJSONObject("result").getJSONArray("rows");
		for (Object object : array) {
			JSONObject item = (JSONObject)object;
			if(item.getString("id").equals(id)) {
				ManagerUserEntity managerUserEntity = managerUserService.getOne(new QueryWrapper<ManagerUserEntity>().like("custodian_name", item.getString("name")));
				return managerUserEntity.getId();
			}
		}
		return "";
	}
	
	
	
	
	
	public static JSONObject  sendGet(String url) {
		HttpRequest createRequest = HttpUtil.createRequest(Method.GET,url);
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("token","8kdSlgc31B3IVBNVejKwYFrhEkPj3OPSFEyBkshK1ID+67TnbBSXhWMW25N2fZxPBcB9QnplU7PCovB3Jps6yl2vPruANRGVGv1rGc5fv7M3sZhQYQX9IdYKsgxrGfhrvVtMymYA9HgrUNiwE6HJ/5FWfR4daaKiN9EnYb2F59OJlRqeiXc7ajvYdRh6RdW8CJh9WOihl8p7k/NJZC0XEU8fwprd7nhxDfOlFVLLv4vI0qWeXdI54PO9Ku69l9G7LArBMkTKqkhrmU53bBUu9M7lYnRZKTj6cgzJxmV62Rs8CHC1FVTdMwdr/eAOYjj63dy4w20MHhhuNc0fSSRQS1FA9eTUJov6VffnkGQehonrD1n3UkTS9JiqJTmoSRjWdzFJ9y+C3feureS6bqBgjuljKti1uLUikKrun4kbb27S09RhLzghS8fXcocAJuL+OHIWlzCEy4va2F/KMs3bvg==");
		createRequest.addHeaders(hashMap);
		HttpResponse execute = createRequest.execute();
		JSONObject jsonObject = JSONObject.parseObject(execute.body());
		return jsonObject;	
	}
	
}
