package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.OrgBountyEntity;
import io.live_mall.modules.server.entity.OrgTaskDetailEntity;
import io.live_mall.modules.server.entity.OrgTaskEntity;
import io.live_mall.modules.server.entity.YjViewEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-20 01:15:19
 */
public interface YjViewService extends IService<YjViewEntity> {

    PageUtils queryPage(Map<String, Object> params);
    

	void sysTask(OrgTaskEntity task);

	void yjCreate(JSONObject prams);

	void sysDate(OrgTaskDetailEntity detail, OrgBountyEntity orgBounty, Long userId);


}

