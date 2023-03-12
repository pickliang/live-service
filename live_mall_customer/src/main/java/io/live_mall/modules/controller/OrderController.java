package io.live_mall.modules.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.BondOrderModel;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.service.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author yewl
 * @date 2022/12/20 14:13
 * @description
 */
@RestController
@RequestMapping("order/")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final IntegralService integralService;
    private final IntegralActivityService integralActivityService;
    private final CustomerUserIntegralItemService customerUserIntegralItemService;
    private final OrderBonusService orderBonusService;
    private final OrderOutService orderOutService;
    private final OrderPurchaseService orderPurchaseService;
    private final OrderRedeemService orderRedeemService;
    private final FundNoticeService fundNoticeService;
    /**
     * 固收订单
     * @param params
     * @return
     */
    @GetMapping(value = "/list")
    public R orders(@RequestParam Map<String, Object> params) {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        if (Objects.isNull(userEntity) || StringUtils.isBlank(userEntity.getCardNum())) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "请先绑定身份证");
        }
        PageUtils pageUtils = orderService.customerDuifuPage(params, userEntity.getCardNum());
        return R.ok().put("data", pageUtils);
    }

    /**
     * 我的资产
     * @return
     */
    @GetMapping(value = "/total-assets")
    public R totalAssets() {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        if (Objects.isNull(userEntity) || StringUtils.isBlank(userEntity.getCardNum())) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "请先绑定身份证");
        }
        Map<String, Object> result = orderService.totalAssets(userEntity.getCardNum());
        return R.ok().put("data", result);
    }

    /**
     * 个人中心总资产
     * @return R
     */
    @GetMapping(value = "/customer-assets")
    public R customerAssets() {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        Map<String, Object> result = orderService.customerAssets(userEntity.getCardNum());
        return R.ok().put("data", result);
    }

    /**
     * 订单和产品详情
     * @param id
     * @return
     */
    @GetMapping(value = "/info/{id}")
    public R info(@PathVariable("id") String id) {
        Map<String, Object> result = orderService.customerOrderInfo(id);
        return R.ok().put("data", result);
    }

    /**
     * 积分规则
     * @return
     */
    @GetMapping(value = "/integral-rule")
    public R integral() {
        Map<String, Object> result = Maps.newHashMap();
        // 默认积分
        IntegralEntity integralEntity = integralService.getOne(Wrappers.lambdaQuery(IntegralEntity.class).eq(IntegralEntity::getDelFlag, 0)
                .orderByDesc(IntegralEntity::getCreateTime).last("LIMIT 1"));
        Integer integral = Objects.nonNull(integralEntity) ? integralEntity.getIntegral() : 10;
        result.put("integral", integral);

        // 活动积分
        IntegralActivityEntity integralActivity = integralActivityService.getOne(Wrappers.lambdaQuery(IntegralActivityEntity.class)
                .eq(IntegralActivityEntity::getDelFlag, 0).orderByDesc(IntegralActivityEntity::getCreateTime).last("LIMIT 1"));
        Integer activityIntegral = 15;
        String date = "2023/01/01日--2023/03/31日";
        String integralProportion = "50%";
        if (Objects.nonNull(integralActivity)) {
            activityIntegral = integralActivity.getIntegral();
            date = DateUtils.format(integralActivity.getBeginDate(), "YYYY/MM/dd日") + "--" + DateUtils.format(integralActivity.getEndDate(), "YYYY/MM/dd日");
            integralProportion = integralActivity.getIntegralProportion() + "%";
        }
        result.put("activityIntegral", activityIntegral);
        result.put("date", date);
        result.put("integralProportion", integralProportion);
        return R.ok().put("data", result);
    }

    /**
     * 处理已绑定身份证用户的积分订单，增加用户积分
     * @return
     */
    @GetMapping(value = "/batch-integral-order")
    public R batchIntegralOrder() {
        orderService.batchIntegralOrder();
        return R.ok();
    }

    /**
     * 用户总积分
     * @return
     */
    @GetMapping(value = "/total-integral")
    public R integralTotal() {
        Long integralTotal = customerUserIntegralItemService.customerUserIntegralTotal(ShiroUtils.getUserId());
        return R.ok().put("data", integralTotal);
    }

    /**
     * 积分订单列表
     * @param params
     * @return
     */
    @GetMapping(value = "/integral-list")
    public R integralOrder(@RequestParam Map<String, Object> params) {
        PageUtils page = customerUserIntegralItemService.customerUserIntegral(params, ShiroUtils.getUserId());
        return R.ok().put("data", page);
    }

    /**
     * 股权订单列表
     * @param params
     * @return
     */
    @GetMapping(value = "/stock-right-list")
    public R stockRight(@RequestParam Map<String, Object> params) {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        PageUtils pageUtils = orderBonusService.customerPages(params, userEntity.getCardNum());
        return R.ok().put("data", pageUtils);
    }

    /**
     * 股权订单详情
     * @param id 订单id
     * @return
     */
    @GetMapping(value = "/stock-right-info/{id}")
    public R stockRightInfo(@PathVariable("id") String id) {
        Map<String, Object> result = Maps.newHashMap();
        List<Map<String, Object>> bonus = new ArrayList<>();
        List<String> appendix = new ArrayList<>();
        List<OrderBonusEntity> list = orderBonusService.list(Wrappers.lambdaQuery(OrderBonusEntity.class).eq(OrderBonusEntity::getOrderId, id));
        list.forEach(b -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put("date", b.getDate());
            map.put("money", b.getMoney());
            bonus.add(map);
            appendix.add(b.getAppendix());
        });
        OrderOutEntity orderOut = orderOutService.getOne(Wrappers.lambdaQuery(OrderOutEntity.class).eq(OrderOutEntity::getOrderId, id).last("LIMIT 1"));
        result.put("bonus", bonus);
        result.put("out", orderOut);
        result.put("appendix", appendix);
        return R.ok().put("data", result);
    }

    /**
     * 二级市场列表
     * @param params
     * @return
     */
    @GetMapping(value = "/bond-pages")
    public R bondPages(@RequestParam Map<String, Object> params) {
        CustomerUserModel userEntity = ShiroUtils.getUserEntity();
        PageUtils pages = orderService.customerBondPages(params, userEntity.getCardNum());
        return R.ok().put("data", pages);
    }

    /**
     * 二级市场订单详情
     * @param orderId
     * @return
     */
    @GetMapping(value = "/bond-info")
    public R bondInfo(@RequestParam String orderId) {
        OrderEntity order = orderService.getById(orderId);
        if (Objects.nonNull(order)) {
            Map<String, Object> result = Maps.newHashMap();
            List<String> appendix = new ArrayList<>();
            // 申购
            List<BondOrderModel> purchase = orderPurchaseService.customerBonds(orderId);
            purchase.forEach(p -> appendix.add(p.getAppendix()));
            // 分红
            List<BondOrderModel> bonus = orderBonusService.customerBonds(orderId);
            // 赎回
            List<BondOrderModel> redeem = orderRedeemService.customerBonds(orderId);
            redeem.forEach(r -> appendix.add(r.getAppendix()));
            // 基金公告
            List<FundNoticeEntity> fund = fundNoticeService.list(Wrappers.lambdaQuery(FundNoticeEntity.class)
                    .eq(FundNoticeEntity::getProductId, order.getProductId()).orderByDesc(FundNoticeEntity::getCreateTime));
            result.put("purchase", purchase);
            result.put("bonus", bonus);
            result.put("redeem", redeem);
            result.put("appendix", appendix);
            result.put("fund", fund);
            return R.ok().put("data", result);
        }
        return R.ok().put("data", null);

    }

    /**
     * 产品详情
     * @param orderId 订单id
     * @return
     */
    // @GetMapping(value = "/product-info")
    // public R info(@RequestParam String orderId) {
    //     JSONObject productInfo = orderService.productInfo(orderId);
    //     return R.ok().put("data", productInfo);
    // }
}
