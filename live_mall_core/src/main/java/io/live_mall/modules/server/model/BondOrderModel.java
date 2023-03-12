package io.live_mall.modules.server.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yewl
 * @date 2023/3/12 15:08
 * @description
 */
@Data
public class BondOrderModel {
    /**
     * 日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date date;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 份额
     */
    private Integer portion;

    /**
     * 净值
     */
    private BigDecimal worth;

    /**
     * 收益
     */
    private String income;

    /**
     * 附件
     */
    private String appendix;
}
