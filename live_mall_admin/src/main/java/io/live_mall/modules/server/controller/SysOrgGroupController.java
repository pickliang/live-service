package io.live_mall.modules.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.OrgRegionEntity;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import io.live_mall.modules.server.service.OrgRegionService;
import io.live_mall.modules.server.service.SysOrgGroupService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 机构组织管理
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
@RestController
@RequestMapping("server/sysorggroup")
public class SysOrgGroupController {
    @Autowired
    private SysOrgGroupService sysOrgGroupService;

    @Autowired
    private OrgRegionService orgRegionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:sysorggroup:list")
    public R list(@RequestParam String  name){
        List<SysOrgGroupEntity> page = sysOrgGroupService.listTree(name);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:sysorggroup:info")
    public R info(@PathVariable("id") String id){
		SysOrgGroupEntity sysOrgGroup = sysOrgGroupService.getById(id);

        return R.ok().put("sysOrgGroup", sysOrgGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:sysorggroup:save")
    public R save(@RequestBody SysOrgGroupEntity sysOrgGroup){
		sysOrgGroupService.saveOrUpdate(sysOrgGroup);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:sysorggroup:delete")
    public R delete(@RequestBody String ids){
		sysOrgGroupService.removeByIds(ids);
        return R.ok();
    }
    @PostMapping("/save-region")
    @RequiresPermissions("server:sysorggroup:save")
    public R saveOrgRegion(@RequestBody @Valid OrgRegionEntity orgRegionEntity) {
        int count = orgRegionService.count(Wrappers.lambdaQuery(OrgRegionEntity.class).eq(OrgRegionEntity::getDelFlag, 0).eq(OrgRegionEntity::getName, orgRegionEntity.getName()));
        if (count > 0) {
            return R.error("城市已存在");
        }
        String orgGroupIds = sysOrgGroupService.orgGroupIds(orgRegionEntity.getName());
        if (StringUtils.isBlank(orgGroupIds)) {
            return R.error("城市暂无理财师");
        }
        orgRegionEntity.setCreateTime(new Date());
        orgRegionEntity.setOrgGroupId(orgGroupIds);
        orgRegionService.save(orgRegionEntity);
        return R.ok();
    }

    @GetMapping(value = "/org-regions")
    @RequiresPermissions("server:sysorggroup:list")
    public R orgRegions() {
        List<OrgRegionEntity> list = orgRegionService.list(Wrappers.lambdaQuery(OrgRegionEntity.class).eq(OrgRegionEntity::getDelFlag, 0));
        return R.ok().put("data", list);
    }

    @PutMapping(value = "/deleted-region/{id}")
    @RequiresPermissions("server:sysorggroup:delete")
    public R deletedOrgRegion(@PathVariable("id") String id) {
        boolean update = orgRegionService.update(Wrappers.lambdaUpdate(OrgRegionEntity.class).set(OrgRegionEntity::getDelFlag, 1).eq(OrgRegionEntity::getId, id));
        return update ? R.ok() : R.error();
    }
}
