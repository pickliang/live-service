package io.live_mall.modules.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.entity.MemberEntity;
import io.live_mall.modules.server.model.YouZanUserModel;
import io.live_mall.modules.server.service.CustomerUserService;
import io.live_mall.modules.server.service.MemberService;
import io.live_mall.modules.server.service.TouchUserService;
import io.live_mall.modules.server.service.YouZanUserService;
import io.live_mall.tripartite.TouchClients;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


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
    @Autowired
    private YouZanUserService youZanUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TouchUserService touchUserService;
    @Autowired
    private CustomerUserService customerUserService;

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
        if (StringUtils.isNotBlank(member.getCardNum())) {
            // 小程序用户信息
            CustomerUserEntity customerUser = customerUserService.getOne(Wrappers.lambdaQuery(CustomerUserEntity.class)
                    .eq(CustomerUserEntity::getCardNum, member.getCardNum()).last("LIMIT 1"));
            if (Objects.nonNull(customerUser)) {
                member.setCardPhotoR(customerUser.getCardPhotoR());
                member.setCardPhotoL(customerUser.getCardPhotoL());
                member.setCardTime(customerUser.getCardTime());
                member.setPhone(customerUser.getPhone());
            }
        }
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
    public R memberList(String name) {
        return R.ok().put("data", memberService.memberList(name));
    }

    /**
     * 客户三方信息 有赞
     * @param cardNum 身份证号
     * @return
     */
    @GetMapping(value = "/trilateral-yz-info")
    public R trilateralYzInfo(@RequestParam String cardNum) {
        YouZanUserModel model = youZanUserService.yzUserInfo(cardNum);
        return R.ok().put("data", model);
    }

    /**
     * 客户三方信息 小鹅通
     * @param memberNo
     * @return
     */
    @GetMapping(value = "/trilateral-touch-info")
    @SneakyThrows
    public R trilateralTouchInfo(@RequestParam String memberNo) {
        String token = redisUtils.get(RedisKeyConstants.TOUCH_ACCESS_TOKEN);
        if (Objects.isNull(token)) {
            JSONObject data = TouchClients.getToken();
            if (Objects.nonNull(data)) {
                String accessToken = data.getString("access_token");
                Integer expiresIn = data.getInteger("expires_in");
                redisUtils.set(RedisKeyConstants.TOUCH_ACCESS_TOKEN, accessToken, expiresIn);
            }
        }
        return R.ok().put("data", touchUserService.touchUserInfo(token, memberNo));
    }

    /**
     * 小鹅通用户学习记录
     * @param memberNo
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/trilateral-touch-learning")
    @SneakyThrows
    public R trilateralTouchLearning(@RequestParam String memberNo,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10")Integer limit) {
        String token = redisUtils.get(RedisKeyConstants.TOUCH_ACCESS_TOKEN);
        if (Objects.isNull(token)) {
            JSONObject data = TouchClients.getToken();
            if (Objects.nonNull(data)) {
                String accessToken = data.getString("access_token");
                Integer expiresIn = data.getInteger("expires_in");
                redisUtils.set(RedisKeyConstants.TOUCH_ACCESS_TOKEN, accessToken, expiresIn);
            }
        }
        return R.ok().put("data", touchUserService.userLearning(token, memberNo, page, limit));
    }


    /**
     * 发送短信生日通知使用的数据
     * @param startDate yyyy-MM-dd
     * @param endDate yyyy-MM-dd
     * @return
     */
    @GetMapping(value = "/member-list")
    public R members(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        List<JSONObject> list = memberService.memberList(startDate, endDate);
        return R.ok().put("list", list);
    }


}
