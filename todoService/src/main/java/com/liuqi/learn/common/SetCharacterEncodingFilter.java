package com.liuqi.learn.common;/**
 * Created by icaru on 2017/9/4.
 */

import org.springframework.context.annotation.ComponentScan;

import javax.servlet.*;
import java.io.IOException;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/4 9:09
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/4 9:09
 **/
public class SetCharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}