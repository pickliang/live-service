package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youzan.cloud.open.sdk.gen.v1_0_0.model.YouzanScrmCustomerDetailGetResult;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.modules.server.dao.CustomerUserDao;
import io.live_mall.modules.server.dao.YouZanOrderDao;
import io.live_mall.modules.server.dao.YouZanUserDao;
import io.live_mall.modules.server.entity.CustomerUserEntity;
import io.live_mall.modules.server.entity.YouZanUserEntity;
import io.live_mall.modules.server.model.YouZanUserModel;
import io.live_mall.modules.server.service.YouZanUserService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class YouZanUserServiceImpl extends ServiceImpl<YouZanUserDao, YouZanUserEntity> implements YouZanUserService {
    private final CustomerUserDao customerUserDao;
    private final YouZanOrderDao youZanOrderDao;

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

    @Override
    public YouZanUserModel yzUserInfo(String cardNum) {
        CustomerUserEntity userEntity = customerUserDao.selectOne(Wrappers.lambdaQuery(CustomerUserEntity.class).eq(CustomerUserEntity::getCardNum, cardNum).last("LIMIT 1"));
        if (Objects.nonNull(userEntity)) {
            YouZanUserModel model = this.baseMapper.yzUserDetail(userEntity.getId());
            model.setConsumerAmount(youZanOrderDao.consumerAmount(model.getYzOpenId()));
            model.setConsumerOrderNum(youZanOrderDao.consumerOrderNum(model.getYzOpenId()));
            model.setRecentlyOderTime(youZanOrderDao.recentlyOderTime(model.getYzOpenId()));
            return model;
        }
        return null;
    }

}
