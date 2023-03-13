package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.LivePlaybackDao;
import io.live_mall.modules.server.entity.LivePlaybackEntity;
import io.live_mall.modules.server.service.LivePlaybackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * @author yewl
 * @date 2023/2/25 9:53
 * @description
 */
@Service("livePlaybackService")
@Slf4j
public class LivePlaybackServiceImpl extends ServiceImpl<LivePlaybackDao, LivePlaybackEntity> implements LivePlaybackService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.pages(new Query<JSONObject>().getPage(params)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateIsShow(String id) {
        this.baseMapper.updateIsShow(null, 1);
        Integer integer = this.baseMapper.updateIsShow(id, 0);
        return Objects.isNull(integer) ? 0 : integer;
    }
}
