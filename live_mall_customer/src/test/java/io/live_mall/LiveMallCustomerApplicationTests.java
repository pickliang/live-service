package io.live_mall;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LiveMallCustomerApplicationTests.class)
public class LiveMallCustomerApplicationTests {


    @Test
    public void card() throws TencentCloudSDKException {
        Credential cred = new Credential("AKIDA6A6mSLDfwUmnXJDDwNYv1rRl32mI7h0", "xg9MIxQffbHhrkMipgZCYeU5QWMIMBj0");
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        OcrClient client = new OcrClient(cred, "ap-beijing", clientProfile);

        IDCardOCRRequest req = new IDCardOCRRequest();
        req.setImageUrl("https://wx-wudo.oss-cn-beijing.aliyuncs.com/wx/20221219/7d3a4b2c24e24d6e891a0f2470f5328b.jpg");
        IDCardOCRResponse resp = client.IDCardOCR(req);
        System.out.println(resp);
    }

}
