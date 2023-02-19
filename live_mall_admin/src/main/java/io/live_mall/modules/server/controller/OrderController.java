package io.live_mall.modules.server.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.oss.cloud.OSSFactory;
import io.live_mall.modules.oss.entity.SysOssEntity;
import io.live_mall.modules.oss.service.SysOssService;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.OrderPayEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.model.OrderModel;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.OrderPayService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.RaiseService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 订单管理
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 20:17:06
 */
@RestController
@RequestMapping("server/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final RaiseService raiseService;
    private final SysOssService sysOssService;
    private final OrderPayService orderPayService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:order:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);
        return R.ok().put("page", page);
    }
  
    @RequestMapping("/selectDuifuPage")
    public R selectDuifuPage(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.selectDuifuPage(params);
        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/selectYjPage")
    public R selectYjPage(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.selectYjPage(params);
        return R.ok().put("page", page);
    }
    
    @RequestMapping("/selectYjSum")
    public R selectYjSum(@RequestParam Map<String, Object> params){
        return R.ok().put("data", orderService.selectYjSum(params));
    }
    
    
    @RequestMapping("/selectYongjin")
    public R selectYongjin(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.selectYongjin(params);
        return R.ok().put("page", page);
    }
    
    @RequestMapping("/selectYongjinSum")
    public R selectYongjinSum(@RequestParam Map<String, Object> params){
        return R.ok().put("data", orderService.selectYongjinSum(params));
    }

    //	add by lyg for sum appoint-money and count customer at 20220516
    @RequestMapping("/selectSumOrder")
    public R selectSumOrder(@RequestParam Map<String, Object> params){
        return R.ok().put("data",orderService.selectSumOrder(params));
    }


    @GetMapping("/sysOrderFile")
    public R sysOrderFile() throws InterruptedException{
    	List<OrderEntity> list = orderService.selectList(new QueryWrapper<OrderEntity>().like("card_photo_l", "wx-wudo.oss-cn-beijing.aliyuncs.com").orderByAsc("id"));
		for (OrderEntity orderEntity : list) {
			try {
				orderEntity.setCardPhotoL(saveImage( orderEntity.getCardPhotoL()));
				orderEntity.setCardPhotoR(saveImage( orderEntity.getCardPhotoR()));
				orderEntity.setOtherFile(saveImage( orderEntity.getOtherFile()));
				orderEntity.setBankCardBack(saveImage( orderEntity.getBankCardBack()));
				orderEntity.setBankCardFront(saveImage( orderEntity.getBankCardFront()));
				orderEntity.setPaymentSlip(saveImage( orderEntity.getPaymentSlip()));
				orderEntity.setAssetsPro(saveImage( orderEntity.getAssetsPro()));
				orderEntity.setUptBy("sys");
				orderService.updateById(orderEntity);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Thread.sleep(20000L);
		}
        return R.ok().put("data", "");
    }
    
    
	private String saveImage(String cardPhotoL) {
		if(StringUtils.isBlank(cardPhotoL)  || ! cardPhotoL.contains("wx-wudo.oss-cn-beijing.aliyuncs.com") ) {
			return cardPhotoL;
		}
		try {
			JSONObject parseObject = JSONObject.parseObject(cardPhotoL);
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
		return cardPhotoL;
	}
    
    
    @RequestMapping("/getOrderSuccess")
    @RequiresPermissions("server:order:list")
    public R getOrderSuccess(@RequestParam Map<String, Object> params){
       List<JSONObject> list= orderService.getOrderSuccess(params);
        return R.ok().put("data", list);
    }
    
    @RequestMapping("/getOrderCurrentUserList")
    @RequiresPermissions("server:order:list")
    public R getOrderCurrentUserList(@RequestBody JSONObject params){
    	params.put("saleId", ShiroUtils.getUserId());
    	PageUtils page = orderService.queryPage(params);
        return R.ok().put("data",page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:order:info")
    public R info(@PathVariable("id") String id){
		OrderEntity order = orderService.getById(id);
        return R.ok().put("order", order);
    }
    
    
    @RequestMapping("getOneOrderModel")
    @RequiresPermissions("server:order:info")
    public R getOneOrderModel(@RequestParam String orderId){
		OrderModel order = orderService.getOneOrderModel(orderId);
        return R.ok().put("data", order);
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:order:save")
    public R save(@RequestBody OrderEntity order){
    	//预约时间验证
    	String raiseId = order.getRaiseId();
    	raiseService.canCreateOrder(raiseId,order.getAppointMoney());
    	order.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	order.setCreateDate(new Date());
    	order.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	order.setUptDate(new Date());
    	order.setOrderNo("OD"+DateUtils.getOrderNo());
		orderService.save(order);
        return R.ok();
    }
    
    @RequestMapping("/saveApp")
    @RequiresPermissions("server:order:save")
    public R saveApp(@RequestBody OrderEntity order){
    	RaiseEntity raiseEntity = raiseService.getById(order.getRaiseId());
    	raiseService.canCreateOrder(order.getRaiseId(),order.getAppointMoney());
    	order.setProductId(raiseEntity.getProductId());
    	order.setProductUnitId(raiseEntity.getProductUnitId());
    	order.setAppointTime(new Date());
    	order.setStatus(-1);
    	order.setSaleId(ShiroUtils.getUserId());
    	if(ShiroUtils.getUserEntity().getOrg().getOrgId() == null ){
    		throw new RRException("该业务员未绑定组织,请绑定后重新登录在创建订单");
    	}
    	order.setOrgId(ShiroUtils.getUserEntity().getOrg().getOrgId());
    	order.setReasourse("APP");
    	order.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	order.setCreateDate(new Date());
    	order.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	order.setUptDate(new Date());
    	order.setOrderNo("OD"+DateUtils.getOrderNo());
    	OrderUtils.orderStup(raiseEntity, order, -1,"");
		orderService.save(order);
        return R.ok().put("data",order.getId());
    }
    

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:order:update")
    public R update(@RequestBody OrderEntity order){
    	 orderService.updateOrder(order,ShiroUtils.getUserEntity());
		return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:order:delete")
    public R delete(@RequestBody String[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 订单付息详情
     * @param orderId 订单id
     * @return
     */
    @GetMapping(value = "/pay-list")
    @RequiresPermissions("server:order:list")
    public R orderPayList(@RequestParam String orderId) {
        List<OrderPayEntity> list = orderPayService.list(Wrappers.lambdaQuery(OrderPayEntity.class).eq(OrderPayEntity::getOrderId, orderId).orderByAsc(OrderPayEntity::getNum));
        return R.ok().put("data", list);
    }

    /**
     * 更新付息日
     * @param params
     * @return
     */
    @PostMapping(value = "/update-pay")
    public R updateOrderPay(@RequestBody List<JSONObject> params) {
        orderPayService.updatePayDate(params);
        return R.ok();
    }

    /**
     * 历史兑付列表
     * @param params
     * @return
     */
    @GetMapping(value = "/history-duifu-list")
    public R historyDuifuPage(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = orderService.historyDuifuPage(params);
        return R.ok().put("data", pageUtils);
    }

    /**
     * 发送兑付完成通知
     * @param startDate 开始日期yyyy-MM-dd
     * @param endDate 结束日期yyyy-MM-dd
     * @return
     */
    @GetMapping(value = "/duifu-notice-data")
    public R duifuNoticeData(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        return R.ok().put("data", orderService.duifuNoticeData(startDate, endDate));
    }

}
