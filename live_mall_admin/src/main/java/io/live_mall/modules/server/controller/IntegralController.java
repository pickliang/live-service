package io.live_mall.modules.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.IntegralActivityEntity;
import io.live_mall.modules.server.entity.IntegralEntity;
import io.live_mall.modules.server.service.IntegralActivityService;
import io.live_mall.modules.server.service.IntegralService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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
    public R save(@RequestBody @Valid IntegralEntity integral) {
        integral.setDelFlag(0);
        integral.setCreateTime(new Date());
        integralService.save(integral);

        Map<String, Object> result = Maps.newHashMap();
        // 积分设置
        result.put("integral", integral.getIntegral());
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
    public R integralActivityASave(@RequestBody @Valid IntegralActivityEntity integralActivity) {
        integralActivity.setDelFlag(0);
        integralActivity.setCreateTime(new Date());
        integralActivityService.save(integralActivity);

        Map<String, Object> result = Maps.newHashMap();
        // 默认积分
        IntegralEntity integralEntity = integralService.getOne(Wrappers.lambdaQuery(IntegralEntity.class).eq(IntegralEntity::getDelFlag, 0)
                .orderByDesc(IntegralEntity::getCreateTime).last("LIMIT 1"));
        Integer integral = Objects.nonNull(integralEntity) ? integralEntity.getIntegral() : 10;
        result.put("integral", integral);
        // 活动积分
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

}
