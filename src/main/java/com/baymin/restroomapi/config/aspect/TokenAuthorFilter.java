package com.baymin.restroomapi.config.aspect;

import com.baymin.restroomapi.config.aspect.jwt.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


/**
 * Created by baymin on 17-8-4.
 */
@Component
@Slf4j
public class TokenAuthorFilter implements Filter {



    //@Autowired
    //private WebSecurityConfig webSecurityConfig;
    //@Value("${server.filter.login-url}")
    //private String loginUrl;

    /**
     * kuayu-origin: japi.waterever.cn
     * kuayu-methods: japi.waterever.cn
     * kuayu-max-age: 3600
     * kuayu-headers: Origin, X-Requested-With, Content-Type, Accept
     */
    @Value("${kuayu-origin}")
    private String origin;
    @Value("${kuayu-methods}")
    private String methods;
    @Value("${kuayu-max-age}")
    private String maxAge;
    @Value("${kuayu-headers}")
    private String headers;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("authorization");



        //region 允许跨域
        response.setHeader("Access-Control-Allow-Origin", origin);
        //允许的访问方法
        response.setHeader("Access-Control-Allow-Methods", methods);
        //Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", maxAge);
        response.setHeader("Access-Control-Allow-Headers", headers);

        log.info("~\n");

        log.info("########TokenFilterFirst########");

        String uri = request.getRequestURI(); //uri就是获取到的连接地址!
        log.info("now url:" + uri);
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            if(name.equals("img"))continue;
            String value = request.getParameter(name);
            log.info("{}:{}",name,value);
        }
        if(uri.contains("/login.html")||
                uri.contains("/api/app/v1/work-line")||
                uri.contains("/api/app/v1/work-station")||
                uri.contains("/api/app/v1/work-post")||
                uri.contains("/api/app/v1/login")||
                uri.contains("/api/app/v1/password")||
                uri.contains("/api/app/v1/select")){
            filterChain.doFilter(servletRequest, response);
            return;
        }
        if(!uri.contains("/api/")){
            filterChain.doFilter(servletRequest, response);
            return;
        }
        if (uri.contains("/user/login")) {
            filterChain.doFilter(servletRequest, response);
        } else {

            log.info("***enter to filter***");
            if ("OPTIONS".equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                filterChain.doFilter(servletRequest, response);
                log.info(uri + "***OPTIONS 验证通过***");
            } else {

                //region 针对微信网页版特殊的校验
                final String wx = request.getHeader("reffers");
                if (wx != null) {
                    if (wx.equals("http://japi.waterever.cn")) {
                        filterChain.doFilter(servletRequest, response);
                        log.info("***special url,no filter***");
                        return;
                    }
                }
                //endregion

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);

                    String userId = TokenUtils.isValid(token);
                    if (!"overtime".equals(userId)) {
                        response.addHeader("user_id", userId);
                        log.info("***now userId:{}***",userId);
                        filterChain.doFilter(servletRequest, response);
                        log.info("########PassedTokenFilter########");
                        //response
                        return;
                    }
                }
                //验证不通过
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                //将验证不通过的错误返回
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(R.error(ResultEnum.FAIL_AUTH)));
                log.info("########NotPass########");
                //request.setAttribute("user_id","12323");
                return;
            }
        }
    }

    @Override
    public void destroy() {
    }
}

