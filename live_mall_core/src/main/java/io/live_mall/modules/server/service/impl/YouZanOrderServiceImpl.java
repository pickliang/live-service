package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youzan.cloud.open.sdk.gen.v4_0_2.model.YouzanTradesSoldGetResult;
import io.live_mall.modules.server.dao.YouZanOrderDao;
import io.live_mall.modules.server.entity.YouZanOrderEntity;
import io.live_mall.modules.server.service.YouZanOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yewl
 * @date 2023/2/19 13:25
 * @description
 */
@Service("youZanOrderService")
public class YouZanOrderServiceImpl extends ServiceImpl<YouZanOrderDao, YouZanOrderEntity> implements YouZanOrderService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(YouzanTradesSoldGetResult.YouzanTradesSoldGetResultData data) {
        List<YouzanTradesSoldGetResult.YouzanTradesSoldGetResultFullorderinfolist> fullOrderInfoList = data.getFullOrderInfoList();
        List<YouZanOrderEntity> entities = new ArrayList<>();
        fullOrderInfoList.forEach(order -> {
            YouzanTradesSoldGetResult.YouzanTradesSoldGetResultFullorderinfo orderInfo = order.getFullOrderInfo();
            YouZanOrderEntity entity = new YouZanOrderEntity();
            YouzanTradesSoldGetResult.YouzanTradesSoldGetResultOrderinfo info = orderInfo.getOrderInfo();
            entity.setOrderInfo(JSON.toJSONString(info));
            BeanUtils.copyProperties(info, entity);
            entity.setAddressInfo(JSON.toJSONString(orderInfo.getAddressInfo()));
            entity.setOrders(JSON.toJSONString(orderInfo.getOrders()));
            entity.setBuyerInfo(JSON.toJSONString(orderInfo.getBuyerInfo()));
            entity.setSourceInfo(JSON.toJSONString(orderInfo.getSourceInfo()));
            entity.setPayInfo(JSON.toJSONString(orderInfo.getPayInfo()));
            entity.setChildInfo(JSON.toJSONString(orderInfo.getChildInfo()));
            entity.setRemarkInfo(JSON.toJSONString(orderInfo.getRemarkInfo()));
            entity.setCreateTime(new Date());
            entities.add(entity);
        });
        if (!entities.isEmpty()) {
            this.saveOrUpdateBatch(entities);
        }
    }
}
