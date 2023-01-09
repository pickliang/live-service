package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yewl
 * @date 2022/12/22 15:58
 * @description
 */
@Data
@TableName("c_org_region")
public class OrgRegionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 省市名称
     */
    @NotBlank(message = "名称不可为空")
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 删除状态 0-未删除  1-已删除
     */
    private Integer delFlag;
}
