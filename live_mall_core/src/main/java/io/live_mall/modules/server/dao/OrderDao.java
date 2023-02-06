package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.OrderPayEntity;
import io.live_mall.modules.server.model.OrderModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
	OrderModel getOneOrderModel(String orderId);

	IPage<OrderEntity> selectPageVo(IPage<OrderEntity> page, @Param("params")Map<String, Object> params);
	
	List<JSONObject> getOrderSuccess( @Param("params")Map<String, Object> params);
	
	IPage<JSONObject> selectDuifuPage(IPage<JSONObject> page, @Param("params")Map<String, Object> params);
	
	IPage<JSONObject> selectYjPage(IPage<JSONObject> page, @Param("params")Map<String, Object> params);
	
	JSONObject selectYjSum( @Param("params")Map<String, Object> params);
	
	IPage<JSONObject> selectYongjin(IPage<JSONObject> page, @Param("params")Map<String, Object> params);
	
	JSONObject selectYongjinSum( @Param("params")Map<String, Object> params);

	List<JSONObject>  selectAllOrderByUserAndRaise(String raise);

	BigDecimal getSumMoneyByProductId(String porduct);

	//	add by lyg for sum appoint-money and count customer at 20220516
    JSONObject selectSumOrder(@Param("params")Map<String, Object> params);

	/**
	 * 订单列表
	 * @param page 分页
	 * @param cardNum 证件号
	 * @param type 0-全部 1-固收 2-股权 3-二级市场
	 * @param isHistory 1-在投订单 2-历史订单
	 * @return IPage<JSONObject>
	 */
	IPage<JSONObject> customerDuifuPage(IPage<JSONObject> page, @Param("cardNum") String cardNum, @Param("type") Integer type, @Param("isHistory") Integer isHistory);

	/**
	 * 固收总资产
	 * @param cardNum 身份证号
	 * @param type  1-类固收(固收) 2-权益类(股权) 3-净值型(二级市场)
	 * @return Double
	 */
	Double fixedIncome(@Param("cardNum") String cardNum, @Param("type") Integer type);

	/**
	 * 所有已付利息总和
	 * @param cardNum
	 * @return
	 */
	Double totalInterest(String cardNum);

	/**
	 * 历史固收总资产
	 * @param cardNum 身份证号
	 * @param type  1-类固收(固收) 2-权益类(股权) 3-净值型(二级市场)
	 * @return Double
	 */
	Double historyFixedIncome(@Param("cardNum") String cardNum, @Param("type") Integer type);

	/**
	 * 查询2条固收数据 废弃
	 * @param cardNum 身份证号
	 * @return
	 */
	List<JSONObject> customerDuifuLimit(String cardNum);

	OrderPayEntity nextOrder(String orderId);

	/**
	 * 个人中心总资产
	 * @param cardNum 证件号
	 * @return Double
	 */
	Double customerTotalAssets(String cardNum);

	/**
	 * 本年度收益
	 * @param cardNum 证件号
	 * @return Double
	 */
	Double annualIncome(String cardNum);

	/**
	 * 历史数据本年度收益
	 * @param cardNum 证件号
	 * @return Double
	 */
	Double historyAnnualIncome(String cardNum);

	/**
	 * 未来预期收益
	 * @param cardNum 证件号
	 * @return Double
	 */
	Double expectedIncome(String cardNum);
	/**
	 * 历史数据未来预期收益
	 * @param cardNum 证件号
	 * @return Double
	 */
	Double historyExpectedIncome(String cardNum);

	/**
	 * 订单号
	 * @param cardNum 身份证号
	 * @param type 1-在投订单 2-历史订单
	 * @return List<String>
	 */
	List<String> cutsomerOrderIds(@Param("cardNum") String cardNum, @Param("type") Integer type);

	/**
	 * 历史数据订单
	 * @param cardNum 身份证号
	 * @param type 1-在投订单 2-历史订单
	 * @return List<String>
	 */
	List<String> customerHistoryDuifuIds(@Param("cardNum") String cardNum, @Param("type") Integer type);

	/**
	 * 在投订单金额
	 * @param cardNum 身份证号
	 * @return Double
	 */
	Double investingOrderMoney(String cardNum);
	/**
	 * 历史在投订单金额
	 * @param cardNum 身份证号
	 * @return Double
	 */
	Double historyInvestingOrderMoney(String cardNum);

	JSONObject customerOrderInfo(String id);

	/**
	 * 积分订单
	 * @param cardNums 身份证号
	 * @return
	 */
	List<JSONObject> integralOrder(@Param("cardNums") List<String> cardNums);

	IPage<JSONObject> historyDuifuPage(IPage<JSONObject> page, @Param("params")Map<String, Object> params);
}
