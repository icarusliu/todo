package com.liuqi.learn.common;/**
 * Created by icaru on 2017/9/4.
 */

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/4 9:21
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/4 9:21
 **/
@Configuration
public class MainConfigurer {
    @Bean
    public FilterRegistrationBean filterFileUpload() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SetCharacterEncodingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("setCharacterEncodingFilter");
        registration.setOrder(6);
        return registration;
    }
}
