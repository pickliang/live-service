package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MmsMemberDao;
import io.live_mall.modules.server.entity.MmsMemberEntity;
import io.live_mall.modules.server.service.MmsMemberService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/24 15:32
 * @description
 */
@Service("mmsMemberService")
public class MmsMemberServiceImpl extends ServiceImpl<MmsMemberDao, MmsMemberEntity> implements MmsMemberService {
    @Override
    public PageUtils pages(Map<String, Object> params) {
        return new PageUtils(this.baseMapper.pages(new Query<JSONObject>().getPage(params), params));
    }
}
