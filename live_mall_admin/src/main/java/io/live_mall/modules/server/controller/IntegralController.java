package io.live_mall.modules.server.controller;

import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.IntegralActivityEntity;
import io.live_mall.modules.server.entity.IntegralEntity;
import io.live_mall.modules.server.service.IntegralActivityService;
import io.live_mall.modules.server.service.IntegralService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/1/30 16:43
 * @description 积分管理
 */
@RestController
@RequestMapping("integral")
@AllArgsConstructor
public class IntegralController {

    private final IntegralService integralService;
    private final IntegralActivityService integralActivityService;

    /**
     * 积分管理列表
     * @param params
     * @return
     */
    @GetMapping(value = "list")
    public R list(@RequestParam Map<String, Object> params) {
        return R.ok().put("data", integralService.integralPages(params));
    }


    /**
     * 积分保存
     * @param integral 积分对象
     * @return
     */
    @PostMapping(value = "/save")
    public R save(@RequestBody IntegralEntity integral) {
        integral.setDelFlag(0);
        integral.setCreateTime(new Date());
        boolean save = integralService.save(integral);
        return save ? R.ok() : R.error();
    }

    /**
     * 积分活动列表
     * @param params
     * @return
     */
    @GetMapping(value = "/activity-list")
    public R integralActivityList(@RequestParam Map<String, Object> params) {
        return R.ok().put("data", integralActivityService.integralActivityPages(params));
    }

    /**
     * 积分活动保存
     * @param integralActivity
     * @return
     */
    @PostMapping(value = "/activity-save")
    public R integralActivityASave(@RequestBody IntegralActivityEntity integralActivity) {
        integralActivity.setDelFlag(0);
        integralActivity.setCreateTime(new Date());
        boolean save = integralActivityService.save(integralActivity);
        return save ? R.ok() : R.error();
    }

}
