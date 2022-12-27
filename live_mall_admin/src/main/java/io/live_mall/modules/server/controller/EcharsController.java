package io.live_mall.modules.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.live_mall.common.utils.R;
import io.live_mall.modules.sys.service.EcharsService;



/**
 * 图表
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
@RestController
@RequestMapping("echars/")
public class EcharsController {
    @Autowired
    private EcharsService echarsService;

    /**
     * 列表
     */
    @RequestMapping("/geTj1")
    public R geTj1(@RequestBody JSONObject data){
        return R.ok().put("data", echarsService.geTj1(data));
    }


    @RequestMapping("/geTj2")
    public R geTj2(@RequestBody JSONObject data){
        return R.ok().put("data", echarsService.geTj2(data));
    }
    
    
    @RequestMapping("/geTj3")
    public R geTj3(@RequestBody JSONObject data){
        return R.ok().put("data", echarsService.geTj3(data));
    }
    
    
    /**
     * 订单统计
     */
    @RequestMapping("/geTj4")
    public R geTj4(@RequestBody JSONObject data){
        return R.ok().put("data", echarsService.geTj4(data));
    }
    
    
}
