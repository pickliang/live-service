package io.live_mall.modules.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.ActivityEntity;
import io.live_mall.modules.server.entity.FinanceEntity;
import io.live_mall.modules.server.entity.InformationBrowseEntity;
import io.live_mall.modules.server.entity.InformationEntity;
import io.live_mall.modules.server.model.FinanceModel;
import io.live_mall.modules.server.model.InformationDisclosureModel;
import io.live_mall.modules.server.model.InformationModel;
import io.live_mall.modules.server.service.*;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    private final InformationDisclosureService informationDisclosureService;
    private final InformationBrowseService informationBrowseService;
    private final SysUserService sysUserService;

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
        SysUserEntity sysUser = sysUserService.getById(finance.getCreateUser());
        String author = Objects.nonNull(sysUser) ?sysUser.getRealname() : "管理员";
        model.setAuthor(author);
        return R.ok().put("data", model);
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
    public R activitySubscribe(@PathVariable("activityId") Long activityId) {
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

    /**
     * 信息披露列表
     * @param params
     * @return
     */
    @GetMapping(value = "/disclosure-list")
    public R  informationDisclosureList(@RequestParam Map<String, Object> params) {
        PageUtils pages = informationDisclosureService.customerPages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 信息披露详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/disclosure-info/{id}")
    public R informationDisclosureInfo(@PathVariable("id") String id) {
        InformationDisclosureModel model = informationDisclosureService.informationDisclosureInfo(id);
        return R.ok().put("data", model);
    }

    /**
     * 资讯详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/information-info/{id}")
    public R customerInformationInfo(@PathVariable("id") String id) {
        InformationEntity information = informationService.getById(id);
        if (Objects.nonNull(information) && 1 == information.getDelFlag()) {
            return R.error("资讯已删除");
        }
        if (information.getAuth() == 1 && Objects.isNull(ShiroUtils.getUserEntity().getCardNum())) {
            return R.error(100, "请绑定身份证");
        }
        InformationModel model = new InformationModel();
        BeanUtils.copyProperties(information, model);
        SysUserEntity sysUser = sysUserService.getById(information.getCreateUser());
        String author = Objects.nonNull(sysUser) ?sysUser.getRealname() : "管理员";
        model.setAuthor(author);
        return R.ok().put("data", model);
    }

    /**
     * 资讯浏览保存
     * @param entity
     * @return
     */
    @PostMapping(value = "/information-browser")
    public R informationBrowserSave(@RequestBody InformationBrowseEntity entity) {
        if (StringUtils.isBlank(entity.getInformationId())) {
            return R.error();
        }
        entity.setCreateTime(new Date());
        boolean save = informationBrowseService.save(entity);
        return save ? R.ok() : R.error();
    }
}
