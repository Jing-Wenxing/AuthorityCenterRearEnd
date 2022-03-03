package atd.code.authority.response;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Pattern;

public class CommonUtil {
    /**
     * 返回一个默认成功消息的json
     */
    public static JSONObject successJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("Status", Constants.SUCCESS_STATUS);
        resultJson.put("Result", Constants.SUCCESS_MSG);
        return resultJson;
    }

    /**
     * 返回一个成功消息的json
     */
    public static JSONObject successJson(Object info) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("Status", Constants.SUCCESS_STATUS);
        resultJson.put("Result", info);
        return resultJson;
    }

    /**
     * 返回一个默认失败消息的json
     */
    public static JSONObject failJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("Status", Constants.ERROR_STATUS);
        resultJson.put("Result", Constants.ERROR_MSG);
        return resultJson;
    }

    /**
     * 返回一个失败消息的json
     */
    public static JSONObject failJson(Object info) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("Status", Constants.ERROR_MSG);
        resultJson.put("Result", info);
        return resultJson;
    }

    public static JSONObject errorJson(ErrorEnum errorEnum) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("Status", Constants.ERROR_STATUS);
        JSONObject info = new JSONObject();
        info.put("msg",errorEnum.getErrorMsg());
        resultJson.put("Result", info);
        return resultJson;
    }

    public static boolean EmailChecker(String email){
        return Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",email);
    }
}
