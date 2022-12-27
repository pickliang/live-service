package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.HistoryDuiFuDao;
import io.live_mall.modules.server.entity.HistoryDuiFuEntity;
import io.live_mall.modules.server.service.HistoryDuiFuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2022/12/27 11:43
 * @description
 */
@Service("historyDuiFuService")
@AllArgsConstructor
@Slf4j
public class HistoryDuiFuServiceImpl extends ServiceImpl<HistoryDuiFuDao, HistoryDuiFuEntity> implements HistoryDuiFuService {
}
