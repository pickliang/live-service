package io.live_mall.modules.server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/3/11 16:30
 * @description
 */
@Data
public class OrderBonusModel {
    /**
     * id
     */
    private String id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 类型 1-股权 2-证券
     */
    private int type;

    /**
     * 分红金额
     */
    private BigDecimal money;

    /**
     * 分红日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date date;
    /**
     * 分红次数
     */
    private Integer frequency;
    /**
     * 备注
     */
    private String remark;

    /**
     * 附件
     */
    private String appendix;
}
