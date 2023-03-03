package io.live_mall.modules.server.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.RaiseDao;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static io.live_mall.modules.server.model.OrderUtils.getRate;


@Service("raiseService")
public class RaiseServiceImpl extends ServiceImpl<RaiseDao, RaiseEntity> implements RaiseService {

	@Autowired
	OrderService ordersevice;

	@Autowired
	ProductService productService;

	@Autowired
	OrderPayService orderPaySerivce;

	@Autowired
	ProductUnitService productUnitService;

	@Autowired
	ProductUserService productUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	QueryWrapper<RaiseEntity> queryWrapper = new QueryWrapper<RaiseEntity>();
    	if(StringUtils.isNotBlank(params.get("productId")+"")) {
    		queryWrapper.eq("product_id", params.get("productId"));
    	}
        IPage<RaiseEntity> page = this.page(
                new Query<RaiseEntity>().getPage(params),
                queryWrapper.orderByDesc("create_date")
        );
        page.getRecords().stream().forEach(e->{
    		e.setSyts(getSyts(e));
			QueryWrapper<OrderEntity> queryWrapper1 =new QueryWrapper<OrderEntity>()
					.eq("raise_id",e.getId())
					.in("status", OrderUtils.YX_ORDER_STATUS);
			QueryWrapper<OrderEntity> queryWrapper2 =new QueryWrapper<OrderEntity>()
					.eq("raise_id",e.getId())
					.in("status", OrderUtils.YY_ORDER_STATUS);
//					.and(wrapper -> wrapper.eq("status",2)
//							.or().eq("status",4));
			e.setRaiseOrderAll(ordersevice.count(queryWrapper2));
			Integer size = ordersevice.count(queryWrapper1);
			e.setRaiseOrder(size);

    		e.setSyrs(e.getInvestorNum()-size);
    		Integer sumRaiseMoney = ordersevice.sumMoenyRaise(e.getId());

    		Integer i= e.getRaiseMoney()-sumRaiseMoney;
			e.setOrderNum(sumRaiseMoney);
    		e.setCouldFz(ordersevice.getCouldFz(e));
			System.out.println("size===="+e.getRaiseMoney());
    		e.setSyje(i>0?i:0);
    		e.setProduct(productService.getOne(new QueryWrapper<ProductEntity>().eq("id", e.getProductId())));
    	});
        return new PageUtils(page);
    }

    @Override
    public List<RaiseEntity> list(Wrapper<RaiseEntity> queryWrapper) {
    	// TODO Auto-generated method stub
    	List<RaiseEntity> order = super.list(queryWrapper);
    	order.stream().forEach(e->{
    		e.setSyts(getSyts(e));
    		e.setSyrs(e.getInvestorNum()-ordersevice.count(new QueryWrapper<OrderEntity>().ne("status",OrderUtils.YX_ORDER_STATUS).eq("raise_id",e.getId())));
    		int i= e.getRaiseMoney()-ordersevice.sumMoenyRaise(e.getId());
    		e.setSyje(i>0?i:0);
    	});
    	return order;
    }

	@Override
	@Transactional
	public void fx(RaiseEntity raise) {
		RaiseEntity byId = this.getById(raise.getId());
		List<RaiseEntity> raiseEntitylist = this.list(new QueryWrapper<RaiseEntity>()
				.eq("product_id", raise.getProductId())
				.ne("status", 1)
				.ne("id", raise.getId()));
		raiseEntitylist.stream().forEach(e->{
			e.setStatus(2);
		});
		this.updateBatchById(raiseEntitylist);
		raise.setStatus(0);
		this.updateById(raise);
		List<RaiseEntity> raiselist = this.list(new QueryWrapper<RaiseEntity>()
				.le("begin_date", DateUtil.format(new Date(), "yyy-MM-dd HH:mm:ss"))
				.eq("status", 0)
				);
		raiselist.stream().forEach(e->{
			e.setStatus(1);
		});
		productService.uptStatus(byId.getProductId());
	}

	@Override
	public PageUtils getRaiseSuccessPage(Map<String, Object> params) {
		 IPage<JSONObject> page = this.baseMapper.getRaiseSuccessPage(
	                new Query<ProductEntity>().getPage(params),
	                params
	        );
		 page.getRecords().stream().forEach(e->{
	    		e.put("product",productService.getOne(new QueryWrapper<ProductEntity>().eq("id", e.getString("productId"))));
	    	});
		 return new PageUtils(page);
	}

	@Override
	@Transactional
	public void updateSuccess(RaiseEntity raise) {
		// TODO Auto-generated method stub
		raise.setEstablishTime(raise.getEstablishTime());
		ordersevice.success(raise);
		this.updateById(raise);
	}




	@Override
	public RaiseEntity getNewRaise(String id) {
		// TODO Auto-generated method stub
		//展示条件 为 如果有发行中的 显示发行中的 如果没有显示最新创建的募集期 状态为大于 >=1
		 RaiseEntity newRaiseRun = this.baseMapper.getNewRaiseRun(id);
		 if(newRaiseRun == null ){
			 newRaiseRun =  this.baseMapper.getNewRaiseLast(id);
		 }
		 if(newRaiseRun !=null ) {
			 newRaiseRun.setSyts(getSyts(newRaiseRun));
			 newRaiseRun.setSyrs(newRaiseRun.getInvestorNum()-(newRaiseRun.getOrderNum() == null ?0:newRaiseRun.getOrderNum()));
			int i= newRaiseRun.getRaiseMoney()-(newRaiseRun.getWcje()== null ? 0:newRaiseRun.getWcje());
			newRaiseRun.setSyje(i>0?i:0);
		 }
		return newRaiseRun;
	}

	private String getSyts(RaiseEntity newRaiseRun) {
		if(newRaiseRun.getStatus() !=null && newRaiseRun.getStatus() == 1) {
			long month = DateUtil.betweenMonth(newRaiseRun.getEndDate(),  new Date(),true);
			long day = DateUtil.between(newRaiseRun.getEndDate(), new Date(), DateUnit.DAY);
			long hour = DateUtil.between(newRaiseRun.getEndDate(),new Date(), DateUnit.HOUR);
			long minute = DateUtil.between(newRaiseRun.getEndDate(),new Date(), DateUnit.MINUTE);
			if(hour <= 24) {
				return hour+"小时"+(minute-hour*60)+"分钟";
			}else if(month == 1) {
				return day+"天";
			}else if(month >1) {
				return month+"月";
			}else {
				return day+"天";
			}
		}else {
			long month = DateUtil.betweenMonth(newRaiseRun.getBeginDate(),  new Date(),true);
			long day = DateUtil.between(newRaiseRun.getBeginDate(), new Date(), DateUnit.DAY);
			long hour = DateUtil.between(newRaiseRun.getBeginDate(),new Date(), DateUnit.HOUR);
			long minute = DateUtil.between(newRaiseRun.getBeginDate(),new Date(), DateUnit.MINUTE);
			if(hour <= 24) {
				return hour+"小时"+(minute-hour*60)+"分钟";
			}else if(month == 1) {
				return day+"天";
			}else if(month >1) {
				return month+"月";
			}else {
				return day+"天";
			}
		}

	}

	@Override
	public void canCreateOrder(String raiseId,Integer money) {
		// TODO Auto-generated method stub
		RaiseEntity byId = this.getById(raiseId);
		if(byId !=null) {
			long between = DateUtil.between(new Date(), byId.getBeginDate(), DateUnit.SECOND);
			if(between < 0) {
				throw new RRException("募集期开始时间为"+DateUtil.formatDateTime(byId.getBeginDate()));
			}
			// Integer sumMoenyRaise = ordersevice.sumMoenyRaise(raiseId);
			List<OrderEntity> orderEntities = ordersevice.list(Wrappers.lambdaQuery(OrderEntity.class).in(OrderEntity::getStatus, Arrays.asList(-1, 0, 2)).eq(OrderEntity::getRaiseId, raiseId));
			Integer totalMoney = 0;
			for (OrderEntity orderEntity : orderEntities) {
				totalMoney += orderEntity.getAppointMoney();
			}
			if(byId.getRaiseMoney().doubleValue() <(totalMoney.doubleValue()+money.doubleValue())) {
				throw new RRException("募集金额已超额");
			}
		}else {
			throw new RRException("募集期信息有误,创建失败");
		}

	}

	@Override
	public void down(String raiseId) {
		// TODO Auto-generated method stub
		Integer sumMoenyRaise = ordersevice.sumMoenyRaise(raiseId);
		RaiseEntity raiseEntity = this.getById(raiseId);
		try {
			if(sumMoenyRaise >= raiseEntity.getRaiseMoney() ) {
				raiseEntity.setStatus(2);
				this.updateById(raiseEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	@Transactional
	public void duifu(RaiseEntity raise) {
		// TODO Auto-generated method stub
		ProductEntity productEntity = productService.getById(raise.getProductId());
		if(productEntity!=null && StringUtils.isNotBlank(raise.getProductEndDate())) {
			productEntity.setProductEndDate(DateUtil.parse(raise.getProductEndDate()));
			productEntity.setDayNum(DateUtil.betweenDay(raise.getEstablishTime(), DateUtil.parse(raise.getProductEndDate()), false));
			productService.updateById(productEntity);
		}
		ProductUnitEntity productUnit = productEntity.getProductUnit();
		List<OrderEntity> list =ordersevice.list(new QueryWrapper<OrderEntity>().eq("raise_id", raise.getId()).in("status",4));
		for (OrderEntity orderEntity : list) {
			Double rate = getRate(productUnit.getIncomeData(), orderEntity.getAppointMoney());
			if(rate !=null) {
				orderEntity.setRate(new BigDecimal(rate));
			}
			//修改订单参数信息
			crateOrderPay(productEntity, raise, orderEntity);
			//计算佣金
			ordersevice.updateById(orderEntity);
		}
		raise.setDfType("1");
		productUserService.createReiseUser(raise);
		this.updateById(raise);
	}




	private void crateOrderPay(ProductEntity productEntity,RaiseEntity raise,OrderEntity order) {
		orderPaySerivce.remove(new QueryWrapper<OrderPayEntity>().eq("order_id", order.getId()));
		Integer productTermType = productEntity.getProductTermType();
		String method = raise.getMethod();//类型
		if(StringUtils.isBlank(method)  ) {
			throw new  RRException("请配置规则信息");
		}
		if(raise.getEstablishTime() == null ) {
			throw new  RRException("成立时间为空,请维护");
		}
		if(!"5".equals(method)) {
			Integer dayNum = raise.getDayNum();//月的几号
			if("2".equals(method) ) { //每季度
				dayNum= raise.getDayNum();
			} else if("4".equals(method)) {
        dayNum = raise.getDayYearNum();//每半年
      }
			Integer ys =12;
			String productTerm = productEntity.getProductTerm();
			if(productTermType !=4  || productTermType == 3) {
				ys = Integer.valueOf(productTerm.split(",")[0]);
			}else if(productTermType == 3){
				ys = Integer.valueOf(productTerm.split(",")[1]);
			}
			order.setDateNum(ys);
			order.setDateUint("月");
			order.setDateEnd(DateUtil.offsetMonth(raise.getEstablishTime(), ys));
			List<String> clrz = OrderUtils.getClrz(method, raise.getEstablishTime(), ys, dayNum);
			//利息=认购金额*年化收益率/365*（利息支付日-成立日/上一利息日）
			BigDecimal rate = order.getRate();
			ProductUnitEntity productUnit = productUnitService.getById(raise.getProductUnitId());
			rate = OrderUtils.getRateBigDecimal( productUnit.getIncomeData(), order.getAppointMoney());
			if(rate ==null) {
				throw new RRException("年化收益率请维护");
			}else {
				order.setRate(rate);
				ordersevice.updateById(order);
			}
			List<OrderPayEntity> payList = new ArrayList<OrderPayEntity>();
			OrderPayEntity orderPayEntity = new OrderPayEntity();
			orderPayEntity.setPayDate(clrz.get(0));
			orderPayEntity.setRate(rate);
			orderPayEntity.setName("第"+OrderUtils.int2chineseNum(1)+"次");
			orderPayEntity.setPayMoney(new BigDecimal(order.getAppointMoney()*10000*rate.doubleValue()/100/365*DateUtil.betweenDay(raise.getEstablishTime(), DateUtil.parseDate(clrz.get(0)), true)));
			orderPayEntity.setOrderId(order.getId());
			orderPayEntity.setNum(1);
			payList.add(orderPayEntity);
			for (int i = 1; i < clrz.size(); i++) {
				OrderPayEntity item = new OrderPayEntity();
				item.setPayDate(clrz.get(i));
				item.setName("第"+OrderUtils.int2chineseNum(i+1)+"次");
				item.setPayMoney(new BigDecimal(order.getAppointMoney()*10000*rate.doubleValue()/100/365*DateUtil.betweenDay(DateUtil.parseDate(clrz.get(i-1)), DateUtil.parseDate(clrz.get(i)), true)));
				item.setOrderId(order.getId());
				item.setNum(i+1);
				item.setRate(rate);
				payList.add(item);
			}
			orderPaySerivce.saveBatch(payList);
		}else {
			BigDecimal rate = order.getRate();
			if(rate ==null ) {

			}
			List<OrderPayEntity> payList = new ArrayList<OrderPayEntity>();
			OrderPayEntity orderPayEntity = new OrderPayEntity();
			orderPayEntity.setPayDate(raise.getProductEndDate());
			orderPayEntity.setRate(rate);
			orderPayEntity.setName("第"+OrderUtils.int2chineseNum(1)+"次");
			orderPayEntity.setPayMoney(new BigDecimal(order.getAppointMoney()*10000*rate.doubleValue()/100/365*DateUtil.betweenDay(raise.getEstablishTime(), DateUtil.parseDate(raise.getProductEndDate()), true)));
			orderPayEntity.setOrderId(order.getId());
			orderPayEntity.setNum(1);
			payList.add(orderPayEntity);
			orderPaySerivce.saveBatch(payList);
		}

	}

	@Override
	public Date getMaxEndDate(String productId) {
		// TODO Auto-generated method stub
		return this.baseMapper.getMaxEndDate(productId);
	}






}
