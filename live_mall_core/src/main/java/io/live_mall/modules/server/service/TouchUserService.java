package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.modules.server.entity.TouchUserEntity;

import java.io.IOException;

/**
 * @author yewl
 * @date 2023/2/23 16:28
 * @description
 */
public interface TouchUserService extends IService<TouchUserEntity> {
    JSONObject touchUserInfo(String touchToken, String memberNo) throws IOException;

    JSONObject userLearning(String touchToken, String memberNo, Integer page, Integer pageSize) throws IOException;
}
