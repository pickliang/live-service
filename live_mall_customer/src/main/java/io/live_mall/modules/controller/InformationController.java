package io.live_mall.modules.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.ActivityEntity;
import io.live_mall.modules.server.entity.FinanceEntity;
import io.live_mall.modules.server.model.FinanceModel;
import io.live_mall.modules.server.model.InformationModel;
import io.live_mall.modules.server.service.ActivityService;
import io.live_mall.modules.server.service.FinanceService;
import io.live_mall.modules.server.service.InformationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yewl
 * @date 2023/1/5 10:36
 * @description
 */
@RestController
@RequestMapping("information")
@AllArgsConstructor
public class InformationController {
    private final FinanceService financeService;
    private final InformationService informationService;
    private final ActivityService activityService;

    /**
     * 公司动态文章
     * @return
     */
    @GetMapping(value = "/company-dynamics")
    public R companyDynamics() {
        Map<String, Object> result = Maps.newHashMap();
        // 五道财经
        List<FinanceEntity> finances = financeService.companyDynamics(1);
        // 高尔夫活动
        List<FinanceEntity> golf = financeService.companyDynamics(2);
        // 公益活动
        List<FinanceEntity> welfare = financeService.companyDynamics(3);
        result.put("finances", finances);
        result.put("golf", golf);
        result.put("welfare", welfare);
        return R.ok().put("data", result);
    }

    /**
     * 财经列表 分页
     * @param params
     * @return
     */
    @GetMapping(value = "/finance-list")
    public R financeList(@RequestParam Map<String, Object> params) {
        PageUtils pages = financeService.financeList(params);
        return R.ok().put("data", pages);
    }

    /**
     * 财经详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/finance-info/{id}")
    public R financeInfo(@PathVariable("id") String id) {
        FinanceEntity finance = financeService.getOne(Wrappers.lambdaQuery(FinanceEntity.class)
                .eq(FinanceEntity::getDelFlag, 0).eq(FinanceEntity::getStatus, 0).eq(FinanceEntity::getId, id));
        if (Objects.isNull(finance)) {
            return R.error("文章不存在");
        }
        FinanceModel model = new FinanceModel();
        BeanUtils.copyProperties(finance, model);
        return R.ok().put("data", model);
    }

    /**
     * 投资资讯
     * @return
     */
    @GetMapping(value = "/information-list")
    public R customerInformation() {
        Map<String, Object> result = Maps.newHashMap();
        // 白话财经
        List<InformationModel> vernacular = informationService.customerInformation(1);
        // 固收资讯
        List<InformationModel> income = informationService.customerInformation(2);
        // 股权资讯
        List<InformationModel> stock  = informationService.customerInformation(3);
        // 二级市场资讯
        List<InformationModel> market = informationService.customerInformation(4);
        result.put("vernacular", vernacular);
        result.put("income", income);
        result.put("stock", stock);
        result.put("market", market);
        return R.ok().put("data", result);
    }

    /**
     * 资讯列表
     * @param params
     * @return
     */
    @GetMapping(value = "/information-pages")
    public R customerInformationPages(@RequestParam Map<String, Object> params) {
        PageUtils pages = informationService.customerInformationPages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 活动列表
     * @param params
     * @return
     */
    @GetMapping(value = "/activity-pages")
    public R activityPages(@RequestParam Map<String, Object> params) {
        PageUtils pages = activityService.customerActivityPages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 活动详情
     * @param id
     * @return
     */
    @GetMapping(value = "/activity-info/{id}")
    public R activityInfo(@PathVariable("id") String id) {
        ActivityEntity activity = activityService.getOne(Wrappers.lambdaQuery(ActivityEntity.class)
                .eq(ActivityEntity::getStatus, 1).eq(ActivityEntity::getDelFlag, 0)
                .eq(ActivityEntity::getId, id).last("LIMIT 1"));
        if (Objects.isNull(activity)) {
            return R.error("活动不存在");
        }
        return R.ok().put("data", activity);
    }

    /**
     * 活动预约
     * @param activityId 活动id
     * @return
     */
    @GetMapping(value = "/activity-subscribe/{activityId}")
    public R activitySubscribe(@PathVariable("activityId") String activityId) {
        return activityService.activitySubscribe(activityId, ShiroUtils.getUserId());
    }

    /**
     * 我预约的活动
     * @return
     */
    @GetMapping(value = "/my-activity")
    public R mySubscribeActivity() {
        String userId = ShiroUtils.getUserId();
        JSONObject result = activityService.mySubscribeActivity(userId);
        return R.ok().put("data", result);
    }
}
