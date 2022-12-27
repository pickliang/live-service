package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.SysOrgGroupDao;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("sysOrgGroupService")
public class SysOrgGroupServiceImpl extends ServiceImpl<SysOrgGroupDao, SysOrgGroupEntity> implements SysOrgGroupService {
	
	@Autowired
	SysUserService sysUserService;
	
	
	@Override
	public boolean saveOrUpdate(SysOrgGroupEntity entity) {
		// TODO Auto-generated method stub
		entity.setCreateDate(new Date());
		entity.setUptDate(new Date());
		return super.saveOrUpdate(entity);
	}
	
	
	
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysOrgGroupEntity> page = this.page(
                new Query<SysOrgGroupEntity>().getPage(params),
                new QueryWrapper<SysOrgGroupEntity>()
        );

        return new PageUtils(page);
    }
    
    @Override
    public SysOrgGroupEntity getById(Serializable id) {
    	// TODO Auto-generated method stub
    	SysOrgGroupEntity org = super.getById(id);
    	if(org == null) {
    		return null;
    	}
    	org.setSysUserEntity(sysUserService.getOne(new QueryWrapper<SysUserEntity>().eq("user_id", org.getPersonCharge())));
    	org.setSysuserList(sysUserService.getUserListByOrgId(org.getId()));
    	JSONObject orgOneView = this.getBaseMapper().getOrgOneView(org.getId());
    	if(orgOneView !=null) {
    		org.setIds(orgOneView.getString("orgids"));
    	}
		return org;
    }
    
    @Override
   	public List<SysOrgGroupEntity> listTree(String name) {
    	 List<SysOrgGroupEntity> parentList=null;
       	if(StringUtils.isNotBlank(name)){
       		parentList= this.baseMapper.getListByName(name);
       	}else {
       		parentList=this.baseMapper.getListByParentIdIsNull();
       	}
       	parentList.stream().forEach(e->{
       		e.setLevel(1);
       		getChildrenList(e);
       	});
   		return parentList;
   	}
    
    @Override
	public List<SysOrgGroupEntity> list(Wrapper<SysOrgGroupEntity> queryWrapper) {
    	 List<SysOrgGroupEntity> childrenList = super.list(queryWrapper);
		 return childrenList;
	}
    
    private void getChildrenList(SysOrgGroupEntity org){
    	List<SysOrgGroupEntity> childrenList = this.baseMapper.getListByParentId(org.getId());
    	if(childrenList != null && childrenList.size()> 0  ) {
    		childrenList.stream().forEach(e->{
    			e.setLevel(org.getLevel()+1);
           		getChildrenList(e);
           	});
    	}
    	org.setChildrenList(childrenList);
    }
    
    private void getChildrenList(SysOrgGroupEntity org,List<String> orgChildrenIdList){
    	List<SysOrgGroupEntity> childrenList = this.baseMapper.getListByParentId(org.getId());
    	childrenList.stream().forEach(e->{
       		getChildrenList(e,orgChildrenIdList);
       	});
    	orgChildrenIdList.add(org.getId());
    	org.setChildrenList(childrenList);
    }
    
    
    @Override
    public String getChildrenOrgIdAndCurrentId(String parentId){
		/*List<String> orgChildrenIdList = new ArrayList<String>();
		orgChildrenIdList.add(parentId);
		List<SysOrgGroupEntity> childrenList = this.list(new QueryWrapper<SysOrgGroupEntity>().eq("parent_id",parentId));
		childrenList.stream().forEach(e->{
			getChildrenList(e,orgChildrenIdList);
		});
		String ids="";
		for (String e : orgChildrenIdList) {
			ids+="'"+e+"',";
		}*/
    	List<String> orgIds = this.baseMapper.getOrgIds(parentId);
    	String ids="";
		for (String e : orgIds) {
			ids+="'"+e+"',";
		}
    	return ids.substring(0,ids.length()-1);
    }





	@Override
	public List<JSONObject> getOrgView() {
		// TODO Auto-generated method stub
		return this.baseMapper.getOrgView();
	}



	@Override
	public JSONObject getOrgOneView(String orgId) {
		// TODO Auto-generated method stub
		return this.baseMapper.getOrgOneView(orgId);
	}





	@Override
	@Transactional
	public void removeByIds(String ids) {
		// TODO Auto-generated method stub
		this.baseMapper.removeChild(ids);
		this.removeById(ids);
	}

	@Override
	public String orgGroupIds(String name) {
		List<String> ids = this.baseMapper.orgGroupIds(name);
		return ids.stream().collect(Collectors.joining(","));
	}
}