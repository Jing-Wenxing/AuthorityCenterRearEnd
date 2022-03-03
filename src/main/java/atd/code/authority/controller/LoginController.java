package atd.code.authority.controller;

import cn.dev33.satoken.sso.SaSsoHandle;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    /**
     * Sa-Token公共处理
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoHandle.serverRequest();
    }
}