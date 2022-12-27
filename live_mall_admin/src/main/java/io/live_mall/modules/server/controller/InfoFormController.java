package io.live_mall.modules.server.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.modules.server.entity.InfoFormEntity;
import io.live_mall.modules.server.entity.InfoTypeEntity;
import io.live_mall.modules.server.service.InfoFormService;
import io.live_mall.modules.server.service.InfoTypeService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;



/**
 * 这是资料库父子文件表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:10
 */
@RestController
@RequestMapping("server/infoform")
public class InfoFormController {
    @Autowired
    private InfoFormService infoFormService;

    
    @Autowired
    private InfoTypeService infoTypeService;

    
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:infoform:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = infoFormService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/getInfoformList")
    @RequiresPermissions("server:infoform:list")
    public R getInfoformList(@RequestParam Map<String, Object> params){
        return R.ok().put("data", infoFormService.list(new QueryWrapper<InfoFormEntity>().eq("pid",params.get("pid")).eq("status", 1)));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:infoform:info")
    public R info(@PathVariable("id") Integer id){
		InfoFormEntity infoForm = infoFormService.getById(id);
		InfoTypeEntity byId = infoTypeService.getById(infoForm.getTypeId());
		if(byId !=null) {
			infoForm.setPid(byId.getPid());
		}
        return R.ok().put("infoForm", infoForm);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:infoform:save")
    public R save(@RequestBody InfoFormEntity infoForm){
		infoFormService.save(infoForm);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:infoform:update")
    public R update(@RequestBody InfoFormEntity infoForm){
		infoFormService.updateById(infoForm);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:infoform:delete")
    public R delete(@RequestBody Integer[] ids){
		infoFormService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
