package io.live_mall.modules.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ProductUnitDao;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.ProductUnitEntity;
import io.live_mall.modules.server.model.UnitDataModel;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.ProductUnitService;


@Service("productUnitService")
public class ProductUnitServiceImpl extends ServiceImpl<ProductUnitDao, ProductUnitEntity> implements ProductUnitService {
	
	@Autowired
	ProductService productService;
	
	

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductUnitEntity> page = this.page(
                new Query<ProductUnitEntity>().getPage(params),
                new QueryWrapper<ProductUnitEntity>().orderByDesc("create_date")
        );
        return new PageUtils(page);
    }
    
    
    
    @Override
    public ProductUnitEntity getOne(Wrapper<ProductUnitEntity> queryWrapper) {
    	// TODO Auto-generated method stub
    	ProductUnitEntity productUnit = super.getOne(queryWrapper);
    	if(productUnit == null ) {
   		 return null;
    	}
    	 String incomeData = productUnit.getIncomeData();
    	 if( StringUtils.isNotBlank(incomeData) && 2 !=productUnit.getIncomeType()) {
    		 List<UnitDataModel> parseArray = JSONArray.parseArray(incomeData,UnitDataModel.class);
    		 if(parseArray.size()> 0) {
    			 double min = parseArray.stream().mapToDouble(UnitDataModel::getName).min().getAsDouble();
        		 double max = parseArray.stream().mapToDouble(UnitDataModel::getName).max().getAsDouble();
        		 if(min==max) {
        			 productUnit.setIncomeDataMinMax(min+"%");
        		 }else {
        			 productUnit.setIncomeDataMinMax(min+"%-"+max+"%");
        		 }
    		 }
    		 
    	 }
    	 productUnit.setProduct(productService.getOne(new QueryWrapper<ProductEntity>().eq("id", productUnit.getProductId())));
    	return productUnit;
    }
    
    
    
    
    @Override
    public ProductUnitEntity getById(Serializable id) {
    	// TODO Auto-generated method stub
    	 if(id == null) {
			  return null;
		  }
    	 ProductUnitEntity productUnit = super.getById(id);
    	 if(productUnit == null ) {
    		 return null;
    	 }
    	 String incomeData = productUnit.getIncomeData();
    	 if(incomeData !=null &&productUnit.getIncomeType() !=2 ) {
    		 List<UnitDataModel> parseArray = JSONArray.parseArray(incomeData,UnitDataModel.class); 
    		 if(parseArray.size() > 0) {
    			 double min = parseArray.stream().mapToDouble(UnitDataModel::getName).min().getAsDouble();
        		 double max = parseArray.stream().mapToDouble(UnitDataModel::getName).max().getAsDouble();
        		 if(min==max) {
        			 productUnit.setIncomeDataMinMax(min+"%");
        		 }else {
        			 productUnit.setIncomeDataMinMax(min+"%-"+max+"%");
        		 }
    		 }
    		 
    	 }else {
    		 productUnit.setIncomeDataMinMax(incomeData);
    	 }
    	 
    	 productUnit.setProduct(productService.getOne(new QueryWrapper<ProductEntity>().eq("id", productUnit.getProductId())));
    	return productUnit;
    }

}