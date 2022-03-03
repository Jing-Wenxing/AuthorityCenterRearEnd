package atd.code.authority.response;

public enum ErrorEnum {
    E_401("401", "未登录"),
    E_500("500", "请求方式错误"),

    // 100编号
    E_501001("501001","账户已存在"),
    E_501002("501002","您已经注册过了"),
    E_501003("501003","您的手机号已被绑定，如若不记得密码，请返回登录页找回"),
    E_501004("501004","验证码未生成"),
    E_501005("501005","验证码已过期"),
    E_501006("501006","验证码不正确"),
    E_501007("501007","登录参数不全"),

    // 200编号
    E_502001("502001","密码错误"),
    E_502002("502002","未找到相关的账户信息"),
    E_502003("502003","验证码不正确，登录失败"),

    // 500编号
    E_505001("505001", "短信发送失败"),
    E_505002("505002","您的手机号已被绑定，如若不记得密码，请返回登录页找回"),
    E_505003("505003","您的手机验证码已过期"),
    E_505004("505004","您提交的手机号未绑定任何账号，请重新注册"),

    // 600编号
    E_506001("506001", "缺少必填参数");

    private String errorCode;
    private String errorMsg;

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
