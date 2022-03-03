package atd.code.authority.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    /**
     * ===== 注册相关 =====
     */

    // 注册账号
    JSONObject register(String account, String password, String username, String telephone);

    // 用户名重复
    JSONObject accountCheck(String account);

    /**
     * ===== 登录相关 =====
     */

    // 手机登录
    JSONObject loginOfPhone(String telephone, String vercode);

    // 密码登录
    JSONObject loginOfPassword(String login_id, String password);

    /**
     * ===== 其他内容 =====
     */

    // 重置密码
    JSONObject reset(String telephone, String password);

    /**
     * ===== 公共使用 =====
     */

    // 获取手机号验证码
    JSONObject getSMS(String telephone);
}
