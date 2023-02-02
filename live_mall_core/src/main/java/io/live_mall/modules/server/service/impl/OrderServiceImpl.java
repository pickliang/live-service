package io.live_mall.modules.server.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.*;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.OrderModel;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.*;
import io.live_mall.modules.server.utils.ValidateUtils;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
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
	@Transactional
	public int updateOrder(OrderEntity order, SysUserEntity user) {
		RaiseEntity raiseEntity = raiseService.getById(order.getRaiseId());
		OrderEntity orderEntity = this.getById(order.getId());
		orderEntity.setStatus(order.getStatus());
		order.setUptBy(user.getRealname());
		order.setUptDate(new Date());
		
		if ("pass".equals(order.getUptType())) {
			order.setAduitId(user.getUserId());
			order.setAduitTime(new Date());
			//创建客户信息
			MemberEntity memberEntity = memberService.saveOrUpdate(orderEntity);
			if (memberEntity != null) {
				order.setCustId(memberEntity.getMemberNo());
			}
			smsService.sendMsgToCust(orderEntity);
			MemberEntity one = memberService.getOne(new QueryWrapper<MemberEntity>().eq("card_num", order.getCardNum()));
			if( one !=null && !String.valueOf(order.getSaleId()).equals( one.getSaleId())) {
				throw new RRException("该会员,已经是其他业务客户,请联系相关负责人");
			}
		}
		if ("tuikuan".equals(order.getUptType())) {
			smsService.sendMsgToCust(orderEntity);
		}
		if ("auth".equals(order.getUptType())) {
			if( StringUtils.isNotBlank(order.getPhone()) ) {
				if(!ValidateUtils.isMobile(order.getPhone())) {
					throw new RRException("手机号不符合要求");
				}
				
				/*销售员自己买*/
				/**
				 * 1、首先验证客户的手机号是否与理财师本人的手机号重复，以防理财师本人用自己的手机号登记客户；
				 * 2、验证与其他客户的手机号是否重复，不同客户之间的手机号不能相同
				 */
				if(!user.getRealname().equals(order.getCustomerName())) {
					if(StringUtils.isNotBlank(user.getMobile()) && order.getPhone().equals(user.getMobile())) {
						throw new RRException("手机号与理财师本人手机号重复，请修改。");
					}
					int count = memberService.count(new QueryWrapper<MemberEntity>().eq("phone", order.getPhone()).ne("sale_id", order.getSaleId()));
					if(count>0) {
						throw new RRException("手机号不符合要求,已被使用");
					}
				}
			}else {
					throw new RRException("请填写手机号");
			}
			order.setStatus(0);
			order.setAduitTime(new Date());
			//验证其他人员是否有该客户信息
			MemberEntity one = memberService.getOne(new QueryWrapper<MemberEntity>().eq("card_num", order.getCardNum()));
			if( one !=null && !String.valueOf(order.getSaleId()).equals( one.getSaleId())) {
				throw new RRException("该会员,已经是其他业务客户,请联系相关负责人");
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
			//产品是否满足下线进行下线
			raiseService.down(order.getRaiseId());	
		}
		this.updateById(order);
		return 0;
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
	 * 募集期总额
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
	 * 8、订单都已审核通过，且剩余额度为0时或者，募集期管理的封账按钮要可点击
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
		Integer type = Integer.valueOf(String.valueOf(params.get("type")));
		type = Objects.isNull(type) ? 0 : type;
		Integer isHistory = Integer.valueOf(String.valueOf(params.get("isHistory")));
		isHistory = Objects.isNull(isHistory) ? 1 : isHistory;
		IPage<JSONObject> pages = this.baseMapper.customerDuifuPage(new Query<JSONObject>().getPage(params), cardNum, type, isHistory);
		pages.setRecords(assembleOrderItem(pages.getRecords()));

		return new PageUtils(pages);
	}

	private List<JSONObject> assembleOrderItem(List<JSONObject> records) {
		records.forEach(order -> {
			// 是否灰色展示 0-否 1-是
			Integer isAsh = 0;
			List<Map<String, Object>> maps = new ArrayList<>();
			 String pay_date = "";
			 String pay_money = "";
			 Integer dateNum = order.getInteger("date_num");
			 String end_date = "";
			 if (dateNum == 0) {
			 	end_date = order.getString("establish_time");
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
					String payType = pay.getPayDate().compareTo(now) < 0 ? "已付" : "";
					map.put("payDate", pay.getPayDate());
					map.put("payMoney", pay.getPayMoney() + "元");
					map.put("payType", payType);
					maps.add(map);
				});
				OrderPayEntity payEntity = orderPayList.get(orderPayList.size() - 1);
				isAsh = payEntity.getPayDate().compareTo(now) < 0 ? 1 : 0;
				// OrderPayEntity orderPayEntity = this.baseMapper.nextOrder(order.getString("order_id"));
				// if (Objects.nonNull(orderPayEntity)) {
				// 	pay_date = orderPayEntity.getPayDate();
				// 	pay_money = String.valueOf(orderPayEntity.getPayMoney());
				// }
			}else {
				// HistoryDuifuPayEntity duifuPayEntity = historyDuifuPayDao.nextHistoryDuifuPay(order.getString("order_id"));
				// if (Objects.nonNull(duifuPayEntity)) {
				// 	pay_date = duifuPayEntity.getPayDate();
				// 	pay_money = String.valueOf(duifuPayEntity.getPayMoney());
				// }
				List<HistoryDuifuPayEntity> payEntityList = historyDuifuPayDao.selectList(Wrappers.lambdaQuery(HistoryDuifuPayEntity.class)
						.eq(HistoryDuifuPayEntity::getHistoryDuifuId, order.getString("order_id")).orderByAsc(HistoryDuifuPayEntity::getPayDate));
				if (!payEntityList.isEmpty()) {
					payEntityList.forEach(pay -> {
						Map<String, Object> map = Maps.newHashMap();
						String payType = pay.getPayDate().compareTo(now) < 0 ? "已付" : "";
						map.put("payDate", pay.getPayDate());
						map.put("payMoney", pay.getPayMoney() + "元");
						map.put("payType", payType);
						maps.add(map);
					});
					HistoryDuifuPayEntity historyDuifuPay = payEntityList.get(payEntityList.size() - 1);
					isAsh = historyDuifuPay.getPayDate().compareTo(now) < 0 ? 1 : 0;
				}
			}
			// order.put("pay_date", pay_date);
			// order.put("pay_money", pay_money);
			order.put("orderPayList",maps);
			order.put("isAsh", isAsh);
		});
		return records;
	}

	@Override
	public Map<String, Object> totalAssets(String cardNum) {
		Map<String, Object> result = Maps.newHashMap();
		// 累计收益：所有已付利息总和
		Double totalInterest = this.baseMapper.totalInterest(cardNum);
		// 类固收(固收)
		Double fixedIncome = this.baseMapper.fixedIncome(cardNum, 1);
		Double historyFixedIncome = this.baseMapper.historyFixedIncome(cardNum, 1);

		// 权益类(股权)
		Double stock = this.baseMapper.fixedIncome(cardNum, 2);
		Double historyStock = this.baseMapper.historyFixedIncome(cardNum, 2);
		// 净值型(二级市场)
		Double netWorth = this.baseMapper.fixedIncome(cardNum, 3);
		Double historyNetWorth = this.baseMapper.historyFixedIncome(cardNum, 3);

		result.put("totalAssets", fixedIncome + stock + netWorth + historyFixedIncome + historyStock + historyNetWorth);
		result.put("fixedIncome", fixedIncome + historyFixedIncome);
		result.put("stock", stock + historyStock);
		result.put("netWorth", netWorth + historyNetWorth);
		result.put("totalInterest", totalInterest);
		return result;
	}



	@Override
	public Map<String, Object> customerAssets(String cardNum) {
		Map<String, Object> result = Maps.newHashMap();
		// 总资产
		Double totalAssets = this.baseMapper.customerTotalAssets(cardNum);
		// 历史数据总资产
		Double assets = this.baseMapper.historyFixedIncome(cardNum, 0);

		// 年度收益
		Double annualIncome = this.baseMapper.annualIncome(cardNum);
		// 历史数据本年度收益
		Double historyAnnualIncome = this.baseMapper.historyAnnualIncome(cardNum);

		// 预期收益
		Double expectedIncome = this.baseMapper.expectedIncome(cardNum);
		Double historyExpectedIncome = this.baseMapper.historyExpectedIncome(cardNum);

		// 在投订单金额
		Double investing = this.baseMapper.investingOrderMoney(cardNum);
		// 历史在投订单金额
		Double historyInvesting = this.baseMapper.historyInvestingOrderMoney(cardNum);

		result.put("totalAssets", totalAssets + assets);
		result.put("annualIncome", annualIncome + historyAnnualIncome);
		result.put("expectedIncome", expectedIncome + historyExpectedIncome);
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
//		if (Objects.isNull(orderInfo)) {
//			orderInfo = historyDuiFuDao.HistoryDuiFuInfo(id);
//		}
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

		//产品详情
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
		// 所有身份证不为空的用户
		List<CustomerUserEntity> userEntityList = customerUserDao.selectList(Wrappers.lambdaQuery(CustomerUserEntity.class)
				.isNotNull(CustomerUserEntity::getCardNum).eq(CustomerUserEntity::getDelFlag, 0));
		if (!userEntityList.isEmpty()) {
			// 根据身份证号获取用户
			Map<String, CustomerUserEntity> userEntityMap = userEntityList.stream().collect(Collectors.toMap(CustomerUserEntity::getCardNum, entity -> entity));
			List<String> cardNums = new ArrayList<>();
			userEntityList.forEach(user -> cardNums.add(user.getCardNum()));
			// 订单
			List<JSONObject> orders = this.baseMapper.integralOrder(cardNums);
			// 积分规则
			IntegralEntity integralEntity = integralDao.selectOne(Wrappers.lambdaQuery(IntegralEntity.class).orderByDesc(IntegralEntity::getCreateTime).last("LIMIT 1"));
			Integer integral = Objects.isNull(integralEntity) ? 0 : integralEntity.getIntegral();
			orders.forEach(order -> {
				String id = order.getString("id");
				String productId = order.getString("product_id");
				Integer appointMoney = order.getInteger("appoint_money");
				Integer dateNum = order.getInteger("date_num");
				String cardNum = order.getString("card_num");
				CustomerUserEntity userEntity = userEntityMap.get(cardNum);
				if (Objects.nonNull(userEntity)) {
					CustomerUserIntegralItemEntity integralItem = new CustomerUserIntegralItemEntity();
					integralItem.setCustomerUserId(userEntity.getId());
					integralItem.setOrderId(id);
					integralItem.setProductId(productId);
					integralItem.setAppointMoney(appointMoney);
					// 积分规则
					// 1、产品期限 <= 12 个月，投资年化额每一万兑换商城积分数10积分（投资年化额=投资额*产品期限/12)
					// 2、产品期限> 12个月 投资额每一万兑换10积分
					Integer _integral = dateNum <= 12 ? (appointMoney * dateNum / 12 * integral) : (appointMoney * integral);
					integralItem.setIntegral(_integral);
					integralItem.setDescription("历史订单赠送");
					integralItem.setCreateTime(new Date());
					customerUserIntegralItemDao.insert(integralItem);
				}
			});
		}
	}
}