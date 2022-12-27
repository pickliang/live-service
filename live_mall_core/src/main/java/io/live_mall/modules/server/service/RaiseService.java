package io.live_mall.modules.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.RaiseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 募集期
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-30 22:19:57
 */
public interface RaiseService extends IService<RaiseEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void fx(RaiseEntity raise);

	PageUtils getRaiseSuccessPage(Map<String, Object> params);

	void updateSuccess(RaiseEntity raise);

	RaiseEntity getNewRaise(String id);


	void down(String raiseId);

	void canCreateOrder(String raiseId, Integer appointMoney);

	void duifu(RaiseEntity byId);

	Date getMaxEndDate(String productId);

}

