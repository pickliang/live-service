package io.live_mall.modules.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.ProductCashDao;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.ProductCashEntity;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.ProductCashService;


@Service("productCashService")
public class ProductCashServiceImpl extends ServiceImpl<ProductCashDao, ProductCashEntity> implements ProductCashService {

	@Autowired
	OrderService orderservice;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductCashEntity> page = this.page(
                new Query<ProductCashEntity>().getPage(params),
                new QueryWrapper<ProductCashEntity>()
        );
        page.getRecords().stream().forEach(e->{
        	e.setOrder(orderservice.getOne(new QueryWrapper<OrderEntity>().eq("order_no",e.getOrderNo())));
        });
        return new PageUtils(page);
    }
    
    @Override
    public ProductCashEntity getById(Serializable id) {
    	// TODO Auto-generated method stub
    	ProductCashEntity productCash = super.getById(id);
    	if(productCash == null) {
    		return null;
    	}
    	productCash.setOrder(orderservice.getOne(new QueryWrapper<OrderEntity>().eq("order_no",productCash.getOrderNo())));
    	return productCash;
    }
    
    
    @Override
    public void crateProductCashAll( String prodcutId) {
    	List<ProductCashEntity> productCashList = this.baseMapper.getProductCashList(prodcutId);
    	productCashList.stream().forEach(e->{
    		//
    	});
    }
    
    

}