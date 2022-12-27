package io.live_mall.modules.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员消息订阅表
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-09-23 22:54:07
 */
@Data
@TableName("p_member_template_msg")
public class MemberTemplateMsgEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type=IdType.AUTO)
	private Long id;
	/**
	 * 会员ID
	 */
	private Long userId;
	
	private String wxId;
	/**
	 * 业务id：专场拍卖id，物品id，鉴定订单id
	 */
	private String busId;

	/**
	 * 模板ID
	 */
	private String templateId;
	
	/**
	 * 0:未推送，1已推送
	 */
	private String status;
	/**
	 * 
	 */
	private String callbakMsg;
	/**
	 * 业务类型
	 */
	private String msgType;
	
	private String page;
	
	
	private String data;
	
	private String wxVersion;
	

	
	
}
