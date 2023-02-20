package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youzan.cloud.open.sdk.gen.v1_0_0.model.YouzanScrmCustomerDetailGetResult;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.modules.server.dao.YouZanUserDao;
import io.live_mall.modules.server.entity.YouZanUserEntity;
import io.live_mall.modules.server.service.YouZanUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author yewl
 * @date 2023/2/19 11:14
 * @description
 */
@Service("youZanService")
public class YouZanUserServiceImpl extends ServiceImpl<YouZanUserDao, YouZanUserEntity> implements YouZanUserService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(String userId, String yzOpenId, YouzanScrmCustomerDetailGetResult.YouzanScrmCustomerDetailGetResultData data) {
        if (Objects.isNull(data)) {
            return false;
        }
        YouZanUserEntity entity = new YouZanUserEntity();
        BeanUtils.copyProperties(data, entity);
        entity.setUserId(userId);
        entity.setYzOpenId(yzOpenId);
        entity.setLastTradeAt(DateUtils.unixToDate(data.getLastTradeAt()));
        entity.setCreatedAt(DateUtils.unixToDate(data.getCreatedAt()));
        entity.setUpdatedAt(DateUtils.unixToDate(data.getUpdatedAt()));
        entity.setTags(JSON.toJSONString(data.getTags()));
        entity.setRights(JSON.toJSONString(data.getRights()));
        entity.setCards(JSON.toJSONString(data.getCards()));
        entity.setCustomerAttrInfos(JSON.toJSONString(data.getCustomerAttrinfos()));
        boolean saveOrUpdate = this.saveOrUpdate(entity);
        return saveOrUpdate;
    }

}
