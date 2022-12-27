package io.live_mall.txai;

import lombok.Data;

/**
 * {
  "Response": {
    "Name": "齐惠清",
    "Sex": "女",
    "Nation": "汉",
    "Birth": "1983/11/22",
    "Address": "江苏省苏州市虎丘区济慈路168号17幢",
    "IdNum": "410425198311221049",
    "Authority": "",
    "ValidDate": "",
    "AdvancedInfo": "{}",
    "RequestId": "a7feddcf-9e97-46bd-94f7-f62f9ab56505"
  }
}
 * @author daitao
 */
@Data
public class CardModel {
	private String Name;
	private String Nation;
	private String Birth;
	private String Address;
	private String IdNum;
	private String Authority;
	private String AdvancedInfo;
	private String RequestId;
}
