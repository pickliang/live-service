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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import io.live_mall.modules.server.entity.ProductEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.service.ProductService;
import io.live_mall.modules.server.service.RaiseService;
import io.live_mall.common.exception.RRException;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 募集期
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 20:17:06
 */
@RestController
@RequestMapping("server/raise")
public class RaiseController {
    @Autowired
    private RaiseService raiseService;
    
    
    
    /**
     * 修改
     */
    @RequestMapping("/duifu")
    public R duifu(@RequestBody RaiseEntity raise){
    	raiseService.duifu(raiseService.getById(raise.getId()));
        return R.ok();
    }
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:raise:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = raiseService.queryPage(params);
        return R.ok().put("page", page);
    }
    
    @RequestMapping("/getRaiseByProductId")
    @RequiresPermissions("server:raise:list")
    public R getRaiseByProductId(@RequestParam Map<String, Object> params){
    	QueryWrapper<RaiseEntity> queryWrapper = new QueryWrapper<RaiseEntity>().eq("product_id",params.get("productId"));
    	if(params.get("type") ==null) {
    		queryWrapper.eq("status",1);
    	}
        return R.ok().put("data", raiseService.list(queryWrapper));
    }
    
    @RequestMapping("/getRaiseSuccessPage")
    @RequiresPermissions("server:raise:list")
    public R getRaiseSuccessPage(@RequestParam Map<String, Object> params){
        PageUtils page = raiseService.getRaiseSuccessPage(params);
        return R.ok().put("page", page);
    }
    
    
    
    
    /**
     * 修改
     */
    @RequestMapping("/fx")
    @RequiresPermissions("server:raise:update")
    public R fx(@RequestBody RaiseEntity raise){
    	raise.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	raise.setUptDate(new Date());
		raiseService.fx(raise);
        return R.ok();
    }
    
    
    
    	
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:raise:info")
    public R info(@PathVariable("id") String id){
		RaiseEntity raise = raiseService.getById(id);
        return R.ok().put("raise", raise);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:raise:save")
    public R save(@RequestBody RaiseEntity raise){
    	raise.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	raise.setCreateDate(new Date());
    	raise.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	raise.setUptDate(new Date());
    	raise.setStatus(-1);
    	//检查有没有发行的募集期
		raiseService.save(raise);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:raise:update")
    public R update(@RequestBody RaiseEntity raise){
    	RaiseEntity byId = raiseService.getById(raise.getId());
    	if("success".equals(raise.getUptType())){
    		raise.setUptBy(ShiroUtils.getUserEntity().getRealname());
        	raise.setUptDate(new Date());
        	raise.setProductId(byId.getProductId());
    		raiseService.updateSuccess(raise);
    	}else if("issue".equals(raise.getUptType())){
    		int count = raiseService.count(new QueryWrapper<RaiseEntity>().eq("status",1).eq("product_id", byId.getProductId()));
        	if(count> 0 ) {
        		throw new RRException("已存在发行中的募集期,请确认");
        	}
        	raise.setUptBy(ShiroUtils.getUserEntity().getRealname());
        	raise.setUptDate(new Date());
    		raiseService.fx(raise);
    	}else {
        	raise.setUptBy(ShiroUtils.getUserEntity().getRealname());
        	raise.setUptDate(new Date());
    		raiseService.updateById(raise);
    	}
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:raise:delete")
    public R delete(@RequestBody String[] ids){
		raiseService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
