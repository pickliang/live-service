package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsLogItemDao;
import io.live_mall.modules.server.entity.MmsLogItemEntity;
import io.live_mall.modules.server.service.MmsLogItemService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/20 21:07
 * @description
 */
@Service("mmsLogItemService")
public class MmsLogItemServiceImpl extends ServiceImpl<MmsLogItemDao, MmsLogItemEntity> implements MmsLogItemService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.pages(new Query<MmsLogItemEntity>().getPage(params), params));
    }
}
