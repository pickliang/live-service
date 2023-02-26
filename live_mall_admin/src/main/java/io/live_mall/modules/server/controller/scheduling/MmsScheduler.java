package io.live_mall.modules.server.controller.scheduling;

import com.alibaba.fastjson.JSONObject;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.modules.server.model.DuiFuNoticeModel;
import io.live_mall.modules.server.service.MmsLogItemService;
import io.live_mall.modules.server.service.MmsPaymentItemService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.sms.mms.MmsClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yewl
 * @date 2023/2/25 12:22
 * @description
 */
@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class MmsScheduler {
    private final RedisUtils redisUtils;
    private final OrderService orderService;
    private final MmsLogItemService mmsLogItemService;
    private final MmsPaymentItemService mmsPaymentItemService;


    /**
     * 每天10点执行 发送对付预警短信
     * @return
     */
    @Scheduled(cron = "0 0 10 1/1 * ?")
    public void duiFuEarlyWarning() {
        // 7天
        String date = LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_DATE);
        CompletableFuture.supplyAsync(() -> sendEarlyWarning(date));
        // 5天
        String fiveDate = LocalDate.now().plusDays(5).format(DateTimeFormatter.ISO_DATE);
        CompletableFuture.supplyAsync(() -> sendEarlyWarning(fiveDate));
        // 3天
        String threeDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE);
        CompletableFuture.supplyAsync(() -> sendEarlyWarning(threeDate));
        // 1天
        String oneDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);
        CompletableFuture.supplyAsync(() -> sendEarlyWarning(oneDate));
    }

    @SneakyThrows
    private boolean sendEarlyWarning(String date) {
        List<JSONObject> list = orderService.duifuNoticeData(date, date);
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        return mmsLogItemService.sendDuiFuEarlyWarning(token, list);
    }

    /**
     * 每天10点执行 发送对付预警短信
     * @return
     */
    @Scheduled(cron = "0 0 10 1/1 * ?")
    public void paymentEarlyWarning() {
        // 3天
        String threeDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE);
        CompletableFuture.supplyAsync(() -> sendPaymentEarlyWarning(threeDate));
        // 1天
        String oneDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);
        CompletableFuture.supplyAsync(() -> sendPaymentEarlyWarning(oneDate));
    }

    @SneakyThrows
    private boolean sendPaymentEarlyWarning(String date) {
        String token = redisUtils.get(RedisKeyConstants.MMS_TOKEN);
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            token = MmsClient.getToken();
            long expire = 60 * 60 * 10;
            redisUtils.set(RedisKeyConstants.MMS_TOKEN, token, expire);
        }
        List<DuiFuNoticeModel> models = orderService.orderPayNoticeData(date, date);
        return mmsPaymentItemService.sendPaymentEarlyWarning(token, models);
    }
}
