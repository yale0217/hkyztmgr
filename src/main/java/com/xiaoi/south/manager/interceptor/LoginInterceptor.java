package com.xiaoi.south.manager.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@PropertySource("classpath:/application.yml")
public class LoginInterceptor implements HandlerInterceptor {
    // 不验证URL anon：不验证/authc：受控制的
    public static final String NO_INTERCEPTOR_PATH =".*/((.css)|(.js)|(images)|(login)|(anon)|(error)).*";
    @Value("${manager.login.url}")
    private String managerUrl;
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse httpServletResponse, Object o) throws Exception {
//        System.out.println("开始拦截.........");
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        String origin = request.getHeader("Origin");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","Origin,Content-Type,Accept,token,X-Requested-With");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        String path = request.getServletPath();
        if (path.matches(NO_INTERCEPTOR_PATH)) {
            return true;
        } else {
            // 这写你拦截需要干的事儿，比如取缓存，SESSION，权限判断等
            Object username = request.getSession().getAttribute("session_user");
            System.out.println("SESSION---》user = " + username);
            //上线是打开注释
            if (username == null){
                httpServletResponse.sendRedirect(managerUrl);//拦截后跳转的方法
                System.out.println("已成功拦截并转发跳转");
                return false;
            }else{
                return true;
            }

        }
//       return true;
    }
    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行
     * （主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
