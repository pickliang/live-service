package io.live_mall.modules.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.HistoryDuifuPayDao;
import io.live_mall.modules.server.entity.HistoryDuifuPayEntity;
import io.live_mall.modules.server.service.HistoryDuifuPayService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yewl
 * @date 2022/12/27 15:44
 * @description
 */
@Service("historyDuifuPayService")
@AllArgsConstructor
public class HistoryDuifuPayServiceImpl extends ServiceImpl<HistoryDuifuPayDao, HistoryDuifuPayEntity> implements HistoryDuifuPayService {
}
