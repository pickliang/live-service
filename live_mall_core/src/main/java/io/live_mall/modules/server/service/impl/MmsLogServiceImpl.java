package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsLogDao;
import io.live_mall.modules.server.entity.MmsLogEntity;
import io.live_mall.modules.server.service.MmsLogService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @author yewl
 * @date 2023/2/20 21:03
 * @description
 */
@Service("mmsLogService")
public class MmsLogServiceImpl extends ServiceImpl<MmsLogDao, MmsLogEntity> implements MmsLogService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        Integer type = Integer.valueOf(String.valueOf(params.get("type")));
        type = Objects.isNull(type) ? 1 :type;
        return new PageUtils(this.baseMapper.pages(new Query<MmsLogEntity>().getPage(params), type));
    }
}
