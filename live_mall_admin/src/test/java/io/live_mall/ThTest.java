package io.live_mall;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.hutool.core.date.DateUtil;
import io.live_mall.modules.server.entity.MemberEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.ProductUnitEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.MemberService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.ProductUnitService;
import io.live_mall.modules.server.service.RaiseService;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ThTest {
	/**
	 * 1、按季付息（以成立日作为付息日），即以（成立日后每3个月）作为付息日。比如采用该规则一个产品成立日（也是起息日）是2019年10月12日，该产品期限是12个月，则付息日分别为：2020年1月12日、2020年4月12日，2020年7月12日，2020年10月12日，2021年1月12日。产品到期日（也就是2021年1月12日）兑付最后一次预期收益（如有）及本金。
2、按季付息（以每季末月固定日作为起息日），即以每年3、6、9、12月的指定日期作为付息日。如采用该规则一个产品成立日是2020/5/8，期限是12个月，设置的日期是23日，则产品到期日是2021/5/8，付息日分别是2020/6/23，2020/9/23，2020/12/23，2021/3/23。
再举一个例子，该规则下，还有一个产品成立日2020/6/5，期限是12个月，设置的日期是23日，则产品到期日是2021/6/5，则付息日是2020/9/23，2020/12/23，2021/3/23。（注意，这种情况下只付息三次）
3、按半年付息（以成立日作为付息日），即以成立后每半年作为付息日，如一个产品成立日是2019年10月12日，该产品期限是24个月，则付息日分别为：2020年4月12日，2020年10月12日，2021年4月12日，2021年10月12日
4、按半年付息（以半年末月固定日作为付息日），即以每年6、12月的指定日期作为付息日。如采用该规则一个产品成立日是2020/5/8，期限是24个月，设置的日期是23日，则产品到期日是2022/5/8，付息日分别是2020/6/23， 2020/12/23，2021/6/23， 2021/12/23
再举一个例子，该规则下，还有一个产品成立日2020/6/5，期限是24个月，设置的日期是23日，则产品到期日是2022/6/5，则付息日是2020/12/23，2021/6/23，2021/12/23。
	 * @param args
	 */
	public static void main(String[] args) {
		/*//1、按季付息（以成立日作为付息日），即以（成立日后每3个月）作为付息日。比如采用该规则一个产品成立日（也是起息日）是2019年10月12日，该产品期限是12个月，则付息日分别为：2020年1月12日、2020年4月12日，2020年7月12日，2020年10月12日，2021年1月12日。产品到期日（也就是2021年1月12日）兑付最后一次预期收益（如有）及本金。
		//得到成立日期
		List<String> clrz = getClrz(new Date(), 10,3,null);
		//2、按季付息（以每季末月固定日作为起息日），即以每年3、6、9、12月的指定日期作为付息日。如采用该规则一个产品成立日是2020/5/8，期限是12个月，设置的日期是23日，则产品到期日是2021/5/8，付息日分别是2020/6/23，2020/9/23，2020/12/23，2021/3/23。
		//再举一个例子，该规则下，还有一个产品成立日2020/6/5，期限是12个月，设置的日期是23日，则产品到期日是2021/6/5，则付息日是2020/9/23，2020/12/23，2021/3/23。（注意，这种情况下只付息三次）
		List<String> clrz2 = getClrz(new Date(), 10,3,10);
		//3、按半年付息（以成立日作为付息日），即以成立后每半年作为付息日，如一个产品成立日是2019年10月12日，该产品期限是24个月，则付息日分别为：2020年4月12日，2020年10月12日，2021年4月12日，2021年10月12日
		List<String> clrz3 = getClrz(new Date(), 24,6,null);
		//System.out.println(JSONArray.toJSONString(clrz3));
		//4、按半年付息（以半年末月固定日作为付息日），即以每年6、12月的指定日期作为付息日。如采用该规则一个产品成立日是2020/5/8，期限是24个月，设置的日期是23日，则产品到期日是2022/5/8，付息日分别是2020/6/23， 2020/12/23，2021/6/23， 2021/12/23
		//再举一个例子，该规则下，还有一个产品成立日2020/6/5，期限是24个月，设置的日期是23日，则产品到期日是2022/6/5，则付息日是2020/12/23，2021/6/23，2021/12/23。
		List<String> clrz4 = getClrz(new Date(), 24,6,11);
		System.out.println(JSONArray.toJSONString(clrz4));*/
		List<String> clrz = OrderUtils.getClrz("2",new Date(), 24,13);
		System.err.println(clrz);
	}
	
	@Autowired
	OrderService orderservice;
	
	@Autowired
	RaiseService raiseService;
	
	
	@Autowired
	ProductUnitService productUnitService;
	
	@Autowired
	ProductService productService;
	
	
	@Test
	public void testName() throws Exception {
		/*RaiseEntity newRaise = raiseService.getNewRaise("1348944602777866242");*/
		List<OrderEntity> list = orderservice.list();
		for (OrderEntity orderEntity : list) {
		try {
			String productUnitId = orderEntity.getProductUnitId();
			ProductEntity productEntity = productService.getById(orderEntity.getProductId());
			RaiseEntity raiseEntity = raiseService.getById(orderEntity.getRaiseId());
			Integer productTermType = productEntity.getProductTermType();
			ProductUnitEntity byId = productUnitService.getById(productUnitId);
			String incomeData = byId.getIncomeData();
			Double rate = OrderUtils.getRate(incomeData, orderEntity.getAppointMoney());
			if(rate !=null) {
				orderEntity.setRaseRate(new BigDecimal(rate));
			}
			Integer ys =12;
			String productTerm = productEntity.getProductTerm();
			if(productTermType !=4  || productTermType == 3) {
				ys = Integer.valueOf(productTerm.split(",")[0]);
				orderEntity.setDateUint(productTerm.split(",")[1]);
			}else if(productTermType == 3){
				ys = Integer.valueOf(productTerm.split(",")[1]);
				orderEntity.setDateUint(productTerm.split(",")[0]);
			}
			orderEntity.setDateNum(ys);
			orderEntity.setDateEnd(DateUtil.offsetMonth(raiseEntity.getEstablishTime(), ys));
			
			orderservice.updateById(orderEntity);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(orderEntity.getId());
		}
			
		}
	}
	
	@Autowired
	MemberService memberservice;
	
	
	@Test
	public void memberSysn() throws Exception {
		List<MemberEntity> list = memberservice.list();
		for (MemberEntity memberEntity : list) {
			memberservice.getMemberType(memberEntity);;
		}
	}

	
}
