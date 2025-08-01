package top.otsuland.market.common;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 拦截器
 * ok
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/**")
@Profile("prod")
public class LoginCheckFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String url = req.getRequestURL().toString();
        log.info("请求的 URL: {}", url);

        // 判断是否是登录或注册请求
        if(url.contains("login") || url.contains("register")) {
            log.info("login or register, ok!");
			chain.doFilter(request, response);
            return;
        }

        // 判断令牌是否存在
        String jwt = req.getHeader("token");
        if(!StringUtils.hasLength(jwt)) {
            log.info("not login, fail!");
			String notLogin = JSON.toJSONString(Result.set(0, "认证未认证！"));
            resp.getWriter().write(notLogin);
            return;
        }

        // 解析 token
        try {
            Integer userId = JwtUtils.validateJWT(jwt);
            log.info("令牌合法，放行！");
            // 将用户 id 存入 request attribute
            req.setAttribute("userId", userId);
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            String notLogin = JSON.toJSONString(Result.error("not_login"));
            resp.getWriter().write(notLogin);
            return;
        }
    }
}
