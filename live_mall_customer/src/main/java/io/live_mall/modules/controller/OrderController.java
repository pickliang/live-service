package io.live_mall.modules.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.IntegralActivityEntity;
import io.live_mall.modules.server.entity.IntegralEntity;
import io.live_mall.modules.server.model.CustomerUserModel;
import io.live_mall.modules.server.service.CustomerUserIntegralItemService;
import io.live_mall.modules.server.service.IntegralActivityService;
import io.live_mall.modules.server.service.IntegralService;
import io.live_mall.modules.server.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

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
    /**
     * 订单
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
