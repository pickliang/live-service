package io.live_mall.modules.server.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.live_mall.modules.server.entity.OrgBountyEntity;
import io.live_mall.modules.server.service.OrgBountyService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-18 17:23:53
 */
@RestController
@RequestMapping("server/orgbounty")
public class OrgBountyController {
    @Autowired
    private OrgBountyService orgBountyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:orgbounty:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orgBountyService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:orgbounty:info")
    public R info(@PathVariable("id") String id){
		OrgBountyEntity orgBounty = orgBountyService.getById(id);

        return R.ok().put("orgBounty", orgBounty);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:orgbounty:save")
    public R save(@RequestBody OrgBountyEntity orgBounty){
    	orgBounty.setCreateBy(ShiroUtils.getUserId()+"");
    	orgBounty.setCreateDate(new Date());
		orgBountyService.saveOrUpdate(orgBounty);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:orgbounty:update")
    public R update(@RequestBody OrgBountyEntity orgBounty){
    	orgBounty.setCreateBy(ShiroUtils.getUserId()+"");
    	orgBounty.setCreateDate(new Date());
		orgBountyService.saveOrUpdate(orgBounty);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:orgbounty:delete")
    public R delete(@RequestBody String[] ids){
		orgBountyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
