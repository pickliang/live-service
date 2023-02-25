package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsLogDao;
import io.live_mall.modules.server.dao.OrderDao;
import io.live_mall.modules.server.dao.OrderPayDao;
import io.live_mall.modules.server.entity.MmsLogEntity;
import io.live_mall.modules.server.entity.OrderPayEntity;
import io.live_mall.modules.server.model.DuiFuNoticeModel;
import io.live_mall.modules.server.service.MmsLogItemService;
import io.live_mall.modules.server.service.MmsLogService;
import io.live_mall.modules.server.service.MmsPaymentItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yewl
 * @date 2023/2/20 21:03
 * @description
 */
@Service("mmsLogService")
@AllArgsConstructor
public class MmsLogServiceImpl extends ServiceImpl<MmsLogDao, MmsLogEntity> implements MmsLogService {
    private final MmsPaymentItemService mmsPaymentItemService;
    private final OrderDao orderDao;
    private final OrderPayDao orderPayDao;
    private final MmsLogItemService mmsLogItemService;
    @Override
    public PageUtils pages(Map<String, Object> params) {
        Integer type = Integer.valueOf(String.valueOf(params.get("type")));
        type = Objects.isNull(type) ? 1 :type;
        return new PageUtils(this.baseMapper.pages(new Query<MmsLogEntity>().getPage(params), type));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendDuiFuMms(String startDate, String endDate, String ids, String mmsToken, Long userId) {
        List<String> orderIds = Arrays.asList(ids.split(","));
        List<DuiFuNoticeModel> list = orderDao.selectDuifuNoticeData(orderIds);

        // 保存mms发送对付日志
        MmsLogEntity logEntity = new MmsLogEntity();
        logEntity.setStartDate(DateUtils.stringToDate(startDate, DateUtils.DATE_PATTERN));
        logEntity.setEndDate(DateUtils.stringToDate(endDate, DateUtils.DATE_PATTERN));
        logEntity.setRowNum(list.size());
        logEntity.setType(1);
        logEntity.setCreateTime(new Date());
        logEntity.setCreateUser(userId);
        this.baseMapper.insert(logEntity);
        CompletableFuture.supplyAsync(() -> mmsLogItemService.sendDuifuMms(mmsToken, list, logEntity.getId(), userId));
    }



    @Override
    public void sendPayMendMms(String startDate, String endDate, String ids, String mmsToken, Long userId) {
        List<String> order_ids = Arrays.asList(ids.split(","));
        List<DuiFuNoticeModel> models = this.payMendData(startDate, endDate, order_ids);
        // 保存mms发送付息日志
        MmsLogEntity logEntity = new MmsLogEntity();
        logEntity.setStartDate(DateUtils.stringToDate(startDate, DateUtils.DATE_PATTERN));
        logEntity.setEndDate(DateUtils.stringToDate(endDate, DateUtils.DATE_PATTERN));
        logEntity.setRowNum(models.size());
        logEntity.setType(2);
        logEntity.setCreateTime(new Date());
        logEntity.setCreateUser(userId);
        this.baseMapper.insert(logEntity);
        CompletableFuture.supplyAsync(() -> mmsPaymentItemService.sendSalePayment(mmsToken, models, logEntity.getId(), userId));
    }

    private List<DuiFuNoticeModel> payMendData(String startDate, String endDate, List<String> ids) {
        List<OrderPayEntity> orderPayEntities = orderPayDao.orderPayList(ids, startDate, endDate);
        List<DuiFuNoticeModel> models = new ArrayList<>();
        if (!orderPayEntities.isEmpty()) {
            Set<String> orderIds = new HashSet<>();
            orderPayEntities.forEach(orderPayEntity -> orderIds.add(orderPayEntity.getOrderId()));
            List<DuiFuNoticeModel> list = orderDao.selectDuifuNoticeData(new ArrayList<>(orderIds));
            Map<String, DuiFuNoticeModel> noticeModelMap = list.stream().collect(Collectors.toMap(DuiFuNoticeModel::getId, model -> model));
            orderPayEntities.forEach(entity -> {
                DuiFuNoticeModel noticeModel = noticeModelMap.get(entity.getOrderId());
                if (Objects.nonNull(noticeModel)) {
                    noticeModel.setProductName(noticeModel.getProductName());
                    noticeModel.setName(entity.getName());
                    noticeModel.setPayDate(entity.getPayDate());
                    models.add(noticeModel);
                }
            });
        }
        return models;
    }
}
