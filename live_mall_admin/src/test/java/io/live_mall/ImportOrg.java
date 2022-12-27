package io.live_mall;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.server.service.YjViewService;
import io.live_mall.modules.server.utils.excel.ExcelParam;
import io.live_mall.modules.server.utils.excel.ExcelUtil;
import io.live_mall.modules.sys.controller.SysUserController;
import io.live_mall.modules.sys.service.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportOrg {
	@Autowired
	private RedisUtils redisUtils;
	
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	SysOrgGroupService orgSever;
	
	@Autowired
	SysUserService userservice;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	YjViewService yjViewService;
	
	@Test
	public void contextLoads() throws Exception {
			// 用uuid作为文件名，防止生成的临时文件重复
			//序号	一级组织机构	二级组织机构	三级组织机构	四级组织机构
			String excel_conf="一级组织机构:org1,二级组织机构:org2,三级组织机构:org3,四级组织机构:org4";
            ExcelParam param = new ExcelParam();
	   		param.setMap(ExcelUtil.getMap(excel_conf));
	   		param.setClassPath("com.alibaba.fastjson.JSONObject");
	   		param.setFile(new File("F:\\wxchat\\WeChat Files\\daitao25\\FileStorage\\File\\2020-11\\五道集团-组织人员.xlsx"));
	   		param.setRowNumIndex(1);
	   		param.setSheetIndex(1);
	   		JSONArray all = ExcelUtil.readXlsPart(param);
	   		//[{"org1":"五道集团","org2":"五道商学院"},{"org1":"五道集团","org2":"五道管理部"},{"org1":"五道集团","org2":"人力行政中心","org3":"人事行政部"},{"org1":"五道集团","org2":"人力行政中心","org3":"招聘部"},{"org1":"五道集团","org2":"海外家族信托办公室"},{"org1":"五道集团","org2":"品牌市场中心"},{"org1":"五道集团","org2":"法务合规中心"},{"org1":"五道集团","org2":"营销管理中心"},{"org1":"五道集团","org2":"财务中心"},{"org1":"五道集团","org2":"运营管理中心","org3":"销售运营支持部"},{"org1":"五道集团","org2":"风控中心"},{"org1":"五道集团","org2":"创新业务部"},{"org1":"五道集团","org2":"董事会办公室"},{"org1":"五道集团","org2":"总裁办"},{"org1":"五道集团","org2":"战略发展"},{"org1":"五道集团","org2":"其他"},{"org1":"五道集团","org2":"股权投资部"},{"org1":"五道集团","org2":"渠道经理"},{"org1":"五道集团","org2":"健康管理部"},{"org1":"五道集团","org2":"大客户部"},{"org1":"五道集团","org2":"机构业务部一部"},{"org1":"五道集团","org2":"投中投后中心"},{"org1":"五道集团","org2":"测试部门"},{"org4":"team2","org1":"销售组织","org2":"上海区域","org3":"上海第二分公司"},{"org4":"Team1","org1":"销售组织","org2":"上海区域","org3":"上海第二分公司"},{"org4":"上海区域运营支持","org1":"销售组织","org2":"上海区域","org3":"上海区域管理中心"},{"org4":"Team1","org1":"销售组织","org2":"上海区域","org3":"上海张丽团队"},{"org4":"Team1","org1":"销售组织","org2":"上海区域","org3":"上海第一分公司"},{"org4":"Team4","org1":"销售组织","org2":"北京区域","org3":"北京第一分公司"},{"org4":"Team5","org1":"销售组织","org2":"北京区域","org3":"北京第一分公司"},{"org4":"Team3","org1":"销售组织","org2":"北京区域","org3":"北京第一分公司"},{"org4":"Team2","org1":"销售组织","org2":"北京区域","org3":"北京第一分公司"},{"org4":"Team6","org1":"销售组织","org2":"北京区域","org3":"北京第一分公司"},{"org4":"Team3","org1":"销售组织","org2":"北京区域","org3":"北京第四分公司"},{"org4":"Team2","org1":"销售组织","org2":"北京区域","org3":"北京第四分公司"},{"org4":"Team1","org1":"销售组织","org2":"北京区域","org3":"北京第四分公司"},{"org4":"北京区域人事行政部","org1":"销售组织","org2":"北京区域","org3":"北京区域管理中心"},{"org4":"Team1","org1":"销售组织","org2":"北京区域","org3":"北京第二分公司"},{"org1":"销售组织","org2":"山东区域","org3":"潍坊第一分公司"},{"org4":"Team1","org1":"销售组织","org2":"山东区域","org3":"东营第一分公司"},{"org4":"Team2","org1":"销售组织","org2":"山东区域","org3":"济南第一分公司"},{"org4":"Team3","org1":"销售组织","org2":"山东区域","org3":"济南第一分公司"},{"org4":"Team1","org1":"销售组织","org2":"山东区域","org3":"济南第一分公司"},{"org1":"销售组织","org2":"山东区域","org3":"滨州第一分公司"},{"org1":"销售组织","org2":"山东区域","org3":"山东区域管理中心"},{"org4":"Team1","org1":"销售组织","org2":"南京区域","org3":"南京第一分公司"},{"org1":"销售组织","org2":"北京二区","org3":"北京第九分公司"},{"org4":"Team1","org1":"销售组织","org2":"无锡区域","org3":"无锡第一分公司"}]
			SysOrgGroupEntity sysOrgGroupEntity = new SysOrgGroupEntity();
			//sysOrgGroupEntity.setOrganizationName("五道集团");
			sysOrgGroupEntity.setOrganizationName("销售组织");
			sysOrgGroupEntity.setId(IdUtil.objectId());
			sysOrgGroupEntity.setLevel(1);
			for (Object object : all) {
				JSONObject item = 	(JSONObject)object;
				if(sysOrgGroupEntity.getOrganizationName().equals(item.getString("org1"))) {
					getchildren2(item, sysOrgGroupEntity,"org2");
				}
			}
			
			for (Object object : all) {
				JSONObject item = 	(JSONObject)object;
				getchildren3(item, sysOrgGroupEntity,"org3");
			}
			
			for (Object object : all) {
				JSONObject item = 	(JSONObject)object;
				getchildren4(item, sysOrgGroupEntity,"org4");
			}
			orgSever.save(sysOrgGroupEntity);
			savechild(sysOrgGroupEntity);
	   		
	}
	
	
	public void savechild(SysOrgGroupEntity sysOrgGroupEntity) {
		List<SysOrgGroupEntity> childrenList = sysOrgGroupEntity.getChildrenList();
		for (SysOrgGroupEntity sysOrgGroupEntity2 : childrenList) {
			orgSever.save(sysOrgGroupEntity2);
			savechild(sysOrgGroupEntity2);
		}
	}
	
	
	
	private void getchildren4(JSONObject item,SysOrgGroupEntity sysOrgGroupEntity,String levf) {
		List<SysOrgGroupEntity> childrenList = sysOrgGroupEntity.getChildrenList();
		for (SysOrgGroupEntity object2 : childrenList) {
			List<SysOrgGroupEntity> childrenList2 = object2.getChildrenList();
			for (SysOrgGroupEntity sysOrgGroupEntity2 : childrenList2) {
				boolean flag=true;
				List<SysOrgGroupEntity> childrenList3 = sysOrgGroupEntity2.getChildrenList();
				for (SysOrgGroupEntity sysOrgGroupEntity3 : childrenList3) {
					if(StringUtils.isNotBlank(item.getString(levf)) && sysOrgGroupEntity3.getOrganizationName().equals(item.getString(levf))) {
						flag=false;
					}
				}
				if(flag  && StringUtils.isNotBlank(item.getString(levf))  && object2.getOrganizationName().equals(item.getString("org2"))  && sysOrgGroupEntity2.getOrganizationName().equals(item.getString("org3"))) {
					SysOrgGroupEntity orgGroupEntity = new SysOrgGroupEntity();
					orgGroupEntity.setOrganizationName(item.getString(levf));
					orgGroupEntity.setId(IdUtil.objectId());
					orgGroupEntity.setParentId(sysOrgGroupEntity2.getId());
					orgGroupEntity.setLevel(4);
					childrenList3.add(orgGroupEntity);
				}
				
			}
			
		}
	}
	
	private void getchildren3(JSONObject item,SysOrgGroupEntity sysOrgGroupEntity,String levf) {
		List<SysOrgGroupEntity> childrenList = sysOrgGroupEntity.getChildrenList();
		for (SysOrgGroupEntity object2 : childrenList) {
			List<SysOrgGroupEntity> childrenList2 = object2.getChildrenList();
			boolean flag=true;
			for (SysOrgGroupEntity sysOrgGroupEntity2 : childrenList2) {
				if(StringUtils.isNotBlank(item.getString(levf)) && sysOrgGroupEntity2.getOrganizationName().equals(item.getString(levf))) {
					flag=false;
				}
			}
			if(flag && StringUtils.isNotBlank(item.getString(levf)) && object2.getOrganizationName().equals(item.getString("org2"))) {
				SysOrgGroupEntity orgGroupEntity = new SysOrgGroupEntity();
				orgGroupEntity.setOrganizationName(item.getString(levf));
				orgGroupEntity.setId(IdUtil.objectId());
				orgGroupEntity.setParentId(object2.getId());
				orgGroupEntity.setLevel(3);
		   		childrenList2.add(orgGroupEntity);
		   		
			}
		}
	}
	
	
	
	
	
	private void getchildren2(JSONObject item,SysOrgGroupEntity sysOrgGroupEntity,String levf) {
		List<SysOrgGroupEntity> childrenList = sysOrgGroupEntity.getChildrenList();
		boolean flag=true;
		for (SysOrgGroupEntity object2 : childrenList) {
			if(object2.getOrganizationName().equals(item.getString(levf))) {
				flag=false;
			}
		}
		if(flag) {
			SysOrgGroupEntity orgGroupEntity = new SysOrgGroupEntity();
			orgGroupEntity.setOrganizationName(item.getString(levf));
			orgGroupEntity.setId(IdUtil.objectId());
			orgGroupEntity.setLevel(2);
			orgGroupEntity.setParentId(sysOrgGroupEntity.getId());
			childrenList.add(orgGroupEntity);
		}
	}
	
	@Test
    public void fe_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\user_import.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("limit", 9999);
        List<?> list = userservice.queryPage(hashMap).getList();
        List<JSONObject> listMap = new ArrayList<JSONObject>();
        for (Object object : list) {
        	listMap.add(JSONObject.parseObject(JSONObject.toJSONString(object)));
		}
        map.put("list",listMap );
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\人员信息.xlsx");
        workbook.write(fos);
        fos.close();
    }
	
	//业绩名单
	@Test
    public void yj_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\yj.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("limit", 10);
        List<?> list = orderService.selectYjPage(hashMap).getList();
        List<JSONObject> listMap = new ArrayList<JSONObject>();
        for (Object object : list) {
        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
        	parseObject.put("date_num", parseObject.getString("date_num")+parseObject.getString("date_uint"));
        	parseObject.put("zbyj",parseObject.get("zbyj") +"万");
        	parseObject.put("zhyj",parseObject.get("zhyj") +"万");
        	parseObject.put("nhyj",parseObject.get("nhyj") +"万");
        	parseObject.put("nhyjcw",parseObject.get("nhyjcw") +"万");
        	listMap.add(parseObject);
		}
        map.put("list",listMap );
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\业绩信息.xlsx");
        workbook.write(fos);
        fos.close();
    }
	//、佣金名单、
	
	@Test
    public void yongjin_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\yongjin.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("limit", 10);
        List<?> list = orderService.selectYongjin(hashMap).getList();
        List<JSONObject> listMap = new ArrayList<JSONObject>();
        for (Object object : list) {
        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
        	parseObject.put("date_num", parseObject.getString("date_num")+parseObject.getString("date_uint"));
        	parseObject.put("zbyj",parseObject.get("zbyj") +"万");
        	parseObject.put("zhyj",parseObject.get("zhyj") +"万");
        	parseObject.put("nhyj",parseObject.get("nhyj") +"万");
        	if(parseObject.get("yongjin_rate") !=null) {
        		parseObject.put("yongjin_rate",parseObject.get("yongjin_rate") +"%");
        	}
        	parseObject.put("nhyjcw",parseObject.get("nhyjcw") +"万");
        	listMap.add(parseObject);
		}
        map.put("list",listMap );
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\佣金信息.xlsx");
        workbook.write(fos);
        fos.close();
    }
	
	//兑付名单
	
	@Test
    public void duifu_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\duifu.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("limit", 10);
        List<?> list = orderService.selectDuifuPage(hashMap).getList();
        List<JSONObject> listMap = new ArrayList<JSONObject>();
        for (Object object : list) {
        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
        	parseObject.put("date_num", parseObject.getString("date_num")+parseObject.getString("date_uint"));
        	if(parseObject.get("rate") !=null) {
        		parseObject.put("rate",parseObject.get("rate") +"%");
        	}
        	List<JSONObject> payList = parseObject.getJSONArray("orderPayList").toJavaList(JSONObject.class);
        	for (int i = 0; i < payList.size(); i++) {
        		parseObject.put("payDate"+(i+1), payList.get(i).get("payDate"));
        		parseObject.put("payMoney"+(i+1), payList.get(i).get("payMoney"));
			}
        	listMap.add(parseObject);
		}
        map.put("list",listMap );
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\兑付信息.xlsx");
        workbook.write(fos);
        fos.close();
    }
	//岗位津贴
	@Test
    public void gwjt_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\gwjt.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("limit", 10);
        List<?> list = yjViewService.queryPage(hashMap).getList();
        List<JSONObject> listMap = new ArrayList<JSONObject>();
        for (Object object : list) {
        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
        	parseObject.put("task", parseObject.getString("task")+"万");
        	parseObject.put("zbyj", parseObject.getString("zbyj")+"万");
        	parseObject.put("nhyj", parseObject.getString("nhyj")+"万");
        	if(parseObject.get("jqxs") !=null) {
        		parseObject.put("jqxs",parseObject.get("jqxs") +"%");
        	}
          	if(parseObject.get("wcl") !=null) {
        		parseObject.put("wcl",parseObject.get("wcl") +"%");
        	}
          	
          	if(parseObject.getInteger("level") == 4) {
        		parseObject.put("level","城市副总"+parseObject.getInteger("grade")+"级");
        	}else if(parseObject.getInteger("level") == 3) {
        		parseObject.put("level","城市总"+parseObject.getInteger("grade")+"级");
        	}else if(parseObject.getInteger("level") == 2) {
        		parseObject.put("level","区域总"+parseObject.getInteger("grade")+"级");
        	}
        	listMap.add(parseObject);
		}
        map.put("list",listMap );
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\岗位津贴信息.xlsx");
        workbook.write(fos);
        fos.close();
    }
	
	
	//成立名单详情、
	@Test
    public void raise_suc_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\raise_suc.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("raiseId", "1353587526496964610");
        List<?> list = 	orderService.getOrderSuccess(hashMap);;
        List<JSONObject> listMap = new ArrayList<JSONObject>();
        for (Object object : list) {
        	JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(object));
        	map.put("raiseName", parseObject.get("raiseName"));
        	if(parseObject.getDate("appointTime") !=null) {
        		parseObject.put("appointTime", DateUtil.formatDateTime(parseObject.getDate("appointTime")));
        	}
        	if(parseObject.getDate("aduitTime") !=null) {
        		parseObject.put("aduitTime", DateUtil.formatDateTime(parseObject.getDate("aduitTime")));
        	}
        	listMap.add(parseObject);
		}
        map.put("list",listMap );
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("E:\\work\\sxhl\\ipo\\five_way_ipo\\doc\\成立信息.xlsx");
        workbook.write(fos);
        fos.close();
    }
	
}
