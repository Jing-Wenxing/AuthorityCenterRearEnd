package atd.code.authority.utils.sms;

import atd.code.authority.response.CommonJsonException;
import atd.code.authority.response.ErrorEnum;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import java.util.Random;

public class SendMessageUtils {
    public String SendMsg(String[] phoneNumbers) {
        // APIID
        String secretId = "";
        // API密钥
        String secretKey = "";

        // 短信应用ID
        String appid = "";
        // 短信模板ID
        String templateID = "";
        // 短信签名内容
        String sign = "";

        try {
            Credential cred = new Credential(secretId, secretKey);

            ClientProfile clientProfile = new ClientProfile();

            clientProfile.setSignMethod("HmacSHA256");
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            SendSmsRequest req = new SendSmsRequest();

            req.setSmsSdkAppId(appid);
            req.setSignName(sign);
            req.setTemplateId(templateID);
            req.setPhoneNumberSet(phoneNumbers);
            String[] vercode = getRegisterCode(6);
            req.setTemplateParamSet(vercode);
            SendSmsResponse res = client.SendSms(req);
            return vercode[0];
        } catch (TencentCloudSDKException e) {
            throw new CommonJsonException(ErrorEnum.E_505001);
        }
    }

    public String[] getRegisterCode(int n) {
        char[] chars = "01234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            stringBuilder.append(aChar);
        }

        // 组装验证码
        String[] templateParams = new String[1];
        templateParams[0] = stringBuilder.toString();
        return templateParams;
    }
}
