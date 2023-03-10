package io.live_mall.modules.server.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.youzan.cloud.open.sdk.common.exception.SDKException;
import com.youzan.cloud.open.sdk.gen.v3_1_0.model.YouzanCrmCustomerPointsIncreaseResult;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.*;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.DuiFuNoticeModel;
import io.live_mall.modules.server.model.OrderModel;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.*;
import io.live_mall.modules.server.utils.ValidateUtils;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import io.live_mall.tripartite.TouchClients;
import io.live_mall.tripartite.YouZanClients;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

	@Autowired
	ProductService productService;

	@Autowired
	RaiseService raiseService;

	@Autowired
	SysUserService sysUserService;

	@Autowired
	ProductUnitService productUnitService;

	@Autowired
	private SysOrgGroupService sysOrgGroupService;

	@Autowired
	SmsService smsService;
	@Autowired
	@Lazy
	private MemberService memberService;
	@Autowired
	OrderPayService orderPaySerivce;
	@Autowired
	private HistoryDuiFuDao historyDuiFuDao;
	@Autowired
	private HistoryDuifuPayDao historyDuifuPayDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private CustomerUserDao customerUserDao;
	@Autowired
	private IntegralDao integralDao;
	@Autowired
	private CustomerUserIntegralItemDao customerUserIntegralItemDao;
	@Autowired
	private YouZanUserDao youZanUserDao;
	@Autowired
	private OrderPayDao orderPayDao;
	@Autowired
	private TouchUserDao touchUserDao;
	@Autowired
	private IntegralActivityDao integralActivityDao;
	@Autowired
	private OrderRedeemDao orderRedeemDao;
	@Autowired
	private OrderPurchaseDao orderPurchaseDao;
	@Autowired
	private OrderBonusDao orderBonusDao;
	@Autowired
	private OrderOutDao orderOutDao;

	@Override
	public PageUtils selectDuifuPage(Map<String, Object> params) {
		IPage<JSONObject> page = this.baseMapper.selectDuifuPage(new Query<JSONObject>().getPage(params), params);
		page.getRecords().stream().forEach(order -> {
			List<OrderPayEntity> orderPayList = orderPaySerivce.list(new QueryWrapper<OrderPayEntity>().eq("order_id", order.getString("order_id")));
			List<OrderPayEntity> orderPayList2 = orderPaySerivce.list(new QueryWrapper<OrderPayEntity>().eq("order_id", order.getString("order_id")));
			if (!orderPayList2.isEmpty()) {
				orderPayList2.sort((OrderPayEntity m1, OrderPayEntity m2) -> DateUtil.parseDate(m2.getPayDate()).compareTo(DateUtil.parseDate(m1.getPayDate())));
				order.put("endSumMoney", order.getDouble("appoint_money")+orderPayList2.get(0).getPayMoney().doubleValue());
			}
			order.put("orderPayList",orderPayList);
			if(order.getString("date_num") !=null ) {
				order.put("end_date",DateUtil.formatDate(DateUtil.offsetMonth(order.getDate("establish_time"), order.getIntValue("date_num"))));
			}
		});
		return new PageUtils(page);
	}
	
	@Override
	public PageUtils selectYjPage(Map<String, Object> params) {
		// TODO Auto-generated method stub
		IPage<JSONObject> page = this.baseMapper.selectYjPage(new Query<JSONObject>().getPage(params), params);
		return new PageUtils(page);
	}
	
	
	@Override
	public PageUtils selectYongjin(Map<String, Object> params) {
		// TODO Auto-generated method stub
		IPage<JSONObject> page= this.baseMapper.selectYongjin(new Query<JSONObject>().getPage(params), params);
		return new PageUtils(page);
	}

	@Override
	public JSONObject selectYongjinSum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectYongjinSum(params);
	}
	
	
	@Override
	public JSONObject selectYjSum(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectYjSum(params);
	}
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<OrderEntity> page = this.baseMapper.selectPageVo(new Query<OrderEntity>().getPage(params), params);
		page.getRecords().stream().forEach(order -> {
			order.setProductUnit(productUnitService.getById(order.getProductUnitId()));
			order.setProduct(productService.getOne(new QueryWrapper<ProductEntity>().eq("id", order.getProductId())));
			order.setRaise(raiseService.getOne(new QueryWrapper<RaiseEntity>().eq("id", order.getRaiseId())));
			order.setSaleUser(sysUserService.getById(order.getSaleId()));
			order.setAduitUser(sysUserService.getById(order.getAduitId()));
		});
		return new PageUtils(page);
	}

	@Override
	public OrderModel getOneOrderModel(String orderId) {
		return baseMapper.getOneOrderModel(orderId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateOrder(OrderEntity order, SysUserEntity user, String touchToken) {
		RaiseEntity raiseEntity = raiseService.getById(order.getRaiseId());
		OrderEntity orderEntity = this.getById(order.getId());
		orderEntity.setStatus(order.getStatus());
		order.setUptBy(user.getRealname());
		order.setUptDate(new Date());
		
		if ("pass".equals(order.getUptType())) {
			order.setAduitId(user.getUserId());
			order.setAduitTime(new Date());
			//??????????????????
			MemberEntity memberEntity = memberService.saveOrUpdate(orderEntity);
			if (memberEntity != null) {
				order.setCustId(memberEntity.getMemberNo());
			}
			// ???????????????????????????
			CompletableFuture.supplyAsync(() -> this.touchUserInfo(touchToken, memberEntity.getPhone(), memberEntity.getMemberNo()));
			smsService.sendMsgToCust(orderEntity);
			MemberEntity one = memberService.getOne(new QueryWrapper<MemberEntity>().eq("card_num", order.getCardNum()));
			if( one !=null && !String.valueOf(order.getSaleId()).equals( one.getSaleId())) {
				throw new RRException("?????????,???????????????????????????,????????????????????????");
			}
			ProductEntity productEntity = productDao.selectById(orderEntity.getProductId());
			Integer ys = 12;
			String productTerm = productEntity.getProductTerm();
			Integer productTermType = productEntity.getProductTermType();
			if (productTermType != 4 || productTermType == 3) {
				ys = Integer.valueOf(productTerm.split(",")[0]);
			} else if (productTermType == 3) {
				ys = Integer.valueOf(productTerm.split(",")[1]);
			}
			order.setDateNum(ys);
			order.setDateUint("???");
		}
		if ("tuikuan".equals(order.getUptType())) {
			smsService.sendMsgToCust(orderEntity);
		}
		if ("auth".equals(order.getUptType())) {
			if( StringUtils.isNotBlank(order.getPhone()) ) {
				if(!ValidateUtils.isMobile(order.getPhone())) {
					throw new RRException("????????????????????????");
				}
				
				/*??????????????????*/
				/**
				 * 1???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				 * 2?????????????????????????????????????????????????????????????????????????????????????????????
				 */
				if(!user.getRealname().equals(order.getCustomerName())) {
					if(StringUtils.isNotBlank(user.getMobile()) && order.getPhone().equals(user.getMobile())) {
						throw new RRException("?????????????????????????????????????????????????????????");
					}
					int count = memberService.count(new QueryWrapper<MemberEntity>().eq("phone", order.getPhone()).ne("sale_id", order.getSaleId()));
					if(count>0) {
						throw new RRException("????????????????????????,????????????");
					}
				}
			}else {
					throw new RRException("??????????????????");
			}
			order.setStatus(0);
			order.setAduitTime(new Date());
			//??????????????????????????????????????????
			MemberEntity one = memberService.getOne(new QueryWrapper<MemberEntity>().eq("card_num", order.getCardNum()));
			if( one !=null && !String.valueOf(order.getSaleId()).equals( one.getSaleId())) {
				throw new RRException("?????????,???????????????????????????,????????????????????????");
			}
		}

		if ("bohui".equals(order.getUptType())) {
			order.setAduitId(user.getUserId());
			order.setAduitTime(new Date());
			smsService.sendMsgToCust(orderEntity);
		}
		
		if (StringUtils.isNotBlank(order.getUptType())) {
			order.setOrderStup(orderEntity.getOrderStup());
			OrderUtils.orderStup(raiseEntity, order, order.getStatus(), order.getAduitResult());
			//????????????????????????????????????
			raiseService.down(order.getRaiseId());	
		}
		this.updateById(order);
		return 0;
	}

	/**
	 *
	 * @param phone
	 * @param memberNo
	 * @return
	 * @throws IOException
	 */
	@SneakyThrows
	public boolean touchUserInfo(String token, String phone, String memberNo) {
		JSONObject userList = TouchClients.userList(token, phone);
		if (Objects.nonNull(userList)) {
			JSONObject data = userList.getJSONObject("data");
			JSONArray list = data.getJSONArray("list");
			if(!list.isEmpty()) {
				JSONObject userJson = list.getJSONObject(0);
				String userId = userJson.getString("user_id");
				JSONObject userInfo = TouchClients.userInfo(token, userId);
				Integer code = userInfo.getInteger("code");
				if (0 == code) {
					JSONObject userData = userInfo.getJSONObject("data");
					TouchUserEntity entity = new TouchUserEntity();
					entity.setUserId(userId);
					entity.setMemberNo(memberNo);
					entity.setNickname(userData.getString("nickname"));
					entity.setName(userData.getString("name"));
					entity.setAvatar(userData.getString("avatar"));
					entity.setGender(userData.getInteger("gender"));
					entity.setPhone(userData.getString("phone"));
					entity.setIsSeal(userData.getInteger("is_seal"));
					entity.setPhoneCollect(userData.getString("phone_collect"));
					entity.setSdkUserId(userData.getString("sdk_user_id"));
					entity.setCreatedAt(userData.getString("created_at"));
					entity.setAppId(userData.getString("app_id"));
					entity.setAreaCode(userData.getString("area_code"));
					entity.setPhoneResource(userData.getString("phone_resource"));
					entity.setCreateTime(new Date());
					touchUserDao.insert(entity);
				}
			}
		}
		return true;
	}

	@Override
	public List<OrderEntity> list(Wrapper<OrderEntity> queryWrapper) {
		// TODO Auto-generated method stub
		List<OrderEntity> list = super.list(queryWrapper);
		list.stream().forEach(order -> {
			/* order.setProductUnit(productUnitService.getById(order.getProductUnitId()));*/
			order.setProduct(productService.getOne(new QueryWrapper<ProductEntity>().eq("id", order.getProductId())));
			/*order.setRaise(raiseService.getOne(new QueryWrapper<RaiseEntity>().eq("id",order.getRaiseId() )));
			order.setSaleUser(sysUserService.getById(order.getSaleId()));
			order.setAduitUser(sysUserService.getById(order.getAduitId()));*/
		});
		return list;
	}

	@Override
	public OrderEntity getById(Serializable id) {
		// TODO Auto-generated method stub
		OrderEntity order = super.getById(id);
		if (order == null) {
			return null;
		}
		order.setProductUnit(productUnitService.getById(order.getProductUnitId()));
		order.setProduct(productService.getOne(new QueryWrapper<ProductEntity>().eq("id", order.getProductId())));
		order.setRaise(raiseService.getOne(new QueryWrapper<RaiseEntity>().eq("id", order.getRaiseId())));
		order.setSaleUser(sysUserService.getById(order.getSaleId()));
		order.setAduitUser(sysUserService.getById(order.getAduitId()));
		order.setSysOrgGroup(
				sysOrgGroupService.getOne(new QueryWrapper<SysOrgGroupEntity>().eq("id", order.getOrgId())));
		return order;
	}

	/**
	 * ???????????????
	 */
	@Override
	public Integer sumMoenyRaise(String id) {
		// TODO Auto-generated method stub
		List<OrderEntity> selectList = this.baseMapper.selectList(
				new QueryWrapper<OrderEntity>().in("status", OrderUtils.YX_ORDER_STATUS).eq("raise_id", id));
		int totalMoney = 0;
		for (OrderEntity orderEntity : selectList) {
			totalMoney += orderEntity.getAppointMoney();
		}
		return totalMoney;
	}

	/**
	 * 8????????????????????????????????????????????????0??????????????????????????????????????????????????????
	 * 
	 */

	@Override
	public Boolean getCouldFz(RaiseEntity raise) {
		// TODO Auto-generated method stub
		List<OrderEntity> selectList = this.baseMapper
				.selectList(new QueryWrapper<OrderEntity>().in("status", 2).eq("raise_id", raise.getId()));
		int totalMoney = 0;
		for (OrderEntity orderEntity : selectList) {
			totalMoney += orderEntity.getAppointMoney();
		}
		if (totalMoney >= raise.getRaiseMoney()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Integer sumMoenySaleId(Long userId) {
		// TODO Auto-generated method stub
		List<OrderEntity> selectList = this.baseMapper
				.selectList(new QueryWrapper<OrderEntity>().in("status", 2, 4).eq("sale_id", userId));
		int totalMoney = 0;
		for (OrderEntity orderEntity : selectList) {
			totalMoney += orderEntity.getAppointMoney();
		}
		return totalMoney;
	}

	@Override
	public List<JSONObject> getOrderSuccess(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.baseMapper.getOrderSuccess(params);
	}

	@Override
	public void success(RaiseEntity raise) {
		// TODO Auto-generated method stub
		ProductEntity productEntity = productService.getById(raise.getProductId());
		if(productEntity!=null && raise.getProductEndDate() !=null) {
			productEntity.setProductEndDate( DateUtil.parse(raise.getProductEndDate()));
			productEntity.setDayNum(DateUtil.betweenDay(raise.getEstablishTime(), DateUtil.parse(raise.getProductEndDate()), false));
			productService.updateById(productEntity);
		}
		List<OrderEntity> list = this.list(new QueryWrapper<OrderEntity>().eq("raise_id", raise.getId()).eq("status", 2));
		
		for (OrderEntity orderEntity : list) {
			orderEntity.setStatus(4);
			OrderUtils.orderStup(raise, orderEntity,4, "");
			smsService.sendMsgToCust(orderEntity);
		}
		this.updateBatchById(list);
	}
	


	@Override
	public Integer sumMoenyMember(String memberNo) {
		// TODO Auto-generated method stub
		List<OrderEntity> selectList = this.baseMapper.selectList(
				new QueryWrapper<OrderEntity>().in("status", OrderUtils.YX_ORDER_STATUS).eq("cust_id", memberNo));
		int totalMoney = 0;
		for (OrderEntity orderEntity : selectList) {
			totalMoney += orderEntity.getAppointMoney();
		}
		return totalMoney;
	}

	@Override
	public List<JSONObject>  selectAllOrderByUserAndRaise(String raise) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectAllOrderByUserAndRaise(raise);
	}

	@Override
	public BigDecimal getSumMoneyByProductId(String porduct) {
		// TODO Auto-generated method stub
		return this.baseMapper.getSumMoneyByProductId(porduct);
	}

	@Override
	public List<OrderEntity> selectList(QueryWrapper<OrderEntity> orderByAsc) {
		// TODO Auto-generated method stub
		return  this.baseMapper.selectList(orderByAsc);
	}


	//	add by lyg for sum appoint-money and count customer at 20220516
	@Override
	public JSONObject selectSumOrder(Map<String, Object> params) {
		// TODO Auto-generated method stub
		JSONObject jsonObject= this.baseMapper.selectSumOrder(params);
		return  jsonObject;
	}

	@Override
	public PageUtils customerDuifuPage(Map<String, Object> params, String cardNum) {
		Integer isHistory = Integer.valueOf(String.valueOf(params.get("isHistory")));
		isHistory = Objects.isNull(isHistory) ? 1 : isHistory;
		IPage<JSONObject> pages = this.baseMapper.customerDuifuPage(new Query<JSONObject>().getPage(params), cardNum, isHistory);
		pages.setRecords(assembleOrderItem(pages.getRecords()));

		return new PageUtils(pages);
	}

	private List<JSONObject> assembleOrderItem(List<JSONObject> records) {
		records.forEach(order -> {
			String productClass = "";
			OrderEntity orderEntity = this.baseMapper.selectById(order.getString("order_id"));
			if (Objects.nonNull(orderEntity)) {
				ProductEntity product = productService.getById(orderEntity.getProductId());
				productClass = Objects.nonNull(product) ? product.getProductClass() : "";
			}
			order.put("product_class", productClass);
			order.put("appoint_money", order.getInteger("appoint_money"));
			// ?????????????????? 0-??? 1-???
			Integer isAsh = 0;
			List<Map<String, Object>> maps = new ArrayList<>();
			 Integer dateNum = order.getInteger("date_num");
			 String end_date = "";
			 if (dateNum == 0) {
			 	end_date = order.getString("appoint_time");
			 }else {
			 	Date establishTime = order.getDate("establish_time");
			 	if (Objects.nonNull(establishTime) && Objects.nonNull(dateNum)) {
			 		end_date = DateUtil.formatDate(DateUtil.offsetMonth(establishTime, dateNum));
			 	}
			 }
			 order.put("end_date", end_date);
			String now = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
			List<OrderPayEntity> orderPayList = orderPaySerivce.list(Wrappers.lambdaQuery(OrderPayEntity.class)
					.eq(OrderPayEntity::getOrderId, order.getString("order_id")).orderByAsc(OrderPayEntity::getPayDate));
			if (!orderPayList.isEmpty()) {
				orderPayList.forEach(pay -> {
					Map<String, Object> map = Maps.newHashMap();
					String payType = pay.getPayDate().compareTo(now) < 0 ? "??????" : "";
					map.put("payDate", pay.getPayDate());
					map.put("payMoney", pay.getPayMoney() + "???");
					map.put("payType", payType);
					maps.add(map);
				});
				OrderPayEntity payEntity = orderPayList.get(orderPayList.size() - 1);
				isAsh = payEntity.getPayDate().compareTo(now) < 0 ? 1 : 0;
			}else {
				List<HistoryDuifuPayEntity> payEntityList = historyDuifuPayDao.selectList(Wrappers.lambdaQuery(HistoryDuifuPayEntity.class)
						.eq(HistoryDuifuPayEntity::getHistoryDuifuId, order.getString("order_id")).orderByAsc(HistoryDuifuPayEntity::getPayDate));
				if (!payEntityList.isEmpty()) {
					payEntityList.forEach(pay -> {
						Map<String, Object> map = Maps.newHashMap();
						String payType = pay.getPayDate().compareTo(now) < 0 ? "??????" : "";
						map.put("payDate", pay.getPayDate());
						map.put("payMoney", pay.getPayMoney() + "???");
						map.put("payType", payType);
						maps.add(map);
					});
					HistoryDuifuPayEntity historyDuifuPay = payEntityList.get(payEntityList.size() - 1);
					isAsh = historyDuifuPay.getPayDate().compareTo(now) < 0 ? 1 : 0;
				}
			}
			order.put("orderPayList",maps);
			order.put("isAsh", isAsh);
		});
		return records;
	}

	@Override
	public Map<String, Object> 	totalAssets(String cardNum) {
		Map<String, Object> result = Maps.newHashMap();
		// ????????????=???????????????????????????????????????+???????????????????????????????????????=????????????-????????????+ ????????????????????????????????????????????????
		// ???????????????????????????????????????
		Double expectedIncome = this.baseMapper.expectedIncome(cardNum);
		Double historyExpectedIncome = this.baseMapper.historyExpectedIncome(cardNum);
		// ??????
		double sumMoney = orderBonusDao.sumMoney(cardNum);
		// ????????????
		double sumIncome = orderRedeemDao.sumIncome(cardNum);
		// ????????????=????????????-?????????
		double outSumMoney = orderOutDao.sumMoney(cardNum);

		// ??????= ??????????????????????????????
		Double interest = this.baseMapper.interest(cardNum);
		Double historyInterest = this.baseMapper.historyInterest(cardNum);
		// Double totalInterest = this.baseMapper.totalInterest(cardNum);
		// ?????????(??????)
		Integer fixedIncome = this.baseMapper.fixedIncome(cardNum, 1);
		Integer historyFixedIncome = this.baseMapper.historyFixedIncome(cardNum, 1);

		// ?????????(??????)
		Integer stock = this.baseMapper.fixedIncome(cardNum, 2);
		Integer historyStock = this.baseMapper.historyFixedIncome(cardNum, 2);
		// ?????????(????????????)
		Integer netWorth = this.baseMapper.fixedIncome(cardNum, 3);
		Integer historyNetWorth = this.baseMapper.historyFixedIncome(cardNum, 3);

		result.put("totalAssets", fixedIncome + stock + netWorth + historyFixedIncome + historyStock + historyNetWorth);
		result.put("fixedIncome", fixedIncome + historyFixedIncome);
		result.put("stock", stock + historyStock);
		result.put("netWorth", netWorth + historyNetWorth);
		result.put("totalInterest", expectedIncome + historyExpectedIncome + interest + historyInterest + sumMoney + sumIncome + outSumMoney);
		return result;
	}



	@Override
	public Map<String, Object> customerAssets(String cardNum) {
		// ????????????=???????????????????????????????????????+???????????????????????????????????????=????????????-????????????+ ????????????????????????????????????????????????
		Map<String, Object> result = Maps.newHashMap();
		// ???????????? = ??????+?????????????????????
		Integer totalAssets = this.baseMapper.customerTotalAssets(cardNum);
		// ?????????????????????
		Integer assets = this.baseMapper.historyFixedIncome(cardNum, 0);

		// ????????????
		// Double annualIncome = this.baseMapper.annualIncome(cardNum);
		// ???????????????????????????
		// Double historyAnnualIncome = this.baseMapper.historyAnnualIncome(cardNum);

		// ????????????
		Double expectedIncome = this.baseMapper.expectedIncome(cardNum);
		Double historyExpectedIncome = this.baseMapper.historyExpectedIncome(cardNum);
		// ??????= ??????????????????????????????
		Double interest = this.baseMapper.interest(cardNum);
		Double historyInterest = this.baseMapper.historyInterest(cardNum);

		// ??????
		double sumMoney = orderBonusDao.sumMoney(cardNum);
		// ????????????
		double sumIncome = orderRedeemDao.sumIncome(cardNum);
		// ????????????=????????????-?????????
		double outSumMoney = orderOutDao.sumMoney(cardNum);

		// ????????? = ??????????????????
		// ??????????????????
		Integer investing = this.baseMapper.investingOrderMoney(cardNum);
		// ????????????????????????
		Integer historyInvesting = this.baseMapper.historyInvestingOrderMoney(cardNum);

		// ?????????
		result.put("totalAssets", investing + historyInvesting);
		// ????????????
		result.put("annualIncome", totalAssets + assets);
		// ????????????
		result.put("expectedIncome", expectedIncome + historyExpectedIncome + interest + historyInterest + sumMoney + sumIncome + outSumMoney);
		// ??????????????????
		result.put("investing", investing + historyInvesting);
		return result;
	}

	// @Override
	// public JSONObject productInfo(String orderId) {
	// 	JSONObject productInfo = new JSONObject();
	// 	OrderEntity orderEntity = this.baseMapper.selectById(orderId);
	// 	if (Objects.isNull(orderEntity)) {
	// 		HistoryDuiFuEntity duiFuEntity = historyDuiFuDao.selectById(orderId);
	// 		productInfo = productDao.getProductInfo(duiFuEntity.getProductName());
	// 	}else {
	// 		ProductEntity productEntity = productDao.selectById(orderEntity.getProductId());
	// 		productInfo.put("financing_party", productEntity.getFinancingParty());
	// 		productInfo.put("investment_mode", productEntity.getInvestmentMode());
	// 		productInfo.put("investment_target", productEntity.getInvestmentTarget());
	// 		productInfo.put("product_term", productEntity.getProductTerm());
	// 		productInfo.put("risk_level", productEntity.getRiskLevel());
	// 		productInfo.put("product_label", productEntity.getProductLabel());
	// 		productInfo.put("product_highlights", productEntity.getProductHighlights());
	// 		productInfo.put("other_data", productEntity.getOtherData());
	// 	}
	//
	// 	return productInfo;
	// }

	@Override
	public Map<String, Object> customerOrderInfo(String id) {
		Map<String, Object> result = Maps.newHashMap();
		JSONObject orderInfo = this.baseMapper.customerOrderInfo(id);
		if (Objects.nonNull(orderInfo)) {
			String end_date = "";
			Integer dateNum = orderInfo.getInteger("date_num");
			Date establishTime = orderInfo.getDate("establish_time");
			if (Objects.nonNull(establishTime) && Objects.nonNull(dateNum)) {
				end_date = DateUtil.formatDate(DateUtil.offsetMonth(establishTime, dateNum));
			}
			orderInfo.put("end_date", end_date);
		}else {
			orderInfo = historyDuiFuDao.HistoryDuiFuInfo(id);
		}

		//????????????
		JSONObject productInfo = new JSONObject();
		OrderEntity orderEntity = this.baseMapper.selectById(id);
		if (Objects.isNull(orderEntity)) {
			HistoryDuiFuEntity duiFuEntity = historyDuiFuDao.selectById(id);
			productInfo = productDao.getProductInfo(duiFuEntity.getProductName());
		}else {
			ProductEntity productEntity = productDao.selectById(orderEntity.getProductId());
			productInfo.put("financing_party", productEntity.getFinancingParty());
			productInfo.put("investment_mode", productEntity.getInvestmentMode());
			productInfo.put("investment_target", productEntity.getInvestmentTarget());
			productInfo.put("product_term", productEntity.getProductTerm());
			productInfo.put("risk_level", productEntity.getRiskLevel());
			productInfo.put("product_label", productEntity.getProductLabel());
			productInfo.put("product_highlights", productEntity.getProductHighlights());
			productInfo.put("other_data", productEntity.getOtherData());
		}
		result.put("orderInfo", orderInfo);
		result.put("productInfo", productInfo);
		return result;
	}

	@Override
	public void batchIntegralOrder() {
		// ?????????????????????????????????
		List<CustomerUserEntity> userEntityList = customerUserDao.selectList(Wrappers.lambdaQuery(CustomerUserEntity.class)
				.isNotNull(CustomerUserEntity::getCardNum).eq(CustomerUserEntity::getDelFlag, 0));
		if (!userEntityList.isEmpty()) {
			// ??????????????????????????????
			Map<String, CustomerUserEntity> userEntityMap = userEntityList.stream().collect(Collectors.toMap(CustomerUserEntity::getCardNum, entity -> entity));
			List<String> cardNums = new ArrayList<>();
			userEntityList.forEach(user -> cardNums.add(user.getCardNum()));
			// ??????
			List<JSONObject> orders = this.baseMapper.integralOrder(cardNums);
			// ????????????
			IntegralEntity integralEntity = integralDao.selectOne(Wrappers.lambdaQuery(IntegralEntity.class).orderByDesc(IntegralEntity::getCreateTime).last("LIMIT 1"));
			Integer integral = Objects.isNull(integralEntity) ? 0 : integralEntity.getIntegral();
			orders.forEach(order -> {
				String id = order.getString("id");
				String productId = order.getString("product_id");
				Integer appointMoney = order.getInteger("appoint_money");
				Integer dateNum = order.getInteger("date_num");
				String cardNum = order.getString("card_num");
				String raiseId = order.getString("raise_id");
				CustomerUserEntity userEntity = userEntityMap.get(cardNum);
				if (Objects.nonNull(userEntity)) {
					CustomerUserIntegralItemEntity integralItem = new CustomerUserIntegralItemEntity();
					integralItem.setCustomerUserId(userEntity.getId());
					integralItem.setOrderId(id);
					integralItem.setProductId(productId);
					integralItem.setRaiseId(raiseId);
					integralItem.setAppointMoney(appointMoney);
					// ????????????
					// 1??????????????? <= 12 ??????????????????????????????????????????????????????10????????????????????????=?????????*????????????/12)
					// 2???????????????> 12?????? ????????????????????????10??????
					Integer _integral = dateNum <= 12 ? (appointMoney * dateNum / 12 * integral) : (appointMoney * integral);
					integralItem.setIntegral(_integral);
					integralItem.setDescription("??????????????????");
					integralItem.setCreateTime(new Date());
					customerUserIntegralItemDao.insert(integralItem);
				}
			});
		}
	}

	@Override
	public PageUtils historyDuifuPage(Map<String, Object> params) {
		IPage<JSONObject> pages = this.baseMapper.historyDuifuPage(new Query<JSONObject>().getPage(params), params);
		pages.getRecords().forEach(order -> {
			List<HistoryDuifuPayEntity> payEntities = historyDuifuPayDao.selectList(Wrappers.lambdaQuery(HistoryDuifuPayEntity.class).eq(HistoryDuifuPayEntity::getHistoryDuifuId, order.getString("id")));
			order.put("orderPayList", payEntities);
		});
		return new PageUtils(pages);
	}

	@Override
	public List<JSONObject> duifuNoticeData(String startDate, String endDate) {
		List<JSONObject> list = this.baseMapper.duifuNoticeData(startDate, endDate);
		list.forEach(record -> {
			JSONObject order = this.baseMapper.getOrderById(record.getString("order_id"));
			SysUserEntity userEntity = sysUserService.getById(order.getLong("sale_id"));
			if (Objects.nonNull(userEntity)) {
				record.put("user_id", userEntity.getUserId());
				record.put("realname", userEntity.getRealname());
				record.put("mobile", userEntity.getMobile());
			}
		});
		return list;
	}




	@Override
	public void addYouZanPoints(String token, String orderId, String uptType) throws SDKException {
		OrderEntity orderEntity = this.getById(orderId);
		if ("pass".equals(uptType)) {
			// ???????????????????????? ????????????
			CustomerUserEntity userEntity = customerUserDao.selectOne(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getCardNum, orderEntity.getCardNum()).last("LIMIT 1"));
			if (Objects.nonNull(userEntity)) {
				Integer type = 1;
				addPoints(token, orderEntity, userEntity, type);
				// ????????????????????????
				if (Objects.nonNull(userEntity.getAskCode())) {
					CustomerUserIntegralItemEntity userIntegralItem = customerUserIntegralItemDao.selectOne(Wrappers.lambdaQuery(CustomerUserIntegralItemEntity.class)
							.eq(CustomerUserIntegralItemEntity::getInviterId, userEntity.getId()).last("LIMIT 1"));
					// ?????????????????????????????????
					if (Objects.isNull(userIntegralItem)) {
						// ???????????????
						CustomerUserEntity customerUser = customerUserDao.selectOne(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getCode, userEntity.getAskCode()).last("LIMIT 1"));
						type = 2;
						addPoints(token, orderEntity, customerUser, type);
					}
				}

			}
		}
	}

	public String addPoints(String token, OrderEntity orderEntity, CustomerUserEntity userEntity, Integer type) throws SDKException {
		String description = type == 1 ? "????????????" : "??????????????????";
		// ????????????
		IntegralEntity integralEntity = integralDao.selectOne(Wrappers.lambdaQuery(IntegralEntity.class).orderByDesc(IntegralEntity::getCreateTime).last("LIMIT 1"));
		Integer integral = Objects.isNull(integralEntity) ? 0 : integralEntity.getIntegral();
		CustomerUserIntegralItemEntity integralItem = new CustomerUserIntegralItemEntity();
		// 1??????????????? <= 12 ??????????????????????????????????????????????????????10????????????????????????=?????????*????????????/12)
		// 2???????????????> 12?????? ????????????????????????10??????

		Integer newIntegral = orderEntity.getDateNum() <= 12 ? (BigDecimal.valueOf(orderEntity.getAppointMoney())
				.multiply(BigDecimal.valueOf(orderEntity.getDateNum()))
				.divide(BigDecimal.valueOf(12))
				.multiply(BigDecimal.valueOf(integral))).intValue() :
				(BigDecimal.valueOf(orderEntity.getAppointMoney()).multiply(BigDecimal.valueOf(integral)).intValue());
		integralItem.setIntegral(newIntegral);

		// ??????????????????
		if (2 == type) {
			Date now = DateUtils.stringToDate(DateUtils.format(new Date(), DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN);
			IntegralActivityEntity integralActivity = integralActivityDao.selectOne(Wrappers.lambdaQuery(IntegralActivityEntity.class)
					.eq(IntegralActivityEntity::getDelFlag, 0).orderByDesc(IntegralActivityEntity::getCreateTime).last("LIMIT 1"));
			if (Objects.nonNull(integralActivity) && (integralActivity.getBeginDate().compareTo(now) == -1 && integralActivity.getEndDate().compareTo(now) == 1)) {
				// ????????????
				int val = BigDecimal.valueOf(newIntegral) .multiply(BigDecimal.valueOf(integralActivity.getIntegralProportion())).divide(BigDecimal.valueOf(100)).intValue();
				integralItem.setIntegral(val);
			}else {
				return null;
			}

		}
		integralItem.setType(type);
		integralItem.setCustomerUserId(userEntity.getId());
		integralItem.setOrderId(orderEntity.getId());
		integralItem.setProductId(orderEntity.getProductId());
		integralItem.setRaiseId(orderEntity.getRaiseId());
		integralItem.setAppointMoney(orderEntity.getAppointMoney());
		integralItem.setDescription(description);
		integralItem.setCreateTime(new Date());
		YouZanUserEntity youZanUserEntity = youZanUserDao.selectOne(Wrappers.lambdaQuery(YouZanUserEntity.class).eq(YouZanUserEntity::getUserId, userEntity.getId()).last("LIMIT 1"));
		if (Objects.nonNull(youZanUserEntity)) {
			YouzanCrmCustomerPointsIncreaseResult result = YouZanClients.addPoints(token, youZanUserEntity.getYzOpenId(), newIntegral);
			if (Objects.nonNull(result)) {
				integralItem.setCode(result.getCode());
				integralItem.setResult(JSON.toJSONString(result));
			}
		}
		customerUserIntegralItemDao.insert(integralItem);
		return null;
	}

	@Override
	public List<DuiFuNoticeModel> orderPayNoticeData(String startDate, String endDate) {
		List<OrderPayEntity> orderPayEntities = orderPayDao.orderPayList(new ArrayList<>(), startDate, endDate);
		List<DuiFuNoticeModel> models = new ArrayList<>();
		if (!orderPayEntities.isEmpty()) {
			Set<String> orderIds = new HashSet<>();
			orderPayEntities.forEach(orderPayEntity -> orderIds.add(orderPayEntity.getOrderId()));
			List<DuiFuNoticeModel> list = this.baseMapper.selectDuifuNoticeData(new ArrayList<>(orderIds));
			Map<String, DuiFuNoticeModel> noticeModelMap = list.stream().collect(Collectors.toMap(DuiFuNoticeModel::getId, model -> model));
			orderPayEntities.forEach(entity -> {
				DuiFuNoticeModel noticeModel = noticeModelMap.get(entity.getOrderId());
				OrderEntity orderEntity = this.baseMapper.selectById(entity.getOrderId());
				SysUserEntity userEntity = sysUserService.getById(orderEntity.getSaleId());
				if (Objects.nonNull(userEntity)) {
					noticeModel.setSaleId(userEntity.getUserId());
					noticeModel.setRealname(userEntity.getRealname());
					noticeModel.setMobile(userEntity.getMobile());
				}
				if (Objects.nonNull(noticeModel)) {
					noticeModel.setName(entity.getName());
					noticeModel.setPayDate(entity.getPayDate());

				}
				noticeModel.setPayMoney(entity.getPayMoney());
				models.add(noticeModel);
			});
		}
		return models;
	}

	@Override
	public PageUtils stockRightOrders(Map<String, Object> params) {
		IPage<JSONObject> orders = this.baseMapper.stockRightOrders(new Query<JSONObject>().getPage(params), params);
		orders.getRecords().forEach(order -> {
			String date = order.getString("date");
			Integer addBonus = Objects.nonNull(date) ? 1 : 0;
			order.put("addBonus", addBonus);
		});
		return new PageUtils(orders);
	}

	@Override
	public PageUtils bondOrders(Map<String, Object> params) {
		IPage<JSONObject> orders = this.baseMapper.bondOrders(new Query<JSONObject>().getPage(params), params);
		orders.getRecords().forEach(order -> {
			String productId = order.getString("product_id");
			String cardNum = order.getString("card_num");
			// ????????????
			Integer purchase = orderPurchaseDao.selectCount(Wrappers.lambdaQuery(OrderPurchaseEntity.class).eq(OrderPurchaseEntity::getProductId, productId).eq(OrderPurchaseEntity::getCardNum, cardNum));
			// ????????????
			Integer redeem = orderRedeemDao.selectCount(Wrappers.lambdaQuery(OrderRedeemEntity.class).eq(OrderRedeemEntity::getProductId, productId).eq(OrderRedeemEntity::getCardNum, cardNum));
			// ????????????
			Integer bonus = orderBonusDao.selectCount(Wrappers.lambdaQuery(OrderBonusEntity.class).eq(OrderBonusEntity::getProductId, productId).eq(OrderBonusEntity::getCardNum, cardNum));
			// ????????????
			// Integer purchasePortion = orderPurchaseDao.sumPortion(productId, cardNum);
			// Integer redeemPortion = orderRedeemDao.sumPortion(productId, cardNum);
			// Integer portion = purchasePortion - redeemPortion;
			order.put("purchase", purchase);
			order.put("redeem", redeem);
			order.put("bonus", bonus);
			// order.put("portion", portion);
		});
		return new PageUtils(orders);
	}

	@Override
	public void updateOrderPortion(String id, Integer portion) {

		this.baseMapper.update(new OrderEntity(), Wrappers.lambdaUpdate(OrderEntity.class).set(OrderEntity::getPortion, portion).eq(OrderEntity::getId, id));
	}

	@Override
	public PageUtils customerBondPages(Map<String, Object> params, String cardNum) {
		// 1-???????????? 2-????????????
		Integer isHistory = Convert.convert(Integer.class, params.get("isHistory"), 1);
		return new PageUtils(this.baseMapper.customerBondPages(new Query<JSONObject>().getPage(params), isHistory, cardNum));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void transferOrder(JSONObject json, Long userId) {
		// ?????????
		String orderId = json.getString("orderId");
		// ????????????
		String customerName = json.getString("customerName");
		// ??????????????????
		String cardNum = json.getString("cardNum");
		// ?????????
		Long saleId = json.getLong("saleId");
		OrderEntity orderEntity = this.baseMapper.selectById(orderId);
		if (Objects.nonNull(orderEntity)) {
			// ????????????????????????
			this.baseMapper.update(new OrderEntity(), Wrappers.lambdaUpdate(OrderEntity.class).set(OrderEntity::getStatus, 5)
					.set(OrderEntity::getUptDate, new Date()).set(OrderEntity::getUptBy, userId).eq(OrderEntity::getId, orderId));

			String newOrderId = String.valueOf(IdWorker.getId());
			// ?????????????????????????????????
			orderBonusDao.transferStockRight(orderId, newOrderId, cardNum);
			// ??????(????????????)???????????????????????????
			orderBonusDao.transferBond(newOrderId, cardNum, orderEntity.getProductId(), orderEntity.getCardNum());
			// ???????????????
			orderEntity.setId(newOrderId);
			orderEntity.setStatus(7);
			orderEntity.setCustomerName(customerName);
			orderEntity.setCardNum(cardNum);
			orderEntity.setSaleId(saleId);
			orderEntity.setCreateDate(new Date());
			orderEntity.setCreateBy(String.valueOf(userId));
			this.baseMapper.insert(orderEntity);
		}

	}
}