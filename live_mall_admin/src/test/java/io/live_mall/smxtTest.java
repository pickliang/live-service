package io.live_mall;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.db.sql.Order;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import io.live_mall.modules.server.entity.ManagerUserEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.ProductUnitEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.entity.SysConfigEntity;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import io.live_mall.modules.server.service.ManagerUserService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.ProductUnitService;
import io.live_mall.modules.server.service.RaiseService;
import io.live_mall.modules.server.service.SysOrgGroupService;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysConfigService;
import io.live_mall.modules.sys.service.SysUserService;



@RunWith(SpringRunner.class)
@SpringBootTest
public class smxtTest {
	
	@Autowired
	SysConfigService sysConfigService;
	
	
	@Autowired
	RaiseService raiseService;
	@Autowired
	ProductService productService;
	@Autowired
	ProductUnitService productUnitService;
	
	@Autowired
	SysOrgGroupService orgserverice;
	
	@Autowired
	SysUserService userserice;
	
	
	@Autowired
	OrderService orderService;
	
	
	@Autowired
	ManagerUserService managerUserService;
	

	
	
	@Test
	public void testName() throws Exception {
		List<ProductEntity> list = productService.list(new QueryWrapper<ProductEntity>().in("status", "1","2"));
		for (ProductEntity productEntity : list) {
			productService.uptStatus(productEntity.getId());
		}
	}
	
}


