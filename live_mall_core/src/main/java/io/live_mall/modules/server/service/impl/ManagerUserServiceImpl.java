package io.live_mall.modules.server.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.collection.CollectionUtil;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.ManagerUserDao;
import io.live_mall.modules.server.entity.ManagerUserEntity;
import io.live_mall.modules.server.service.ManagerUserService;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.RaiseService;


@Service("managerUserService")
public class ManagerUserServiceImpl extends ServiceImpl<ManagerUserDao, ManagerUserEntity> implements ManagerUserService {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	RaiseService raiseService;
	
    @Override
    public PageUtils queryPage(JSONObject params) {
    	QueryWrapper<ManagerUserEntity> queryWrapper = new QueryWrapper<ManagerUserEntity>();
    	if(StringUtils.isNotBlank(params.getString("custodianType"))) {
        	queryWrapper.in("custodian_type",CollectionUtil.toList( params.getString("custodianType").split(",")));
    	}
    	
    	if(StringUtils.isNotBlank(params.getString("registerNo"))) {
        	queryWrapper.like("register_no",params.getString("registerNo"));
    	}
    	
    	if(StringUtils.isNotBlank(params.getString("custodianName"))) {
    		queryWrapper.like("custodian_name",params.getString("custodianName"));
    	}
    	
    	
        IPage<ManagerUserEntity> page = this.page(
                new Query<ManagerUserEntity>().getPage(params),
                queryWrapper.orderByDesc("create_date")
        );
        page.getRecords().stream().forEach(e->{
        	ManagerUserEntity userEntity = this.baseMapper.getOneAndTj(e.getId());
        	
        	e.setPlanRaiseMoenyUsdt(userEntity.getPlanRaiseMoenyUsdt());
        	e.setPlanRaiseMoenyRmb(userEntity.getPlanRaiseMoenyRmb());
        	e.setRaiseMoneyTotalRmb(userEntity.getRaiseMoneyTotalRmb());
        	e.setRaiseMoneyTotalUsdt(userEntity.getRaiseMoneyTotalUsdt());
        });
        
        return new PageUtils(page);
    }
    
    

}