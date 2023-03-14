package cn.geterror.base.interceptor;


import cn.geterror.base.util.BAUtil;
import com.google.common.net.HttpHeaders;
import com.sun.tools.internal.ws.wsdl.document.http.HTTPConstants;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sun.misc.CharacterEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

public class BAInterceptor extends HandlerInterceptorAdapter {
    private final BAUtil baUtil;
    private final long expiredTime;//分钟
    public BAInterceptor(BAUtil baUtil,long expiredTime){
        this.baUtil = baUtil;
        this.expiredTime = expiredTime;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String authorization = request.getHeader("Authorization");
        String date = request.getHeader("Date");
        if(authorization==null||date==null){
            response.setStatus(401);
            response.getWriter().print("请补全Authorization和Date两个headers字段");
            return false;
        }
        if(!baUtil.auth(uri, method, authorization, date, expiredTime)){
            response.setStatus(401);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("无权限或权限已过期");
            return false;
        }
        return true;
    }
}
