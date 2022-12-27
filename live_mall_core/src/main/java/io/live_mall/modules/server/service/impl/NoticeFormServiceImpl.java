package io.live_mall.modules.server.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;

import io.live_mall.modules.server.dao.NoticeFormDao;
import io.live_mall.modules.server.entity.NoticeFormEntity;
import io.live_mall.modules.server.service.NoticeFormService;


@Service("noticeFormService")
public class NoticeFormServiceImpl extends ServiceImpl<NoticeFormDao, NoticeFormEntity> implements NoticeFormService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<NoticeFormEntity> page = this.page(
                new Query<NoticeFormEntity>().getPage(params),
                new QueryWrapper<NoticeFormEntity>().orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

}