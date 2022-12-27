package io.live_mall.modules.server.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.date.DateUtil;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ProductUserDao;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.ProductUserEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.ProductUserService;


@Service("raiseUserService")
public class ProductUserServiceImpl extends ServiceImpl<ProductUserDao,ProductUserEntity> implements ProductUserService {
	
	@Autowired
	OrderService orderService;
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductUserEntity> page = this.page(
                new Query<ProductUserEntity>().getPage(params),
                new QueryWrapper<ProductUserEntity>()
        );

        return new PageUtils(page);
    }

    
	@Override
	@Transactional
	public void createReiseUser(RaiseEntity raise) {
		// TODO Auto-generated method stub
		List<OrderEntity> orderList = orderService.list(new QueryWrapper<OrderEntity>().eq("product_id",raise.getProductId()).eq("status", 4));
		Map<Long,List<OrderEntity>> orderMap=new HashMap<Long, List<OrderEntity>>();
		for (OrderEntity orderEntity : orderList) {
			List<OrderEntity> list = orderMap.get(orderEntity.getSaleId());
			if(list==null) {
				list=new ArrayList<OrderEntity>();
			}
			list.add(orderEntity);
			orderMap.put(orderEntity.getSaleId(),list);
		}
		
		for (Long userId : orderMap.keySet()) {
			calcOrderYongRate(orderMap.get(userId), raise);
		}
	}
	
	private void  calcOrderYongRate(List<OrderEntity> orderList,RaiseEntity raise) {
		Map<String,List<OrderEntity>> orderMap=new HashMap<String, List<OrderEntity>>();
		for (OrderEntity orderEntity : orderList) {
			List<OrderEntity> list = orderMap.get(DateUtil.format(DateUtil.parse(orderEntity.getPayDate()), "yyy-MM"));
			if(list==null) {
				list=new ArrayList<OrderEntity>();
			}
			list.add(orderEntity);
			orderMap.put(DateUtil.format(DateUtil.parse(orderEntity.getPayDate()), "yyy-MM"),list);
		}
		for (String date : orderMap.keySet()) {
			List<OrderEntity> datelist = orderMap.get(date);
			int sumMoney=0;
			for (OrderEntity orderEntity : datelist) {
				sumMoney+=orderEntity.getAppointMoney();
			}
			BigDecimal decimal = OrderUtils.getYjRateBigDecimal( raise.getCommission(), sumMoney);
			for (OrderEntity orderEntity : datelist) {
				orderEntity.setYongjinRate(decimal);
				orderService.updateById(orderEntity);
			}
		}
	}
	

}