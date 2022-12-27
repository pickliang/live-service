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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.common.utils.RedisUtils;
import io.live_mall.modules.server.entity.ManagerUserEntity;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.SysOrgUserEntity;
import io.live_mall.modules.server.service.ManagerUserService;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.server.utils.excel.ExcelParam;
import io.live_mall.modules.server.utils.excel.ExcelUtil;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportProdcut {
	@Autowired
	private RedisUtils redisUtils;
	
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	SysUserService userSevrice;
	
	@Autowired
	SysOrgGroupService sysOrgGroupService;
	
	
	@Autowired
	ProductService  productService;
	
	@Autowired
	ManagerUserService  managerUserService;
	
	
	@Test
	public void contextLoads() throws Exception {
		
			//name	short_name	label	product_code	founded_date	liquidation_date	currency_type	
			//raising_amt	investment_field	risk_level	investor_num_limit	create_user_name	create_time	
		    //financier	name	trustee	investment_style	term	term_remark	investment_subject	investment_targets
//repayment_source	risk_control	highlights	financier_introduction	sponsor_introduction	consultant	consultant_introduction	
		//director	director_introduction	portfolio	net_regular	product_introduction	sms_introduction	contract	prospectus	
		//publicity_table	other_attachment	raise_bank	raise_account_name	raise_account	raise_remark	buy_start	increase_amt	subscription_fee	subscription_desc	other_fee
		//open_day	open_day_remark	create_user_name	create_time
			String excel_conf="name_all:productName,short_name:productBrif,label:productLabel,product_code:id,founded_date:productDate,liquidation_date:endDate,"
					+ "currency_type:currencyType,raising_amt:productScale,investment_field:investmentField,risk_level:riskLevel,investor_num_limit:peopleLimit,"
					+ "financier:financingParty,name:managerPeople,trustee:trustee,investment_style:investmentMode,investment_subject:investmentTarget,investment_targets:useFound,"
					+ "repayment_source:sourcePay,risk_control:riskMeasures,highlights:productHighlights,financier_introduction:introductionDetail,"
					+ "sponsor_introduction:guarantorDetail,consultant:investmentConsultant,consultant_introduction:consultantDetail,director:fundManager,"
					+ "director_introduction:managerDetail,net_regular:frequency,product_introduction:productEdition,sms_introduction:noteEdition,contract:electroniceContract,"
					+ "prospectus:instructions,publicity_table:formualTable,other_attachment:otherData,raise_bank:raiseBank,raise_account_name:raiseCount,"
					+ "raise_account:raiseAccount,raise_remark:payRemark,buy_start:startingPoint,increase_amt:incrementalAmount,subscription_fee:buyRate,subscription_desc:rateDetail,"
					+ "other_fee:otherRate";
            ExcelParam param = new ExcelParam();
	   		param.setMap(ExcelUtil.getMap(excel_conf));
	   		param.setClassPath("com.alibaba.fastjson.JSONObject");
	   		param.setFile(new File("C:\\Users\\daitao\\Desktop\\1产品数据.xlsx"));
	   		param.setRowNumIndex(1);
	   		param.setSheetIndex(1);
	   		JSONArray all = ExcelUtil.readXlsPart(param);
	   		
	   		List<ProductEntity>  prodcutAll=new ArrayList<ProductEntity>();	   		
	   		for (Object object : all) {
	   			JSONObject item = (JSONObject)object;
	   			ProductEntity productEntity = item.toJavaObject(ProductEntity.class);
	   			if("人民币".equals(productEntity.getCurrencyType())) {
	   				productEntity.setCurrencyType("¥");
	   			}else {
	   				productEntity.setCurrencyType("$");
	   			}
	   			productEntity.setOnetype("私募基金");
	   			//深圳长城汇理资产管理有限公司
	   			String managerPeople = productEntity.getManagerPeople();
	   			if(StringUtils.isNotBlank(managerPeople)) {
	   				ManagerUserEntity managerUser = managerUserService.getOne(new QueryWrapper<ManagerUserEntity>().eq("custodian_name", managerPeople));
		   			if(managerUser !=null) {
		   				productEntity.setManagerPeople("[\""+managerUser.getId()+"\"]");
		   			}
	   			}
	   			if(StringUtils.isNotBlank(productEntity.getProductLabel())) {
	   				productEntity.setProductLabel("[\""+productEntity.getProductLabel()+"\"]");
	   			}
	   			prodcutAll.add(productEntity);
			}
	   		productService.saveBatch(prodcutAll);
	   		
	}
	
	
	
	

	
	
	
	
	
}
