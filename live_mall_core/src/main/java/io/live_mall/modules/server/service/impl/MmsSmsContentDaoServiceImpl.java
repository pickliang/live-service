package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsSmsContentDao;
import io.live_mall.modules.server.entity.MmsSmsContentEntity;
import io.live_mall.modules.server.service.MmsSmsContentService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/26 14:26
 * @description
 */
@Service("mmsSmsContentService")
public class MmsSmsContentDaoServiceImpl extends ServiceImpl<MmsSmsContentDao, MmsSmsContentEntity> implements MmsSmsContentService {
    @Override
    public PageUtils pages(Map<String, Object> params, Long userId) {
        Integer type = Integer.valueOf(String.valueOf(params.get("type")));
        return new PageUtils(this.baseMapper.pages(new Query<JSONObject>().getPage(params), String.valueOf(userId), type));
    }
}
