package io.live_mall.modules.server.utils;

public enum OrderEnum {
	/**
	 *  -1待报备
	 *  0:待审核  
	 *   1:已驳回
	 *    2：已过审
	 *    3 ：已退款
	 *     4：已成立
	 *      5：已结束
	 *       6：已退回 
	 */
	one(-1,"待报备"),
	tow(0,"待审核"),
	three(1,"已驳回"),
	four(2,"已过审"),
	five(3,"已退款"),
	six(4,"已成立"),
	qi(5,"已结束"),
	ba(6,"已退回 ");
	private Integer key;
	private String value;
	private OrderEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
	public Integer getKey() {
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public static String getV(Integer key) {
		OrderEnum[] values = OrderEnum.values();
		for (OrderEnum orderEnum : values) {
			if(orderEnum.getKey() == key) {
				return orderEnum.getValue();
			}
		}
		return "";
	}
	
	
	
}
