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
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

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

    /**
     * 保存短信链接模板
     * @param entity
     * @return
     */
    @PostMapping(value = "/save")
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
    @SneakyThrows
    public R getAppletsUrl(@RequestParam(defaultValue = "develop") String envVersion) {
        String urlLink = appletsService.getUrlLink(envVersion);
        return R.ok().put("data", urlLink);
    }


    /**
     * 发送对付完成通知短信
     * @param params
     * @return
     */
    @PostMapping(value = "/send-duifu")
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

        mmsLogService.sendDuiFuMms(startDate, endDate, ids, token, ShiroUtils.getUserId());
        return R.ok();
    }

    /**
     * 发送付息完成通知短信
     * @param params
     * @return
     */
    @PostMapping(value = "/send-payment")
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
        mmsLogService.sendPayMendMms(startDate, endDate, ids, token, ShiroUtils.getUserId());
        return R.ok();
    }

    /**
     * 兑付完成发送列表
     * @param params 分页
     * @return
     */
    @GetMapping(value = "/log-list")
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
    public R mmsMembers(@RequestParam Map<String, Object> params) {
        return R.ok().put("data", mmsMemberService.pages(params));
    }
}
