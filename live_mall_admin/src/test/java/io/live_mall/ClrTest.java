package io.live_mall;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.live_mall.modules.oss.cloud.OSSFactory;
import io.live_mall.modules.oss.entity.SysOssEntity;
import io.live_mall.modules.oss.service.SysOssService;
import io.live_mall.modules.server.dao.OrderDao;
import io.live_mall.modules.server.entity.OrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ClrTest {
	
	@Autowired
	private SysOssService sysOssService;
	
	@Autowired
	private OrderDao orderService;
	
	@Test
	public  void parse() {
		List<OrderEntity> list = orderService.selectList(new QueryWrapper<OrderEntity>().like("card_photo_l", "wx-wudo.oss-cn-beijing.aliyuncs.com").orderByAsc("id"));
		for (OrderEntity orderEntity : list) {
			try {
				orderEntity.setCardPhotoL(saveImage( orderEntity.getCardPhotoL()));
				orderEntity.setCardPhotoR(saveImage( orderEntity.getCardPhotoR()));
				orderEntity.setOtherFile(saveImage( orderEntity.getOtherFile()));
				orderEntity.setBankCardBack(saveImage( orderEntity.getBankCardBack()));
				orderEntity.setBankCardFront(saveImage( orderEntity.getBankCardFront()));
				orderEntity.setPaymentDetail(saveImage( orderEntity.getPaymentDetail()));
				orderEntity.setAssetsPro(saveImage( orderEntity.getAssetsPro()));
				orderEntity.setUptBy("sys");
				orderService.updateById(orderEntity);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	
	private String saveImage(String cardPhotoL) {
		if(StringUtils.isBlank(cardPhotoL)  || ! cardPhotoL.contains("wx-wudo.oss-cn-beijing.aliyuncs.com") ) {
			return cardPhotoL;
		}
		JSONObject parseObject = JSONObject.parseObject(cardPhotoL);
		try {
			String name = parseObject.getString("name");
			if(StringUtils.isNotBlank(name) &&StringUtils.isNotBlank(parseObject.getString("url"))  && parseObject.getString("url").contains("wx-wudo.oss-cn-beijing.aliyuncs.com") ) {
				byte[] downloadBytes = HttpUtil.downloadBytes(parseObject.getString("url"));
				String suffix = name.substring(name.lastIndexOf("."));
				String url = OSSFactory.build().uploadSuffix(downloadBytes, suffix);
				//保存文件信息
				SysOssEntity ossEntity = new SysOssEntity();
				ossEntity.setUrl(url);
				ossEntity.setCreateDate(new Date());
				sysOssService.save(ossEntity);
				parseObject.put("url", url);
			}
			return parseObject.toJSONString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return parseObject.toJSONString();
		
		
	}
	
	
	
}
