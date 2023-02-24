package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.modules.server.dao.TouchUserDao;
import io.live_mall.modules.server.entity.TouchUserEntity;
import io.live_mall.modules.server.service.TouchUserService;
import io.live_mall.tripartite.TouchClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yewl
 * @date 2023/2/23 16:28
 * @description
 */
@Service("touchUserService")
public class TouchUserServiceImpl extends ServiceImpl<TouchUserDao, TouchUserEntity> implements TouchUserService {
    @Override
    public JSONObject touchUserInfo(String touchToken, String memberNo) throws IOException {
        JSONObject result = new JSONObject();
        String name = "";
        String phoneCollect = "";
        // 今日学习时长
        Integer studyTime = 0;
        TouchUserEntity entity = this.baseMapper.selectOne(Wrappers.lambdaQuery(TouchUserEntity.class).eq(TouchUserEntity::getMemberNo, memberNo).last("LIMIT 1"));
        if (Objects.nonNull(entity)) {
            name = entity.getName();
            phoneCollect = entity.getPhoneCollect();
            // 昨日学习记录
            JSONObject learningDaily = TouchClients.learningDaily(touchToken, entity.getUserId());
            if (Objects.nonNull(learningDaily)) {
                JSONObject learningDailyData = learningDaily.getJSONObject("data");
                JSONArray dailyDataJSONArray = learningDailyData.getJSONArray("list");
                if (!dailyDataJSONArray.isEmpty()) {
                    for (Object arr : dailyDataJSONArray) {
                        JSONObject daily = (JSONObject) arr;
                        studyTime +=daily.getInteger("stay_time");
                    }
                }
            }

        }
        result.put("name", name);
        result.put("phoneCollect", phoneCollect);
        result.put("studyTime", studyTime);
        return result;
    }

    @Override
    public JSONObject userLearning(String touchToken, String memberNo, Integer page, Integer pageSize) throws IOException {
        TouchUserEntity entity = this.baseMapper.selectOne(Wrappers.lambdaQuery(TouchUserEntity.class).eq(TouchUserEntity::getMemberNo, memberNo).last("LIMIT 1"));
        JSONObject result = new JSONObject();
        Integer total = 0;
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (Objects.nonNull(entity)) {
            JSONObject learning = TouchClients.learning(touchToken, entity.getUserId(), page, pageSize);
            JSONObject data = learning.getJSONObject("data");
            // 总条数
            total = data.getInteger("total");
            JSONArray goodsList = data.getJSONArray("goods_list");
            if (!goodsList.isEmpty()) {
                for (Object record : goodsList) {
                    JSONObject jb = new JSONObject();
                    JSONObject json = (JSONObject) record;
                    JSONObject info = json.getJSONObject("info");
                    //  课程名称
                    String title = info.getString("title");
                    // 内容的类型 图文-1，音频-2，视频-3，直播-4，电子书-20
                    Integer goodsType = info.getInteger("goods_type");
                    // 课程类型
                    String type = goodsType == 1 ? "图文" : goodsType == 2 ? "音频" : goodsType == 3 ? "视频" :goodsType == 4 ? "直播" : goodsType == 20 ? "电子书" : "其他";
                    // 是否已学完 1-已学完
                    Integer isFinish = json.getInteger("is_finish");
                    // 学习状态
                    String finish = isFinish == 1 ? "已学完" : "未学完";
                    // 最大学习进度 0-100
                    Integer maxLearnProgress = json.getInteger("max_learn_progress");
                    // 首次学习时间
                    String createdAt = json.getString("created_at");
                    // 最近一次学习时间，默认按该字段倒序排序
                    String lastLearnTime = json.getString("last_learn_time");
                    jb.put("title", title);
                    jb.put("type", type);
                    jb.put("finish", finish);
                    jb.put("maxLearnProgress", maxLearnProgress + "%");
                    jb.put("createdAt", createdAt);
                    jb.put("lastLearnTime", lastLearnTime);
                    jsonObjects.add(jb);
                }
            }
        }
        result.put("currPage", page);
        result.put("pageSize", pageSize);
        result.put("totalCount", total);
        result.put("totalPage", (int)Math.ceil((double)total/pageSize));
        result.put("list", jsonObjects);
        return result;
    }
}
