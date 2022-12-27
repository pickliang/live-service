package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.ProductLabelEntity;

import java.util.Map;

/**
 * 产品分类标签
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:13:38
 */
public interface ProductLabelService extends IService<ProductLabelEntity> {

	PageUtils queryPage(JSONObject params);
}

