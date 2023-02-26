package io.live_mall.modules.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.constants.MmsConstants;
import io.live_mall.modules.server.dao.MmsSmsLogDao;
import io.live_mall.modules.server.entity.MmsSmsLogEntity;
import io.live_mall.modules.server.service.MmsSmsLogService;
import io.live_mall.sms.mms.MmsClient;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yewl
 * @date 2023/2/25 15:58
 * @description
 */
@Service("mmsSmsLogService")
public class MmsSmsLogServiceImpl extends ServiceImpl<MmsSmsLogDao, MmsSmsLogEntity> implements MmsSmsLogService {
    @Override
    public Integer sendCode(String token, String phone, Integer code, Long userId) throws Exception {
        MmsSmsLogEntity entity = new MmsSmsLogEntity();
        String text = "Text1";
        JSONObject result = MmsClient.send(token, text, phone, String.valueOf(code), MmsConstants.MMS_SMS_CODE);
        Integer mmsCode = 1;
        if (result != null) {
            JSONObject content = result.getJSONObject("content");
            mmsCode = Integer.valueOf(content.getString("code"));
            entity.setCode(mmsCode);
            entity.setResult(result.toJSONString());
        }
        entity.setPhone(phone);
        entity.setVerificationCode(code);
        entity.setCreateTime(new Date());
        entity.setCreateUser(userId);
        this.baseMapper.insert(entity);
        return mmsCode;
    }
}
