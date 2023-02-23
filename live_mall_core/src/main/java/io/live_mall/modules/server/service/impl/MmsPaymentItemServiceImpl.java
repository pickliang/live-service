package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsPaymentItemDao;
import io.live_mall.modules.server.entity.MmsPaymentItemEntity;
import io.live_mall.modules.server.service.MmsPaymentItemService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/23 13:26
 * @description
 */
@Service("mmsPaymentItemService")
public class MmsPaymentItemServiceImpl extends ServiceImpl<MmsPaymentItemDao, MmsPaymentItemEntity> implements MmsPaymentItemService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        String mmsLogId = String.valueOf(params.get("mmsLogId"));
        return new PageUtils(this.baseMapper.pages(new Query<MmsPaymentItemEntity>().getPage(params), mmsLogId));
    }
}
