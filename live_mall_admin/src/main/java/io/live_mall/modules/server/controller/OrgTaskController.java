package io.live_mall.modules.server.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.modules.server.entity.OrgTaskDetailEntity;
import io.live_mall.modules.server.entity.OrgTaskEntity;
import io.live_mall.modules.server.entity.SysConfigEntity;
import io.live_mall.modules.server.service.OrgTaskService;
import io.live_mall.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 
 *
 * @author daitao
 * @param <E>
 * @email 867278141@qq.com
 * @date 2021-04-18 17:23:53
 */
@RestController
@RequestMapping("server/orgtask")
public class OrgTaskController<E> {
    @Autowired
    private OrgTaskService orgTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:orgtask:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orgTaskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:orgtask:info")
    public R info(@PathVariable("id") String id){
		OrgTaskEntity orgTask = orgTaskService.getById(id);

        return R.ok().put("orgTask", orgTask);
    }
    
    @Autowired
    SysConfigService sysConfigServicel;
    
    /**
     * 信息
     */
    @RequestMapping("/getCreateInfo")
    public R getCreateInfo(@RequestBody JSONObject jsonObj){
    	SysConfigEntity sysConfigEntity = sysConfigServicel.getById(10);
    	String paramValue = sysConfigEntity.getParamValue();
    	List<String> betweenMonths =DateUtils.getBetweenMonths(DateUtil.beginOfYear(DateUtil.parse(jsonObj.getString("year"),"yyyy")), DateUtil.endOfYear(DateUtil.parse(jsonObj.getString("year"),"yyyy")));
    	OrgTaskEntity orgTaskEntity = new OrgTaskEntity();
    	orgTaskEntity.setYear(jsonObj.getString("year"));
    	for (String mouth : betweenMonths) {
    		List<OrgTaskDetailEntity> orgTaskDetailList = JSONObject.parseArray(paramValue, OrgTaskDetailEntity.class);
    		orgTaskDetailList.stream().forEach(e->{
    			e.setPlanDate(mouth);
    		});
    		orgTaskEntity.getOrgTaskDetailMap().put(mouth, orgTaskDetailList);
		}
        return R.ok().put("orgTask", orgTaskEntity);
    }

    

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:orgtask:save")
    public R save(@RequestBody OrgTaskEntity orgTask){
		orgTaskService.saveOrUpdate(orgTask);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:orgtask:update")
    public R update(@RequestBody OrgTaskEntity orgTask){
		orgTaskService.saveOrUpdate(orgTask);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:orgtask:delete")
    public R delete(@RequestBody String[] ids){
		orgTaskService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
