package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.model.OrderModel;
import io.live_mall.modules.sys.entity.SysUserEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单管理
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 22:19:57
 */
public interface OrderService extends IService<OrderEntity> {

	OrderModel getOneOrderModel(String orderId);

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 募集期总额
	 */
	Integer sumMoenyRaise(String id);
	/**
	 * 销售总额
	 * @param userId
	 * @return
	 */
	Integer sumMoenySaleId(Long userId);

	List<JSONObject> getOrderSuccess(Map<String, Object> params);


	Integer sumMoenyMember(String memberNo);


	/**
	 * 8、订单都已审核通过，且剩余额度为0时或者，募集期管理的封账按钮要可点击
	 * 
	 */
	Boolean getCouldFz(RaiseEntity raise);

	int updateOrder(OrderEntity order, SysUserEntity user);

	void success(RaiseEntity raise);

	PageUtils selectDuifuPage(Map<String, Object> params);
	
	PageUtils selectYjPage(Map<String, Object> params);

	JSONObject selectYjSum(Map<String, Object> params);

	PageUtils selectYongjin(Map<String, Object> params);

	JSONObject selectYongjinSum(Map<String, Object> params);

	List<JSONObject>  selectAllOrderByUserAndRaise(String raise);

	BigDecimal getSumMoneyByProductId(String porduct);

	List<OrderEntity> selectList(QueryWrapper<OrderEntity> orderByAsc);

//	add by lyg for sum appoint-money and count customer at 20220516
	JSONObject selectSumOrder(Map<String, Object> params);

	PageUtils customerDuifuPage(Map<String, Object> params, String cardNum);

	Map<String, Object> totalAssets(String cardNum);

	Map<String, Object> customerAssets(String cardNum);

	// JSONObject productInfo(String orderId);

	Map<String, Object> customerOrderInfo(String id);

	void batchIntegralOrder();
}

