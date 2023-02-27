package io.live_mall.modules.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.dto.ActivityDto;
import io.live_mall.modules.server.dto.CustomerBannerDto;
import io.live_mall.modules.server.dto.InformationDto;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.model.CustomerBannerModel;
import io.live_mall.modules.server.model.InformationModel;
import io.live_mall.modules.server.service.*;
import io.live_mall.modules.server.utils.QrCodeUtils;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.SysUserService;
import io.live_mall.properties.QrCodeProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author yewl
 * @date 2023/1/3 14:52
 * @description 文章
 */
@RestController
@RequestMapping("article")
@AllArgsConstructor
public class ArticleController {

    private final FinanceService financeService;
    private final InformationService informationService;
    private final InformationLabelService informationLabelService;
    private final ActivityService activityService;
    private final QrCodeProperties qrCodeProperties;
    private final InformationBrowseService informationBrowseService;
    private final CustomerBannerService customerBannerService;
    private final SysUserService sysUserService;

    @GetMapping(value = "/finance/list")
    @RequiresPermissions("server:finance:list")
    public R finances(@RequestParam Map<String, Object> params) {
        PageUtils pages = financeService.financePages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 保存财经
     * @param finance
     * @return
     */
    @PostMapping(value = "/finance/save")
    @RequiresPermissions("server:finance:save")
    public R financeSave(@RequestBody FinanceEntity finance) {
        finance.setStatus(0);
        finance.setDelFlag(0);
        finance.setCreateTime(new Date());
        finance.setCreateUser(ShiroUtils.getUserId());
        boolean save = financeService.save(finance);
        return save ? R.ok() : R.error();
    }

    /**
     * 更新财经
     * @param finance
     * @return
     */
    @PutMapping(value = "/finance/modify")
    @RequiresPermissions("server:finance:update")
    public R financeUpdate(@RequestBody FinanceEntity finance) {
        finance.setUpdateTime(new Date());
        finance.setUpdateUser(ShiroUtils.getUserId());
        boolean update = financeService.updateById(finance);
        return update ? R.ok() : R.error();
    }

    /**
     * 上/下架财经
     * @param params
     * @return
     */
    @PutMapping(value = "/finance-status")
    @RequiresPermissions("server:finance:update")
    public R financeUpdateStatus(@RequestBody JSONObject params) {
        Long id = params.getLong("id");
        Integer status = params.getInteger("status");
        FinanceEntity finance = new FinanceEntity();
        finance.setId(id);
        finance.setStatus(status);
        finance.setUpdateTime(new Date());
        finance.setUpdateUser(ShiroUtils.getUserId());
        boolean update = financeService.updateById(finance);
        return update ? R.ok() : R.error();
    }

    /**
     * 财经详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/finance-info/{id}")
    @RequiresPermissions("server:finance:info")
    public R financeInfo(@PathVariable("id") Long id) {
        return R.ok().put("data", financeService.financeInfo(id));
    }

    /**
     * 资讯标签列表
     * @return
     */
    @GetMapping(value = "/information-labels")
    @RequiresPermissions("server:information:list")
    public R informationLabels(@RequestParam(defaultValue = "1") Integer classify) {
        List<InformationLabelEntity> list = informationLabelService.list(Wrappers.lambdaQuery(InformationLabelEntity.class)
                .eq(InformationLabelEntity::getClassify, classify)
                .eq(InformationLabelEntity::getDelFlag, 0));
        return R.ok().put("data", list);
    }

    /**
     * 资讯标签保存
     * @param entity
     * @return
     */
    @PostMapping(value = "/information-label-save")
    @RequiresPermissions("server:information:save")
    public R informationLabelSave(@RequestBody InformationLabelEntity entity) {
        int count = informationLabelService.count(Wrappers.lambdaQuery(InformationLabelEntity.class)
                .eq(InformationLabelEntity::getLabel, entity.getLabel().trim())
                .eq(InformationLabelEntity::getClassify, entity.getClassify())
                .eq(InformationLabelEntity::getDelFlag, 0));
        if (count > 0) {
            return R.error("标签已存在");
        }
        entity.setLabel(entity.getLabel().trim());
        entity.setCreateTime(new Date());
        entity.setCreateUser(ShiroUtils.getUserId());
        entity.setDelFlag(0);
        boolean save = informationLabelService.save(entity);
        return save ? R.ok() : R.error();
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @PutMapping(value = "/information-label-delete/{id}")
    @RequiresPermissions("server:information:update")
    public R informationLabelDelete(@PathVariable("id") String id) {
        InformationLabelEntity entity = new InformationLabelEntity();
        entity.setId(id);
        entity.setDelFlag(1);
        entity.setDelTime(new Date());
        boolean update = informationLabelService.updateById(entity);
        return update ? R.ok() : R.error();
    }

    /**
     * 资讯列表
     * @param params
     * @return
     */
    @GetMapping(value = "/information-list")
    @RequiresPermissions("server:information:list")
    public R informationList(@RequestParam Map<String, Object> params) {
        PageUtils pages = informationService.informationPages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 理财师端投资资讯
     * @return
     */
    @GetMapping(value = "/financial-planner-information")
    public R financialPlannerInformation() {
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
     * 理财师端资讯详情
     * @param id 主键id
     * @return
     */
    @GetMapping(value = "/financial-planner-information-info/{id}")
    public R financialPlannerInformationInfo(@PathVariable("id") String id) {
        InformationEntity information = informationService.getById(id);
        InformationModel model = new InformationModel();
        BeanUtils.copyProperties(information, model);
        SysUserEntity sysUser = sysUserService.getById(information.getCreateUser());
        String author = Objects.nonNull(sysUser) ?sysUser.getRealname() : "管理员";
        model.setAuthor(author);
        return R.ok().put("data", model);
    }

    /**
     * 资讯保存
     * @param information
     * @return
     */
    @PostMapping(value = "/information-save")
    @RequiresPermissions("server:information:save")
    public R informationSave(@RequestBody InformationDto information) {
        InformationEntity entity = new InformationEntity();
        BeanUtils.copyProperties(information, entity);
        entity.setCreateUser(ShiroUtils.getUserId());
        entity.setCreateTime(new Date());
        boolean save = informationService.save(entity);
        return save ? R.ok() : R.error();
    }

    /**
     * 资讯更新
     * @param information
     * @return
     */
    @PutMapping(value = "/information-update")
    @RequiresPermissions("server:information:update")
    public R informationUpdate(@RequestBody InformationDto information) {
        InformationEntity entity = new InformationEntity();
        BeanUtils.copyProperties(information, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(ShiroUtils.getUserId());
        boolean update = informationService.updateById(entity);
        return update ? R.ok() : R.error();
    }


    /**
     * 资讯详情
     * @param id
     * @return
     */
    @GetMapping(value = "/information-info/{id}")
    @RequiresPermissions("server:information:info")
    public R informationInfo(@PathVariable("id") Long id) {
        InformationEntity entity = informationService.getById(id);
        InformationDto dto = new InformationDto();
        BeanUtils.copyProperties(entity, dto);
        return R.ok().put("data", dto);
    }

    /**
     * 资讯删除
     * @param id
     * @return
     */
    @PutMapping(value = "/information-delete/{id}")
    @RequiresPermissions("server:information:update")
    public R informationDelete(@PathVariable("id") Long id) {
        boolean update = informationService.update(Wrappers.lambdaUpdate(InformationEntity.class)
                .set(InformationEntity::getDelFlag, 1)
                .set(InformationEntity::getDelTime, new Date())
                .eq(InformationEntity::getId, id));
        return update ? R.ok() : R.error();
    }

    /**
     * 活动列表
     * @param params
     * @return
     */
    @GetMapping(value = "/activity-list")
    @RequiresPermissions("server:activity:list")
    public R activityList(@RequestParam Map<String, Object> params) {
        PageUtils pages = activityService.activityPages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 活动保存
     * @param dto
     * @return
     */
    @PostMapping(value = "/activity-save")
    @RequiresPermissions("server:activity:save")
    public R activitySave(@RequestBody ActivityDto dto) {
        ActivityEntity entity = new ActivityEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(new Date());
        entity.setCreateUser(ShiroUtils.getUserId());
        boolean save = activityService.save(entity);
        return save ? R.ok() : R.error();
    }

    /**
     * 活动更新
     * @param dto
     * @return
     */
    @PutMapping(value = "/activity-update")
    @RequiresPermissions("server:activity:update")
    public R activityUpdate(@RequestBody ActivityDto dto) {
        ActivityEntity entity = new ActivityEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(ShiroUtils.getUserId());
        boolean save = activityService.updateById(entity);
        return save ? R.ok() : R.error();
    }

    /**
     * 活动详情
     * @param id
     * @return
     */
    @GetMapping(value = "/activity-info/{id}")
    @RequiresPermissions("server:activity:info")
    public R activityInfo(@PathVariable("id") Long id) {
        ActivityEntity entity = activityService.getById(id);
        ActivityDto dto = new ActivityDto();
        BeanUtils.copyProperties(entity, dto);
        return R.ok().put("data", dto);
    }

    /**
     * 活动删除
     * @param id
     * @return
     */
    @PutMapping(value = "/activity-delete/{id}")
    @RequiresPermissions("server:activity:update")
    public R activityDelete(@PathVariable("id") Long id) {
        boolean update = activityService.update(Wrappers.lambdaUpdate(ActivityEntity.class)
                .set(ActivityEntity::getDelFlag, 1)
                .set(ActivityEntity::getDelTime, new Date())
                .eq(ActivityEntity::getId, id));
        return update ? R.ok() : R.error();
    }

    /**
     * 活动审核
     * @param params
     * @return
     */
    @PutMapping(value = "/activity-status")
    @RequiresPermissions("server:activity:update")
    public R activityStatus(@RequestBody JSONObject params) {
        Long id = params.getLong("id");
        Integer status = params.getInteger("status");
        boolean update = activityService.update(Wrappers.lambdaUpdate(ActivityEntity.class)
                .set(ActivityEntity::getStatus, status).eq(ActivityEntity::getId, id));
        return update ? R.ok() : R.error();
    }

    /**
     * 生成活动二维码
     * @param id 主键id
     * @return
     */
    @PutMapping(value = "/activity-qr/{id}")
    @RequiresPermissions("server:information:update")
    public R activityQr(@PathVariable("id") Long id) {
        ActivityEntity entity = activityService.getById(id);
        if (Objects.nonNull(entity) && StringUtils.isNotBlank(entity.getQrCode())) {
            return R.error("二维码已存在");
        }
        String url = qrCodeProperties.getQrUrl() + id;
        String qrCode = QrCodeUtils.qrCode(url);
        boolean update = activityService.update(Wrappers.lambdaUpdate(ActivityEntity.class)
                .set(ActivityEntity::getQrCode, qrCode).eq(ActivityEntity::getId, id));
        return update ? R.ok() : R.error();
    }

    /**
     * 资讯浏览记录
     * @param params
     * @return
     */
    @GetMapping(value = "/information-browser-pages")
    @RequiresPermissions("server:information:list")
    public R informationBrowserPages(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = Maps.newHashMap();
        Long id = Long.valueOf(String.valueOf(params.get("id")));
        InformationEntity information = informationService.getById(id);
        String title = information.getTitle();
        result.put("title", title);
        Long peopleNumber = informationBrowseService.countPeopleNumber(id);
        result.put("visitors", peopleNumber);
        PageUtils pages = informationBrowseService.informationBrowserList(params);
        result.put("list", pages);
        return R.ok().put("data", result);
    }

    /**
     * banner
     * @return
     */
    @GetMapping(value = "/banners")
    public R bannerList() {
        List<CustomerBannerEntity> list = customerBannerService.list(Wrappers.lambdaQuery(CustomerBannerEntity.class)
                .orderByAsc(CustomerBannerEntity::getCreateTime).last("LIMIT 3"));
        List<CustomerBannerModel> models = new ArrayList<>();
        list.forEach(banner -> {
            CustomerBannerModel model = new CustomerBannerModel();
            BeanUtils.copyProperties(banner, model);
            if (1 == banner.getArticleType()) {
                FinanceEntity finance = financeService.getById(banner.getArticleId());
                String articleName = Objects.nonNull(finance) ? finance.getTitle() : "";
                model.setArticleTitle(articleName);
            }
            if (2 == banner.getArticleType()) {
                InformationEntity information = informationService.getById(banner.getArticleId());
                String articleName = Objects.nonNull(information) ? information.getTitle() : "";
                model.setArticleTitle(articleName);
            }
            if (3 == banner.getArticleType()) {
                ActivityEntity activity = activityService.getById(banner.getArticleId());
                String articleName = Objects.nonNull(activity) ? activity.getTitle() : "";
                model.setArticleTitle(articleName);
            }
            models.add(model);
        });
        return R.ok().put("data", models);
    }

    /**
     * 更新banner
     * @param dto
     * @return
     */
    @PostMapping(value = "/update-banner")
    public R updateBanner(@RequestBody CustomerBannerDto dto) {
        CustomerBannerEntity entity = new CustomerBannerEntity();
        entity.setUpdateUser(ShiroUtils.getUserId());
        entity.setUpdateTime(new Date());
        BeanUtils.copyProperties(dto, entity);
        boolean update = customerBannerService.updateById(entity);
        return update ? R.ok() : R.error();
    }

    /**
     * banner下的文章
     * @param type 1-公司动态 2-资讯信息 3-公司活动
     * @return
     */
    @GetMapping(value = "/banner-article")
    public R bannerArticle(@RequestParam(defaultValue = "1") Integer type) {
        List<Map<String, Object>> data = new ArrayList<>();
        if (1 == type) {
            List<FinanceEntity> list = financeService.list(Wrappers.lambdaQuery(FinanceEntity.class)
                    .eq(FinanceEntity::getDelFlag, 0).orderByDesc(FinanceEntity::getCreateTime).last("LIMIT 20"));
            list.forEach(entity -> {
                Map<String, Object> result = Maps.newHashMap();
                result.put("id", entity.getId());
                result.put("title", entity.getTitle());
                data.add(result);
            });
        }

        if (2 == type) {
            List<InformationEntity> list = informationService.list(Wrappers.lambdaQuery(InformationEntity.class)
                    .eq(InformationEntity::getDelFlag, 0).orderByDesc(InformationEntity::getCreateTime).last("LIMIT 20"));
            list.forEach(entity -> {
                Map<String, Object> result = Maps.newHashMap();
                result.put("id", entity.getId());
                result.put("title", entity.getTitle());
                data.add(result);
            });
        }

        if (3 == type) {
            List<ActivityEntity> list = activityService.list(Wrappers.lambdaQuery(ActivityEntity.class)
                    .eq(ActivityEntity::getDelFlag, 0).orderByDesc(ActivityEntity::getCreateTime).last("LIMIT 20"));
            list.forEach(entity -> {
                Map<String, Object> result = Maps.newHashMap();
                result.put("id", entity.getId());
                result.put("title", entity.getTitle());
                data.add(result);
            });
        }
        return R.ok().put("data", data);
    }
}
