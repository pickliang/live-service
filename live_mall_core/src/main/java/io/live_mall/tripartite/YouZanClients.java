package io.live_mall.tripartite;

import com.alibaba.fastjson.JSON;
import com.youzan.cloud.open.sdk.common.exception.SDKException;
import com.youzan.cloud.open.sdk.core.client.auth.Token;
import com.youzan.cloud.open.sdk.core.client.core.DefaultYZClient;
import com.youzan.cloud.open.sdk.core.oauth.model.OAuthToken;
import com.youzan.cloud.open.sdk.core.oauth.token.TokenParameter;
import com.youzan.cloud.open.sdk.gen.v1_0_0.api.YouzanUsersInfoQuery;
import com.youzan.cloud.open.sdk.gen.v1_0_0.model.YouzanScrmCustomerDetailGetResult;
import com.youzan.cloud.open.sdk.gen.v1_0_0.model.YouzanUsersInfoQueryParams;
import com.youzan.cloud.open.sdk.gen.v1_0_0.model.YouzanUsersInfoQueryResult;
import com.youzan.cloud.open.sdk.gen.v1_0_1.api.YouzanScrmCustomerDetailGet;
import com.youzan.cloud.open.sdk.gen.v1_0_1.model.YouzanScrmCustomerDetailGetParams;
import com.youzan.cloud.open.sdk.gen.v4_0_2.api.YouzanTradesSoldGet;
import com.youzan.cloud.open.sdk.gen.v4_0_2.model.YouzanTradesSoldGetParams;
import com.youzan.cloud.open.sdk.gen.v4_0_2.model.YouzanTradesSoldGetResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yewl
 * @date 2023/2/19 10:35
 * @description 有赞
 */
@Slf4j
public class YouZanClients {

    private static final String CLIENT_ID = "e427c19573a8816bbd";
    private static final String CLIENT_SECRET = "7103d6ebcab0f253ecf24c9d9a5552de";
    private static final String GRANT_ID = "108048817";
    
    
    /**
     * 有赞token
     * @return
     * @throws SDKException
     */
    public static OAuthToken token() throws SDKException {
        DefaultYZClient yzClient = new DefaultYZClient();
        TokenParameter tokenParameter = TokenParameter.self()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .grantId(GRANT_ID)
                .build();
        OAuthToken oAuthToken = yzClient.getOAuthToken(tokenParameter);
        log.error("token-->{}", JSON.toJSON(oAuthToken));
        return oAuthToken;
    }

    /**
     * https://doc.youzanyun.com/detail/API/0/2193 用户列表
     * https://doc.youzanyun.com/detail/API/0/1433 查询用户详细信息
     * 1.获取yz_open_id
     * 2.获取详细信息
     *
     * @param accessToken token
     * @param mobile      手机号
     * @return
     * @throws SDKException
     */
    public static List<YouzanUsersInfoQueryResult.YouzanUsersInfoQueryResultUserlist> userList(String accessToken, String mobile) throws SDKException {
        DefaultYZClient yzClient = new DefaultYZClient();
        YouzanUsersInfoQuery usersInfoQuery = new YouzanUsersInfoQuery();
        YouzanUsersInfoQueryParams params = new YouzanUsersInfoQueryParams();
        params.setMobile("+86-" + mobile);
        usersInfoQuery.setAPIParams(params);
        Token token = new Token(accessToken);
        YouzanUsersInfoQueryResult result = yzClient.invoke(usersInfoQuery, token, YouzanUsersInfoQueryResult.class);
        log.error("result-->{}", JSON.toJSON(result));
        YouzanUsersInfoQueryResult.YouzanUsersInfoQueryResultData data = result.getData();
        List<YouzanUsersInfoQueryResult.YouzanUsersInfoQueryResultUserlist> userList = data.getUserList();
        return userList;
    }

    public static  YouzanScrmCustomerDetailGetResult.YouzanScrmCustomerDetailGetResultData userDetail(String accessToken, String yzOpenId, String mobile) throws SDKException {
        DefaultYZClient yzClient = new DefaultYZClient();
        // 用户详细信息
        YouzanScrmCustomerDetailGet detailGet = new YouzanScrmCustomerDetailGet();
        YouzanScrmCustomerDetailGetParams detailGetParams = new YouzanScrmCustomerDetailGetParams();
        YouzanScrmCustomerDetailGetParams.YouzanScrmCustomerDetailGetParamsAccountinfo accountInfo = new YouzanScrmCustomerDetailGetParams.YouzanScrmCustomerDetailGetParamsAccountinfo();
        accountInfo.setAccountId(mobile);
        accountInfo.setAccountType(2);
        detailGetParams.setAccountInfo(accountInfo);
        detailGetParams.setYzOpenId(yzOpenId);
        detailGetParams.setFields("user_base,tags,benefit_cards,benefit_level,benefit_rights,credit,behavior,giftcard,prepaid,coupon,level,auth_info");
        detailGet.setAPIParams(detailGetParams);
        Token token = new Token(accessToken);
        YouzanScrmCustomerDetailGetResult detailGetResult = yzClient.invoke(detailGet, token, YouzanScrmCustomerDetailGetResult.class);
        log.error("detailGetResult-->{}", JSON.toJSON(detailGetResult));
        return detailGetResult.getData();
    }

    /**
     * https://doc.youzanyun.com/detail/API/0/2971
     * 获取交易的订单
     * @param accessToken token
     * @param yzOpenId 有赞用户openId
     * @throws SDKException
     */
    public static YouzanTradesSoldGetResult.YouzanTradesSoldGetResultData orderList(String accessToken, String yzOpenId) throws SDKException {
        //1.创建 YZClient
        DefaultYZClient yzClient = new DefaultYZClient();
        YouzanTradesSoldGet tradesSoldGet = new YouzanTradesSoldGet();
        YouzanTradesSoldGetParams params = new YouzanTradesSoldGetParams();
        params.setYzOpenId(yzOpenId);
        params.setPageSize(100);
        tradesSoldGet.setAPIParams(params);
        Token token = new Token(accessToken);
        YouzanTradesSoldGetResult result = yzClient.invoke(tradesSoldGet, token, YouzanTradesSoldGetResult.class);
        log.error("result-->{}", JSON.toJSON(result));
        return result.getData();

    }
}
