package io.live_mall.modules.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.MemberEntity;
import io.live_mall.modules.server.service.MemberService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;



/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-09 22:21:50
 */
@RestController
@RequestMapping("server/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("server:member:list")
    public R list(@RequestBody JSONObject params){
        PageUtils page = memberService.queryPage(params);
        
        
        
        return R.ok().put("page", page);
    }
    
    
    @RequestMapping("/getSaleMemberList")
    @RequiresPermissions("server:member:list")
    public R getSaleMemberList(@RequestBody JSONObject params){
    	params.put("saleId", ShiroUtils.getUserId());
        PageUtils page = memberService.queryPage(params);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{memberNo}")
    @RequiresPermissions("server:member:info")
    public R info(@PathVariable("memberNo") String memberNo){
		MemberEntity member = memberService.getById(memberNo);
        return R.ok().put("member", member);
    }
    
    @RequestMapping("/getMemberByCustName/{custName}")
    @RequiresPermissions("server:member:info")
    public R getMemberByCustName(@PathVariable("custName") String memberName){
    	Long userId = ShiroUtils.getUserId();
		MemberEntity member = memberService.getOne(new QueryWrapper<MemberEntity>().eq("cust_Name", memberName).eq("sale_id", userId));
        return R.ok().put("data", member);
    }
    
    @RequestMapping("/getTj/{memberNo}")
    @RequiresPermissions("server:member:info")
    public R getTj(@PathVariable("memberNo") String memberNo){
    	Long userId = ShiroUtils.getUserId();
        return R.ok().put("data", memberService.getTj(memberNo,userId+""));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("server:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("server:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("server:member:delete")
    public R delete(@RequestBody String[] memberNos){
		memberService.removeByIds(Arrays.asList(memberNos));
        return R.ok();
    }

    /**
     * 客户编号和姓名 选择客户使用
     * @return
     */
    @GetMapping(value = "/members")
    public R memberList() {
        return R.ok().put("data", memberService.memberList());
    }
}
