package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.MmsSmsContentDao;
import io.live_mall.modules.server.entity.MmsSmsContentEntity;
import io.live_mall.modules.server.service.MmsSmsContentService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/2/26 14:26
 * @description
 */
@Service("mmsSmsContentService")
public class MmsSmsContentDaoServiceImpl extends ServiceImpl<MmsSmsContentDao, MmsSmsContentEntity> implements MmsSmsContentService {
}
