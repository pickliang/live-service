package io.live_mall.modules.server.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrgRegionModel {
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
     * sys_org主键ID
     */
    private String orgGroupId;
}
