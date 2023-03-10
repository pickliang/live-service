package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ProductDao;
import io.live_mall.modules.server.entity.ManagerUserEntity;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.ProductUnitEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductDao, ProductEntity> implements ProductService {

	@Autowired
	RaiseService raiseService;
	
	@Autowired
	ProductUnitService productUnitService;
	
	@Autowired
	ManagerUserService managerUserService;
	
	@Autowired
	OrderService orderService;
	
    @Override
    public PageUtils queryPage(JSONObject params) {
    	QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<ProductEntity>();
    	if(StringUtils.isNotBlank(params.getString("onetype"))) {
        	queryWrapper.inSql("onetype",params.getString("onetype"));
    	}
    	
    	
    	
    	if(StringUtils.isNotBlank(params.getString("productClass"))) {
        	queryWrapper.in("product_class",params.getString("productClass"));
    	}
    	
    	if(StringUtils.isNotBlank(params.getString("status"))) {
        	queryWrapper.inSql("status", params.getString("status"));
    	}
    	
        IPage<ProductEntity> page1 = this.baseMapper.selectPageVo(
                new Query<ProductEntity>().getPage(params),
                params
        );
        
        page1.getRecords().stream().forEach(e->{
        	e.setProductUnit(productUnitService.getOne(new QueryWrapper<ProductUnitEntity>().eq("product_id",e.getId()).last(" limit 1")));
        	e.setRaiseList(raiseService.list(new QueryWrapper<RaiseEntity>().eq("product_id",e.getId())));
        	//???????????? ??? ????????????????????? ?????????????????? ?????????????????????????????????????????? ??????????????? >=1
        	e.setRaiseRun(raiseService.getNewRaise(e.getId()));
        	e.setXsjgs(this.baseMapper.getXsjgs(e.getId()));
        	e.setKhs(this.baseMapper.getKhs(e.getId()));
        	
    	});
        return new PageUtils(page1);
    }
    
    
    @Override
    public PageUtils selectPageApp(JSONObject params) {
    	QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<ProductEntity>();
    	if(StringUtils.isNotBlank(params.getString("onetype"))) {
        	queryWrapper.inSql("onetype",params.getString("onetype"));
    	}
    	
    	if(StringUtils.isNotBlank(params.getString("productClass"))) {
        	queryWrapper.in("product_class",params.getString("productClass"));
    	}
    	
    	if(StringUtils.isNotBlank(params.getString("status"))) {
        	queryWrapper.inSql("status", params.getString("status"));
    	}
    	
        IPage<ProductEntity> page1 = this.baseMapper.selectPageApp(
                new Query<ProductEntity>().getPage(params),
                params
        );
        
        page1.getRecords().stream().forEach(e->{
        	e.setProductUnit(productUnitService.getOne(new QueryWrapper<ProductUnitEntity>().eq("product_id",e.getId())));
        	e.setRaiseList(raiseService.list(new QueryWrapper<RaiseEntity>().eq("product_id",e.getId())));
        	//???????????? ??? ????????????????????? ?????????????????? ?????????????????????????????????????????? ??????????????? >=1
        	e.setRaiseRun(raiseService.getNewRaise(e.getId()));
    	});
        
        
        
        
        return new PageUtils(page1);
    }
    
    
    
  
    
    @Override
    public List<ProductEntity> list(Wrapper<ProductEntity> queryWrapper) {
    	// TODO Auto-generated method stub
    	List<ProductEntity> list = super.list(queryWrapper);
    	list.stream().forEach(e->{
        	e.setProductUnit(productUnitService.getOne(new QueryWrapper<ProductUnitEntity>().eq("product_id",e.getId())));
        	e.setRaiseList(raiseService.list(new QueryWrapper<RaiseEntity>().eq("product_id",e.getId())));
    	});
    	return list;
    }
    
    @Override
    public ProductEntity getById(Serializable id) {
    	ProductEntity productEntity = super.getById(id);
    	if(productEntity == null) {
    		return null;
    	}
    	productEntity.setProductUnit(productUnitService.getOne(new QueryWrapper<ProductUnitEntity>().eq("product_id",productEntity.getId())));
    	productEntity.setRaiseList(raiseService.list(new QueryWrapper<RaiseEntity>().eq("product_id",productEntity.getId()).orderByDesc("create_date")));
    	productEntity.setRaiseRun(raiseService.getNewRaise(productEntity.getId()));
    	//???????????????
    	if(productEntity.getManagerPeople() !=null) {
    		JSONArray list = JSONArray.parseArray(productEntity.getManagerPeople());
    		if(list.size() > 0  ) {
        		List<ManagerUserEntity> mangerList = managerUserService.list(new QueryWrapper<ManagerUserEntity>().in("id",list));
        		if(mangerList.size() > 0 ) {
        			String str="";
            		for (ManagerUserEntity managerUserEntity : mangerList) {
            			str+=managerUserEntity.getCustodianName()+",";
    				}
            		productEntity.setMangerName(str.substring(0,str.length()-1));
        		}
    		}
    	}
    	return productEntity;
    }


	@Override
	public List<ProductEntity> getProductList(JSONObject params) {
		return this.baseMapper.selectList(null);
	}
	
		
    
	@Override
	public JSONObject getTj(JSONObject params) {
		return this.baseMapper.getTj(params);
	}
	
	//?????? 0????????????  1???????????? 2????????????  3????????????  4 ?????????
	@Override
	public void uptStatus(String productId) {
		ProductEntity product = this.getOne(new QueryWrapper<ProductEntity>().eq("id", productId));
		List<RaiseEntity> list = raiseService.list(new QueryWrapper<RaiseEntity>().eq("product_id", productId));
		if(list !=null && list.size() ==0 ) {
			product.setStatus("1");//?????????
		}
		RaiseEntity raiseEntity = raiseService.getOne(new QueryWrapper<RaiseEntity>().eq("status", 1).eq("product_id", productId).last(" limit 1"));
		if(raiseEntity !=null) {
			product.setStatus("2");//?????????
		}else {
			/*?????????????????????????????????
			?????????????????????????????????*/
			//??????????????????
			BigDecimal bigDecimal = orderService.getSumMoneyByProductId(productId );
			if(bigDecimal == null) {
				bigDecimal=BigDecimal.ZERO;
			}
		    String productLabel = product.getProductScale();
			if(bigDecimal.doubleValue() >= Double.valueOf(productLabel) ) {
				product.setStatus("3");
			}else {
				product.setStatus("4");
			}
		}
		this.updateById(product);
		
	}

	@Override
	public String getProductClassByProductName(String productName) {
		return this.baseMapper.getProductClassByProductName(productName);
	}

	@Override
	public List<JSONObject> productNameList(String name, String productClass) {
		return this.baseMapper.productNameList(name, productClass);
	}
}