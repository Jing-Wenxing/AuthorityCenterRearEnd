package atd.code.authority.service.impl;

import atd.code.authority.entity.Authority;
import atd.code.authority.entity.BaseInfo;
import atd.code.authority.entity.UserInfo;
import atd.code.authority.mapper.UserMapper;
import atd.code.authority.response.CommonJsonException;
import atd.code.authority.response.CommonUtil;
import atd.code.authority.response.ErrorEnum;
import atd.code.authority.service.UserService;
import atd.code.authority.utils.encrypt.SaltUtils;
import atd.code.authority.utils.redis.RedisUtils;
import atd.code.authority.utils.sms.SendMessageUtils;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * ===== 注册相关 =====
     */

    // 注册账号
    @Override
    public JSONObject register(String account, String password, String username, String telephone) {
        // 判断手机号
        if (userMapper.phoneCheck(telephone) != 1) {
            // 获取UUID
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            // 加密密码
            String salt = new SaltUtils().getSalt(6);

            // 创建基本信息
            BaseInfo baseInfo = new BaseInfo();
            baseInfo.setUuid(uuid);
            baseInfo.setAccount(account);
            baseInfo.setPassword(SaSecureUtil.md5BySalt(password, salt));
            baseInfo.setSalt(salt);

            // 创建个人信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUuid(uuid);
            userInfo.setTelephone(telephone);
            userInfo.setUsername(username);
            userInfo.setRole("guest");
            userInfo.setRoleid("15");

            if (userMapper.createBaseInfo(baseInfo) != 1) {
                return CommonUtil.failJson("账户信息录入失败");
            }

            if (userMapper.createUserInfo(userInfo) != 1) {
                return CommonUtil.failJson("个人信息录入失败");
            }
            // addPermissionList(userMapper.getUUIDToUserInfo(uuid).getRoleid());
            StpUtil.login(uuid);
            return CommonUtil.successJson("注册成功，点击确认完成登录");
        }
        // addPermissionList(userMapper.getUUIDToUserInfo(userMapper.getPhoneToUUID(telephone)).getRoleid());
        StpUtil.login(userMapper.getPhoneToUUID(telephone));
        return CommonUtil.successJson("您注册的手机号已经被注册，由于您已经验证了验证码，帮您自动登录，点击确认完成登录");
    }

    // 用户名重复
    @Override
    public JSONObject accountCheck(String account) {
        if (userMapper.accountCheck(account) != 0) {
            throw new CommonJsonException(ErrorEnum.E_501001);
        }
        return CommonUtil.successJson();
    }

    /**
     * ===== 登录相关 =====
     */

    // 手机登录
    @Override
    public JSONObject loginOfPhone(String telephone, String vercode) {
        // 验证通过用户，检测账号存在
        if (userMapper.phoneCheck(telephone) != 1) {
            // 获取UUID
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            // 创建基本信息
            BaseInfo baseInfo = new BaseInfo();
            baseInfo.setUuid(uuid);
            baseInfo.setAccount(telephone);

            // 创建个人信息
            UserInfo userInfo = new UserInfo();
            userInfo.setUuid(uuid);
            userInfo.setTelephone(telephone);
            userInfo.setUsername("手机用户" + telephone);
            userInfo.setRole("guest");
            userInfo.setRoleid("15");

            if (userMapper.createBaseInfo(baseInfo) != 1) {
                return CommonUtil.failJson("账户信息录入失败");
            }

            if (userMapper.createUserInfo(userInfo) != 1) {
                return CommonUtil.failJson("个人信息录入失败");
            }
        }
        // addPermissionList(userMapper.getUUIDToUserInfo(userMapper.getPhoneToUUID(telephone)).getRoleid());
        StpUtil.login(userMapper.getPhoneToUUID(telephone));
        return CommonUtil.successJson();
    }

    // 密码登录
    @Override
    public JSONObject loginOfPassword(String login_id, String password) {
        if (userMapper.accountCheck(login_id) == 1 || userMapper.phoneCheck(login_id) == 1) {
            BaseInfo baseInfo = userMapper.getUUIDToBaseInfo(userMapper.getAccountToUUID(login_id));
            UserInfo userInfo = userMapper.getUUIDToUserInfo(userMapper.getPhoneToUUID(login_id));

            if (baseInfo == null) {
                baseInfo = userMapper.getUUIDToBaseInfo(userInfo.getUuid());
            }

            String saltPassword = SaSecureUtil.md5BySalt(password, baseInfo.getSalt());
            // 验证密码
            if (baseInfo.getPassword().equals(saltPassword)) {
                // addPermissionList(userMapper.getUUIDToUserInfo(baseInfo.getUuid()).getRoleid());
                StpUtil.login(baseInfo.getUuid());
                return CommonUtil.successJson();
                // return CommonUtil.failJson("卡点");
            }
            return CommonUtil.errorJson(ErrorEnum.E_502001);
        }
        return CommonUtil.errorJson(ErrorEnum.E_502002);
    }

    /**
     * ===== 其他内容 =====
     */

    // 重置密码
    @Override
    public JSONObject reset(String telephone, String password) {
        // 验证通过用户，检测账号存在
        if (userMapper.phoneCheck(telephone) == 1) {

            // 加密密码
            String salt = new SaltUtils().getSalt(6);

            BaseInfo baseInfo = new BaseInfo();
            baseInfo.setUuid(userMapper.getPhoneToUUID(telephone));
            baseInfo.setPassword(SaSecureUtil.md5BySalt(password, salt));
            baseInfo.setSalt(salt);

            if (userMapper.setPassword(baseInfo) != 1) {
                return CommonUtil.failJson("重置失败");
            }
            return CommonUtil.successJson();
        }
        return CommonUtil.errorJson(ErrorEnum.E_505004);
    }

    /**
     * ===== 公共使用 =====
     */

    // 获取手机号验证码
    @Override
    public JSONObject getSMS(String telephone) {
        tencentSMS(telephone);
        return CommonUtil.successJson();
    }

    private JSONObject tencentSMS(String telephone) {
        // 发送手机号
        SendMessageUtils sendMessageUtils = new SendMessageUtils();
        String[] phoneNumbers = {telephone};
        // 验证码
        String id = sendMessageUtils.SendMsg(phoneNumbers);
        // Redis存放
        redisUtils.set(telephone, id, 5 * 60);
        return CommonUtil.successJson();
    }

    // 权限加成
    private void addPermissionList(String roleid) {
        List<Authority> authorityList = userMapper.getUUIDAuthority(roleid);
        for (Authority authority : authorityList) {
            System.out.println(authority.getPermission());
        }
        // System.out.println(userMapper.getUUIDAuthority(roleid));
    }
}
