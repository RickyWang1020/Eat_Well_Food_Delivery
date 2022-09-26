package com.abc.eatwell.filter;

import com.abc.eatwell.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Check whether the user is logged in
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    // path matcher
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // define the request paths that do not need to be filtered
        String[] urls = new String[]{
                "/employee/login",
                "employee/logout",
                "/server/**",
                "/front/**"
        };

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. get the URL for this request
        String requestURI = request.getRequestURI();

        // 2. check whether this request needs to be processed
        boolean check = checkRequestFilter(urls, requestURI);

        // 3. if no need to be processed, then just filter
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. check the login status of the user, if logged in, then also filter
        if (request.getSession().getAttribute("employee") != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. if not logged in, then response the data to the webpage using output stream
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * path matching and check whether this request can be filtered and thus do not need to be processed
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean checkRequestFilter(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean matched = PATH_MATCHER.match(url, requestURI);
            if (matched) return true;
        }
        return false;
    }
}