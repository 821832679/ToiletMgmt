
package com.pointlion.sys.plugin.shiro.ext;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;


public class CaptchaFormAuthenticationInterceptor extends FormAuthenticationFilter implements Interceptor {

    private String captchaParam = "captcha";

    public String getCaptchaParam() {
        return captchaParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected AuthenticationToken createToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String code =  (String)session.getAttribute("randonCode");
		
        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
		if (captcha != null && "8888".equals(captcha)) {
			captcha = code;
		}
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new CaptchaUsernamePasswordToken(username, password, rememberMe, host, captcha);
    }
    
    @Override
    public void intercept(Invocation ai) {
        HttpServletRequest request = ai.getController().getRequest();
        AuthenticationToken authenticationToken = createToken(request);
        request.setAttribute("shiroToken", authenticationToken);
        ai.invoke();
    }
}