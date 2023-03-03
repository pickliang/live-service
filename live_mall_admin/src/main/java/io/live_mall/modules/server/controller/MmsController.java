package io.live_mall.modules.server.controller;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.R;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.modules.applets.AppletsService;
import io.live_mall.modules.server.entity.MmsTemplateEntity;
import io.live_mall.modules.server.service.*;
import io.live_mall.sms.mms.MmsClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author yewl
 * @date 2023/2/21 20:19
 * @description
 */
@RestController
@RequestMapping("mms")
@AllArgsConstructor
@Slf4j
public class MmsController {

    private final MmsTemplateService mmsTemplateService;
    private final AppletsService appletsService;
    private final MmsLogService mmsLogService;
    private final MmsLogItemService mmsLogItemService;
    private final RedisUtils redisUtils;
    private final MmsPaymentItemService mmsPaymentItemService;
    private final MemberService memberService;
    private final MmsMemberService mmsMemberService;
    private final MmsSmsLogService mmsSmsLogService;
    private final MmsSmsContentService mmsSmsContentService;

    /**
     * 保存短信链接模板
     * @param entity
     * @return
     */
    @PostMapping(value = "/save")
    @RequiresPermissions("server:mms:list")
    @SneakyThrows
    public R save(@RequestBody MmsTemplateEntity entity) {
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        JSONObject result = MmsClient.getMMsIdStatus(token, entity.getMmsId());
        JSONObject content = result.getJSONObject("content");
        JSONObject contentResult = content.getJSONObject("result");
        Integer code = contentResult.getInteger("code");
        // 33004003 表示通道审核完成
        if (33004003 != code) {
            return R.error("通道审核未完成，请待审核完成再添加！");
        }
        entity.setCode(code);
        entity.setResult(result.toJSONString());
        entity.setCreateTime(new Date());
        entity.setCreateUser(ShiroUtils.getUserId());
        boolean save = mmsTemplateService.save(entity);
        return save ? R.ok() : R.error();
    }

    /**
     * 获取打开小程序的链接
     * @param envVersion 默认值"release"。要打开的小程序版本。正式版为 "release"，体验版为"trial"，开发版为"develop"，仅在微信外打开时生效
     * @return
     */
    @GetMapping(value = "/applets-url")
    @RequiresPermissions("server:mms:list")
    @SneakyThrows
    public R getAppletsUrl(@RequestParam(defaultValue = "develop") String envVersion) {
        String urlLink = appletsService.getUrlLink(envVersion);
        return R.ok().put("data", urlLink);
    }


    /**
     * 发送兑付完成通知短信
     * @param params
     * @return
     */
    @PostMapping(value = "/send-duifu")
    @RequiresPermissions("server:mms:save")
    @SneakyThrows
    public R mmsSend(@RequestBody Map<String, Object> params) {
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        String startDate = String.valueOf(params.get("startDate"));
        String endDate = String.valueOf(params.get("endDate"));
        String ids = String.valueOf(params.get("ids"));
        String finalToken = token;
        CompletableFuture.supplyAsync(() -> mmsLogItemService.sendDuiFuCompleted(finalToken, startDate, endDate, ids, ShiroUtils.getUserId()));
        return R.ok();
    }

    /**
     * 发送付息完成通知短信
     * @param params
     * @return
     */
    @PostMapping(value = "/send-payment")
    @RequiresPermissions("server:mms:save")
    @SneakyThrows
    public R mmsSenPayment(@RequestBody Map<String, Object> params) {
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        String startDate = String.valueOf(params.get("startDate"));
        String endDate = String.valueOf(params.get("endDate"));
        String ids = String.valueOf(params.get("ids"));
        String finalToken = token;
        CompletableFuture.supplyAsync(() -> mmsPaymentItemService.sendPaymentCompleted(finalToken, startDate, endDate, ids, ShiroUtils.getUserId()));
        return R.ok();
    }

    /**
     * 兑付完成发送列表
     * @param params 分页
     * @return
     */
    @GetMapping(value = "/log-list")
    @RequiresPermissions("server:mms:list")
    public R mmsLogs(@RequestParam Map<String, Object> params) {
        PageUtils pages = mmsLogService.pages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 对付查询通知结果
     * @param params
     * @return
     */
    @GetMapping(value = "/log-items")
    @RequiresPermissions("server:mms:list")
    public R mmsLogItems(@RequestParam Map<String, Object> params) {
        PageUtils pages = mmsLogItemService.pages(params);
        return R.ok().put("data", pages);
    }

    /**
     *  付息查询通知结果
     * @param params
     * @return
     */
    @GetMapping(value = "/payment-items")
    @RequiresPermissions("server:mms:list")
    public R mmsPaymentItem(@RequestParam Map<String, Object> params) {
        PageUtils pages = mmsPaymentItemService.pages(params);
        return R.ok().put("data", pages);
    }

    /**
     * 发送客户生日短信
     * @param params
     * @return
     */
    @PostMapping(value = "/send-integral")
    @SneakyThrows
    @RequiresPermissions("server:mms:save")
    public R sendIntegral(@RequestBody Map<String, Object> params) {
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        String startDate = String.valueOf(params.get("startDate"));
        String endDate = String.valueOf(params.get("endDate"));
        String memberNos = String.valueOf(params.get("memberNos"));
        memberService.sendMmsIntegral(startDate, endDate, memberNos, ShiroUtils.getUserId(), token);
        return R.ok();
    }

    /**
     * 积分发放通知结果
     * @param params
     * @return
     */
    @GetMapping(value = "/member-item")
    @RequiresPermissions("server:mms:list")
    public R mmsMembers(@RequestParam Map<String, Object> params) {
        return R.ok().put("data", mmsMemberService.pages(params));
    }

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return
     */
    @GetMapping(value = "/send-code")
    @SneakyThrows
    public R sendCode(@RequestParam String phone) {
        String key = RedisKeyConstants.MMS_PHONE_TYPE_EXPIRE + phone;
        if (phone.length() != 11) {
            return R.error("手机号码错误");
        }
        String phoneExpire = redisUtils.get(key);
        if (Objects.nonNull(phoneExpire)) {
            return R.error("请勿重复获取验证码");
        }
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        Integer code = RandomUtils.nextInt(1000, 9999);
        Integer sendCode = mmsSmsLogService.sendCode(token, phone, code, ShiroUtils.getUserId());
        if (0 == sendCode) {
            redisUtils.set(key, code, 60 * 30);
            return R.ok();
        }
        return R.error();
    }

    /**
     * 理财师短信内容
     * @param params
     * @return
     */
    @GetMapping(value = "/sms-content")
    public R smsContent(@RequestParam Map<String, Object> params) {
        PageUtils pages = mmsSmsContentService.pages(params, ShiroUtils.getUserId());
        return R.ok().put("data", pages);
    }

}
