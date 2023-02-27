package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.InformationUserItemDao;
import io.live_mall.modules.server.entity.InformationUserItemEntity;
import io.live_mall.modules.server.service.InformationUserItemService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/27 11:26
 * @description
 */
@Service("informationUserItemService")
public class InformationUserItemServiceImpl extends ServiceImpl<InformationUserItemDao, InformationUserItemEntity> implements InformationUserItemService {
    @Override
    public PageUtils pages(Map<String, Object> params, Long userId) {
        return new PageUtils(this.baseMapper.pages(new Query<JSONObject>().getPage(params), userId));
    }
}
