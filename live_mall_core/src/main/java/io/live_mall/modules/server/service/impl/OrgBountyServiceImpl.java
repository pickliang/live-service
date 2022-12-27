package io.live_mall.modules.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.OrgBountyDao;
import io.live_mall.modules.server.entity.OrgBountyEntity;
import io.live_mall.modules.server.service.OrgBountyService;
import io.live_mall.modules.server.service.YjViewService;


@Service("orgBountyService")
public class OrgBountyServiceImpl extends ServiceImpl<OrgBountyDao, OrgBountyEntity> implements OrgBountyService {
	
	@Autowired
	YjViewService yjViewService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrgBountyEntity> page = this.page(
                new Query<OrgBountyEntity>().getPage(params),
                new QueryWrapper<OrgBountyEntity>()
        );

        return new PageUtils(page);
    }
    
    @Override
    public boolean saveOrUpdate(OrgBountyEntity entity) {
    	// TODO Auto-generated method stub
    	boolean saveOrUpdate = super.saveOrUpdate(entity);
    	
		/*if(saveOrUpdate) {
			yjViewService.sysDate(entity.getId(),entity.getCreateBy());
		}*/
    	return saveOrUpdate;
    }
	

}