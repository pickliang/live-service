package io.live_mall.modules.server.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.OrgTaskDao;
import io.live_mall.modules.server.entity.OrgTaskDetailEntity;
import io.live_mall.modules.server.entity.OrgTaskEntity;
import io.live_mall.modules.server.service.OrgTaskDetailService;
import io.live_mall.modules.server.service.OrgTaskService;
import io.live_mall.modules.server.service.YjViewService;


@Service("orgTaskService")
public class OrgTaskServiceImpl extends ServiceImpl<OrgTaskDao, OrgTaskEntity> implements OrgTaskService {
	
	@Autowired
	YjViewService yjViewService;
	
	@Autowired
	OrgTaskDetailService  orgTaskDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrgTaskEntity> page = this.page(
                new Query<OrgTaskEntity>().getPage(params),
                new QueryWrapper<OrgTaskEntity>()
        );

        return new PageUtils(page);
    }
    
    @Override
    public OrgTaskEntity getById(Serializable id) {
    	// TODO Auto-generated method stub
    	 OrgTaskEntity orgTaskEntity = super.getById(id);
    	 if(orgTaskEntity == null) {
    		 return null;
    	 }
    	 List<OrgTaskDetailEntity> orgTaskDetaiList = orgTaskDetailService.list(new QueryWrapper<OrgTaskDetailEntity>().orderByAsc("plan_date"));
    	 orgTaskEntity.setOrgTaskDetailList(orgTaskDetaiList);
    	 //排除
    	 LinkedHashMap<String, List<OrgTaskDetailEntity>> linkedHashMap = new LinkedHashMap<String, List<OrgTaskDetailEntity>>();
    	 for (OrgTaskDetailEntity orgTaskDetailEntity : orgTaskDetaiList) {
    		 orgTaskDetailEntity.setData(JSONArray.parseArray(orgTaskDetailEntity.getDataStr()));
    		 List<OrgTaskDetailEntity> list = linkedHashMap.get(orgTaskDetailEntity.getPlanDate());
    		 if(list==null) {
    			 list=new ArrayList<OrgTaskDetailEntity>();
    			 
    			 linkedHashMap.put(orgTaskDetailEntity.getPlanDate(), list);
    		 }
    		 list.add(orgTaskDetailEntity);
		 }
    	 orgTaskEntity.setOrgTaskDetailMap(linkedHashMap);
    	return orgTaskEntity;
    }
    
    @Override
    public boolean saveOrUpdate(OrgTaskEntity task) {
    	List<OrgTaskEntity> list = this.list(new QueryWrapper<OrgTaskEntity>().eq("year", task.getYear()).eq("status",1).ne("id", task.getId()));
    	if(list !=null && list.size() > 0 ) {
    		 throw new RRException(task.getYear() +"已存在有效补贴，请停止后");
    	}
    	// TODO Auto-generated method stub
    	boolean saveOrUpdate = super.saveOrUpdate(task);
		/*if(saveOrUpdate) {
			yjViewService.sysTask(entity.getId(), entity.getCreateBy());
		}*/
    	orgTaskDetailService.remove(new QueryWrapper<OrgTaskDetailEntity>().eq("task_id", task.getId()));
    	LinkedHashMap<String, List<OrgTaskDetailEntity>> orgTaskDetailMap = task.getOrgTaskDetailMap();
    	List<OrgTaskDetailEntity> arrayList = new ArrayList<OrgTaskDetailEntity>();
    	for (String key : orgTaskDetailMap.keySet()) {
    		orgTaskDetailMap.get(key).stream().forEach(e->{
    			e.setTaskId(task.getId());
    			e.setDataStr(e.getData().toString());
    			arrayList.add(e);
    		});
		}
    	orgTaskDetailService.saveOrUpdateBatch(arrayList);
		/*if(saveOrUpdate) {
			yjViewService.sysTask(task);
		}*/
    	return true;
    }
    
    
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
    	// TODO Auto-generated method stub
    	boolean removeByIds = super.removeByIds(idList);
    	if(removeByIds) {
    		for (Serializable serializable : idList) {
    			orgTaskDetailService.remove(new QueryWrapper<OrgTaskDetailEntity>().eq("task_id", serializable));
			}
    	}
    	return true;
    }
}