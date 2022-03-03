package atd.code.authority.controller;

import atd.code.authority.response.CommonJsonException;
import atd.code.authority.response.CommonUtil;
import atd.code.authority.response.ErrorEnum;
import atd.code.authority.service.UserService;
import atd.code.authority.utils.captcha.CaptchaUtil;
import atd.code.authority.utils.redis.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/authority")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * ===== 注册相关 =====
     */

    // 注册账号
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONObject register(@RequestBody JSONObject requestJson) {
        String account = requestJson.getString("account");
        String password = requestJson.getString("password");
        String username = requestJson.getString("username");
        String telephone = requestJson.getString("telephone");

        if (!StringUtils.hasLength(account) ||
                !StringUtils.hasLength(password) ||
                !StringUtils.hasLength(username) ||
                !StringUtils.hasLength(telephone)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }

        return userService.register(account, password, username, telephone);
    }

    // 用户名重复
    @RequestMapping(value = "/accountCheck", method = RequestMethod.POST)
    public JSONObject accountCheck(@RequestBody JSONObject requestJson) {
        String account = requestJson.getString("account");
        if (!StringUtils.hasLength(account)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }
        return userService.accountCheck(account);
    }

    /**
     * ===== 登录相关 =====
     */

    // 手机登录
    @RequestMapping(value = "/loginOfPhone", method = RequestMethod.POST)
    public JSONObject loginOfPhone(@RequestBody JSONObject requestJson) {
        String telephone = requestJson.getString("telephone");
        String vercode = requestJson.getString("vercode");

        if (!StringUtils.hasLength(telephone) ||
                !StringUtils.hasLength(vercode)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }

        if (redisUtils.hasKey(telephone)) {
            if (vercode.equals(redisUtils.get(telephone))) {
                redisUtils.del(telephone);
                return userService.loginOfPhone(telephone, vercode);
            }
            return CommonUtil.errorJson(ErrorEnum.E_502003);
        }
        return CommonUtil.errorJson(ErrorEnum.E_505003);
    }

    // 密码登录
    @RequestMapping(value = "/loginOfPassword", method = RequestMethod.POST)
    public JSONObject loginOfPassword(HttpServletRequest request, @RequestBody JSONObject requestJson) {
        String login_id = requestJson.getString("login_id");
        String password = requestJson.getString("password");
        String vercode = requestJson.getString("vercode");

        if (!StringUtils.hasLength(login_id) ||
                !StringUtils.hasLength(password) ||
                !StringUtils.hasLength(vercode)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }

        HttpSession session = request.getSession();
        // 验证码未生成
        if (session.getAttribute("captcha") == null) {
            throw new CommonJsonException(ErrorEnum.E_501004);
        }

        // 三分钟验证码过期
        if (System.currentTimeMillis() - (long) session.getAttribute("captchaTime") > 180000) {
            throw new CommonJsonException(ErrorEnum.E_501005);
        }

        // 验证码校验
        if (!requestJson.getString("vercode").toLowerCase().equals(session.getAttribute("captcha").toString().toLowerCase())) {
            throw new CommonJsonException(ErrorEnum.E_501006);
        }

        return userService.loginOfPassword(login_id, password);
    }

    /**
     * ===== 其他内容 =====
     */

    // 重置密码
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public JSONObject reset(@RequestBody JSONObject requestJson) {
        String telephone = requestJson.getString("telephone");
        String vercode = requestJson.getString("vercode");
        String password = requestJson.getString("password");

        if (!StringUtils.hasLength(telephone) ||
                !StringUtils.hasLength(vercode) ||
                !StringUtils.hasLength(password)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }

        if (redisUtils.hasKey(telephone)) {
            if (vercode.equals(redisUtils.get(telephone))) {
                redisUtils.del(telephone);
                return userService.reset(telephone, password);
            }
            return CommonUtil.errorJson(ErrorEnum.E_502003);
        }
        return CommonUtil.errorJson(ErrorEnum.E_505003);
    }

    /**
     * ===== 公共使用 =====
     */

    // 获取手机号验证码
    @RequestMapping(value = "/getSMS", method = RequestMethod.POST)
    public JSONObject getSMS(@RequestBody JSONObject requestJson) {
        String telephone = requestJson.getString("telephone");
        if (!StringUtils.hasLength(telephone)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }
        return userService.getSMS(telephone);
    }

    // 验证手机号验证码
    @RequestMapping(value = "/checkSMS", method = RequestMethod.POST)
    public JSONObject checkSMS(@RequestBody JSONObject requestJson) {
        String telephone = requestJson.getString("telephone");
        String vercode = requestJson.getString("vercode");

        if (!StringUtils.hasLength(telephone) ||
                !StringUtils.hasLength(vercode)) {
            throw new CommonJsonException(ErrorEnum.E_506001);
        }

        if (redisUtils.hasKey(telephone)) {
            if (vercode.equals(redisUtils.get(telephone))) {
                redisUtils.del(telephone);
                return CommonUtil.successJson();
            }
            return CommonUtil.errorJson(ErrorEnum.E_502003);
        }
        return CommonUtil.errorJson(ErrorEnum.E_505003);
    }

    // 获取验证码
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaUtil captcha = new CaptchaUtil();
        captcha.createCaptcha(request, response);
    }
}
