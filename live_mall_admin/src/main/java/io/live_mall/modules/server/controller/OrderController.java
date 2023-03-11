package io.live_mall.modules.server.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.youzan.cloud.open.sdk.core.oauth.model.OAuthToken;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.*;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.modules.oss.cloud.OSSFactory;
import io.live_mall.modules.oss.entity.SysOssEntity;
import io.live_mall.modules.oss.service.SysOssService;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.OrderBonusModel;
import io.live_mall.modules.server.model.OrderModel;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.*;
import io.live_mall.tripartite.TouchClients;
import io.live_mall.tripartite.YouZanClients;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final RaiseService raiseService;
    private final SysOssService sysOssService;
    private final OrderPayService orderPayService;
    private final RedisUtils redisUtils;
    private final OrderBonusService orderBonusService;
    private final OrderOutService orderOutService;
    private static Lock lock = new ReentrantLock();

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
        lock.lock();
        try {
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
        }catch (Exception e) {
            new RRException("系统异常");
        }finally {
            lock.unlock();
        }
        return R.ok().put("data",order.getId());
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:order:update")
    @SneakyThrows
    public R update(@RequestBody OrderEntity order){
        // 理财师小程序验证手机号正确性
        if (null != order.getSmscode()) {
            String val = redisUtils.get(RedisKeyConstants.MMS_PHONE_TYPE_EXPIRE + order.getPhone());
            if (!val.equals(String.valueOf(order.getSmscode()))) {
                return R.error("验证码错误");
            }
        }

        // 同步小鹅通用户信息
        String touchToken = redisUtils.get(RedisKeyConstants.TOUCH_ACCESS_TOKEN);
        if (Objects.isNull(touchToken)) {
            JSONObject data = TouchClients.getToken();
            if (null != data) {
                touchToken = data.getString("access_token");
                Integer expiresIn = data.getInteger("expires_in");
                redisUtils.set(RedisKeyConstants.TOUCH_ACCESS_TOKEN, touchToken, expiresIn);
            }
        }
        orderService.updateOrder(order,ShiroUtils.getUserEntity(), touchToken);

        // 有赞增加积分
        CompletableFuture.runAsync(() -> {
            try {
                String token = redisUtils.get(RedisKeyConstants.YZ_TOKEN);
                if (Objects.isNull(token)) {
                    OAuthToken authToken = YouZanClients.token();
                    redisUtils.set(RedisKeyConstants.YZ_TOKEN, authToken.getAccessToken(), authToken.getExpires());
                    token = authToken.getAccessToken();
                }
                orderService.addYouZanPoints(token, order.getId(), order.getUptType());
            } catch (Exception e) {
                log.error("有赞增加积分异常-->{}", e);
            }
        });


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

    /**
     * 发送付息通知列表
     * @param startDate 开始日期yyyy-MM-dd
     * @param endDate 结束日期yyyy-MM-dd
     * @return
     */
    @GetMapping(value = "/payment-notice-data")
    public R paymentNoticeData(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return R.ok().put("data", orderService.orderPayNoticeData(startDate, endDate));
    }

    /**
     * 股权订单
     * @param params
     * @return
     */
    @GetMapping(value = "/stock-right")
    public R stockRightOrders(@RequestParam Map<String, Object> params) {
        return R.ok().put("data", orderService.stockRightOrders(params));
    }

    /**
     * 分红列表
     * @param params
     * @return
     */
    @GetMapping(value = "/bonus")
    public R orderBonus(@RequestParam Map<String, Object> params) {
        PageUtils pages = orderBonusService.pages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 保存分红
     * @param entity
     * @return
     */
    @PostMapping(value = "/save-bonus")
    public R saveOrderBonus(@RequestBody OrderBonusEntity entity) {
        OrderEntity order = orderService.getById(entity.getOrderId());
        if (Objects.nonNull(order)) {
            entity.setCreateUser(ShiroUtils.getUserId());
            entity.setCreateTime(new Date());
            entity.setProductId(order.getProductId());
            entity.setCardNum(order.getCardNum());
            boolean save = orderBonusService.save(entity);
            return save ? R.ok() : R.error();
        }
       return R.error();
    }

    /**
     * 分红详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/bonus-info/{id}")
    public R orderBonusInfo(@PathVariable("id") String id) {
        OrderBonusEntity orderBonus = orderBonusService.getById(id);
        OrderBonusModel model = new OrderBonusModel();
        BeanUtils.copyProperties(orderBonus, model);
        return R.ok().put("data", model);
    }

    /**
     * 更新分红
     * @param model
     * @return
     */
    @PostMapping(value = "/update-bonus")
    public R updateOrderBonus(@RequestBody OrderBonusModel model) {
        OrderBonusEntity entity = new OrderBonusEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setUpdateUser(ShiroUtils.getUserId());
        entity.setUpdateTime(new Date());
        boolean b = orderBonusService.updateById(entity);
        return b ? R.ok() : R.error();
    }

    /**
     * 保存订单退出
     * @param entity
     * @return
     */
    @PostMapping(value = "/save-order-out")
    public R saveOrderOut(@RequestBody OrderOutEntity entity) {
        OrderEntity order = orderService.getById(entity.getOrderId());
        if (Objects.nonNull(order)) {
            entity.setCardNum(order.getCardNum());
            entity.setCreateUser(ShiroUtils.getUserId());
            entity.setCreateTime(new Date());
            boolean save = orderOutService.save(entity);
            return save ? R.ok() : R.error();
        }
        return R.error();
    }
}
