package io.live_mall.modules.server.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.modules.server.entity.NoticeFormEntity;
import io.live_mall.modules.server.service.NoticeFormService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;



/**
 * 通知
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-02 02:07:09
 */
@RestController
@RequestMapping("server/noticeform")
public class NoticeFormController {
    @Autowired
    private NoticeFormService noticeFormService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:noticeform:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = noticeFormService.queryPage(params);

        return R.ok().put("page", page);
    }
    
    
    
    
    
    
    @RequestMapping("/getNoticelist")
    @RequiresPermissions("server:noticeform:list")
    public R getNoticelist(@RequestBody JSONObject params){
    	QueryWrapper<NoticeFormEntity> queryWrapper = new QueryWrapper<NoticeFormEntity>();
    	if(StringUtils.isNotBlank(params.getString("status"))) {
    		queryWrapper.eq("status",params.getString("status"));
    	}
    	if(StringUtils.isNotBlank(params.getString("noticeType"))) {
    		queryWrapper.eq("notice_type",params.getString("noticeType"));
    	}
        return R.ok().put("page", noticeFormService.list(queryWrapper.orderByDesc("create_date")));
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("server:noticeform:info")
    public R info(@PathVariable("id") Integer id){
		NoticeFormEntity noticeForm = noticeFormService.getById(id);
        return R.ok().put("noticeForm", noticeForm);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:noticeform:save")
    public R save(@RequestBody NoticeFormEntity noticeForm){
    	noticeForm.setCreateBy(ShiroUtils.getUserEntity().getRealname());
    	noticeForm.setCreateDate(new Date());
    	noticeForm.setUptBy(ShiroUtils.getUserEntity().getRealname());
    	noticeForm.setUptDate(new Date());
		noticeFormService.save(noticeForm);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:noticeform:update")
    public R update(@RequestBody NoticeFormEntity noticeForm){
		noticeFormService.updateById(noticeForm);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:noticeform:delete")
    public R delete(@RequestBody Integer[] ids){
		noticeFormService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
