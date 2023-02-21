package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.MmsTemplateDao;
import io.live_mall.modules.server.entity.MmsTemplateEntity;
import io.live_mall.modules.server.service.MmsTemplateService;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2023/2/21 19:17
 * @description
 */
@Service("mmsTemplateService")
public class MmsTemplateServiceImpl extends ServiceImpl<MmsTemplateDao, MmsTemplateEntity> implements MmsTemplateService {
}
