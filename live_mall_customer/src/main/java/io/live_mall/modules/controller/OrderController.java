package io.live_mall.modules.controller;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.model.CustomerUserModel;
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
     * 产品详情
     * @param orderId 订单id
     * @return
     */
    @GetMapping(value = "/product-info")
    public R info(@RequestParam String orderId) {
        JSONObject productInfo = orderService.productInfo(orderId);
        return R.ok().put("data", productInfo);
    }
}
