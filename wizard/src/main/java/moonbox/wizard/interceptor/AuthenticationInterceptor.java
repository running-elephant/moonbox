
package moonbox.wizard.interceptor;


import lombok.extern.slf4j.Slf4j;
import moonbox.wizard.consts.Constants;
import moonbox.wizard.enums.HttpCodeEnum;
import moonbox.wizard.security.Session;
import moonbox.wizard.security.TokenEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenEncoder tokenEncoder;

    private String tokenFlag = "newToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(Constants.TOKEN_HEADER_STRING);

        if (StringUtils.isEmpty(token) || !token.startsWith(Constants.TOKEN_PREFIX)) {
            log.warn("{} : Unknown token", request.getServletPath());
            response.setStatus(HttpCodeEnum.UNAUTHORIZED.getCode());
            response.getWriter().print("The resource requires authentication, which was not supplied with the request");
            return false;
        }

        if (!tokenEncoder.isValid(token)) {
            log.warn("{} : token validation failed", request.getServletPath());
            response.setStatus(HttpCodeEnum.UNAUTHORIZED.getCode());
            response.getWriter().print("Invalid token");
            return false;
        } else {
            Session session = tokenEncoder.decode(token);
            request.setAttribute(Constants.CURRENT_USER, session);
            request.setAttribute(tokenFlag, tokenEncoder.encode(session));
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object tc = request.getAttribute(tokenFlag);
        if (tc != null) {
            String tmpToken = (String) tc;
            if (!tmpToken.trim().isEmpty()) {
                response.setHeader(Constants.TOKEN_HEADER_STRING, tmpToken);
            }
        }
    }
}
