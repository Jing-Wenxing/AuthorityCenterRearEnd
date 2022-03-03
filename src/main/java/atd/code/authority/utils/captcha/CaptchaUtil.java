package atd.code.authority.utils.captcha;

import cn.dev33.satoken.session.SaSession;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CaptchaUtil {

    public String createCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException, NullPointerException {
        HttpSession session = request.getSession();

        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        Captcha specCaptcha = new SpecCaptcha(125, 40, 4);
        String verCode = specCaptcha.text().toLowerCase();

        // 验证码存入session
        session.setAttribute("captcha", verCode);
        session.setAttribute("captchaTime", System.currentTimeMillis());

        specCaptcha.out(response.getOutputStream());
        return verCode;
    }
}
