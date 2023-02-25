package io.live_mall.modules.server.controller;

import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.entity.LivePlaybackEntity;
import io.live_mall.modules.server.service.LivePlaybackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @author yewl
 * @date 2023/2/25 9:55
 * @description 直播回放
 */
@RestController
@RequestMapping("server/live")
@AllArgsConstructor
@Slf4j
public class LivePlaybackController {
    private final LivePlaybackService livePlaybackService;

    /**
     * 直播回放列表
     * @param params
     * @return
     */
    @GetMapping(value = "/list")
    public R list(@RequestParam Map<String, Object> params) {
        return R.ok().put("data", livePlaybackService.pages(params));
    }

    /**
     * 保存回放链接
     * @param entity
     * @return
     */
    @PostMapping(value = "/save")
    public R save(@RequestBody LivePlaybackEntity entity) {
        entity.setDelFlag(0);
        entity.setIsShow(1);
        entity.setCreateTime(new Date());
        entity.setCreateUser(ShiroUtils.getUserId());
        boolean save = livePlaybackService.save(entity);
        return save ? R.ok() : R.error();
    }

    /**
     * 是否展示在首页
     * @return
     */
    @GetMapping(value = "/update-show/{id}")
    public R update(@PathVariable("id") String id) {
        int isShow = livePlaybackService.updateIsShow(id);
        return isShow > 0 ? R.ok() : R.error("数据已更新");
    }
}
