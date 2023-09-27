package com.hrc.takeOut.filter;

import com.alibaba.fastjson.JSON;
import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.utils.ThreadLocals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 检查用户是否登录
 * */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //转换为Http协议子接口
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取请求的URI
        String requestURI = request.getRequestURI();

        //定义需要过滤的uri  静态资源需要放行和登录与退出请求
        /**todo 错误❌
         * 静态资源必须放行，否则前端无法显示页面
         * */
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //判断请求uri是否为需要过滤的uri
        boolean check = check(uris, requestURI);
        //如果是需要过滤的直接过滤不拦截
        if (check) {
            log.info("本次放行uri:{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //判断是否登录，若登录直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("已登录 本次放行uri:{}", requestURI);
            Long id = (Long) request.getSession().getAttribute("employee");
            ThreadLocals.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("user") != null) {
            log.info("已登录 本次放行uri：{}",requestURI);
            Long userId = (Long) request.getSession().getAttribute("user");
            ThreadLocals.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        //返回特定信息让前端知道该用户未登录
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }

    private static boolean check(String[] uris, String requestURI) {
        for (String uri : uris) {
            if (PATH_MATCHER.match(uri, requestURI))
                return true;
        }
        return false;
    }
}
