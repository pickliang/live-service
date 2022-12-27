package io.live_mall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.live_mall.common.utils.RedisUtils;
import io.live_mall.modules.server.entity.SysOrgUserEntity;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.server.utils.excel.ExcelParam;
import io.live_mall.modules.server.utils.excel.ExcelUtil;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportUser {
	@Autowired
	private RedisUtils redisUtils;
	
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	SysUserService userSevrice;
	
	@Autowired
	SysOrgGroupService sysOrgGroupService;
	
	@Test
	public void contextLoads() throws Exception {
			// 用uuid作为文件名，防止生成的临时文件重复
			//序号	一级组织机构	二级组织机构	三级组织机构	四级组织机构
			String excel_conf="账号:username,姓名:realname,所属一级组织机构:org1,所属二级组织机构:org2,所属三级组织机构:org3,所属四级组织机构:org4";
            ExcelParam param = new ExcelParam();
	   		param.setMap(ExcelUtil.getMap(excel_conf));
	   		param.setClassPath("com.alibaba.fastjson.JSONObject");
	   		param.setFile(new File("F:\\wxchat\\WeChat Files\\daitao25\\FileStorage\\File\\2020-11\\五道集团-组织人员.xlsx"));
	   		param.setRowNumIndex(1);
	   		param.setSheetIndex(2);
	   		JSONArray all = ExcelUtil.readXlsPart(param);
	   		List<JSONObject> orgView = sysOrgGroupService.getOrgView();
	   		for (Object object : all) {
	   			JSONObject item = (JSONObject)object;
	   			SysUserEntity user = new SysUserEntity();
	   			user.setUsername(item.getString("username"));
	   			user.setRealname(item.getString("realname"));
	   			List<Long> arrayList = new ArrayList<Long>();
	   			arrayList.add(4L);
	   			
	   			for (JSONObject org_item : orgView) {
	   				/*一级组织机构:org1,二级组织机构:org2,三级组织机构:org3,四级组织机构:org4*/
	   				if(StringUtils.isNoneBlank(item.getString("org4"))) {
	   					if(
	   							item.getString("org4").equals(org_item.getString("org4")) &&
	   							item.getString("org3").equals(org_item.getString("org3"))&&
	   							item.getString("org2").equals(org_item.getString("org2"))&&
	   							item.getString("org1").equals(org_item.getString("org1"))
	   							
	   					) {
	   						SysOrgUserEntity sysOrgUserEntity = new  SysOrgUserEntity();
	   						sysOrgUserEntity.setOrgId(org_item.getString("org_id"));
	   						sysOrgUserEntity.setOrgids(org_item.getString("orgids"));
	   						user.setOrg(sysOrgUserEntity);
	   					}
	   				}else if(StringUtils.isNoneBlank(item.getString("org3"))){
	   					if(
	   							item.getString("org3").equals(org_item.getString("org3"))&&
	   							item.getString("org2").equals(org_item.getString("org2"))&&
	   							item.getString("org1").equals(org_item.getString("org1"))
	   							
	   					) {
	   						SysOrgUserEntity sysOrgUserEntity = new  SysOrgUserEntity();
	   						sysOrgUserEntity.setOrgId(org_item.getString("org_id"));
	   						sysOrgUserEntity.setOrgids(org_item.getString("orgids"));
	   						user.setOrg(sysOrgUserEntity);
	   					}
	   					
	   				}else if(StringUtils.isNoneBlank(item.getString("org2"))){
	   					if(
	   							item.getString("org2").equals(org_item.getString("org2"))&&
	   							item.getString("org1").equals(org_item.getString("org1"))
	   							
	   					) {
	   						SysOrgUserEntity sysOrgUserEntity = new  SysOrgUserEntity();
	   						sysOrgUserEntity.setOrgId(org_item.getString("org_id"));
	   						sysOrgUserEntity.setOrgids(org_item.getString("orgids"));
	   						user.setOrg(sysOrgUserEntity);
	   					}
	   				}else {
	   					if(
	   							StringUtils.isNoneBlank(item.getString("org1")) &&	item.getString("org1").equals(org_item.getString("org1"))
	   					) {
	   						SysOrgUserEntity sysOrgUserEntity = new  SysOrgUserEntity();
	   						sysOrgUserEntity.setOrgId(org_item.getString("org_id"));
	   						sysOrgUserEntity.setOrgids(org_item.getString("orgids"));
	   						user.setOrg(sysOrgUserEntity);
	   					}
	   				}
				}
	   			user.setPassword("123456");
	   			user.setRoleIdList(arrayList);
	   			userSevrice.saveUser(user);
			}
	}
	
	
	
	

	
	
	
	
	
}
