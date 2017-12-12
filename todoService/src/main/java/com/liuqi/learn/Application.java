package com.liuqi.learn; /**
 * Created by icaru on 2017/7/2.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/2 23:10
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/2 23:10
 **/
@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    @PostConstruct
    private void init() {
        logger.debug("Start to init application...");

        //加载thymeleaf模板解析语言，使用thymeleaf布局用； 默认已经添加
//        LayoutDialect dialect = new LayoutDialect();
//        engine.addDialect(dialect);
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error_401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error_404.html");
//                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error_500.html");
                container.addErrorPages(error401Page, error404Page);
            }
        };
    }

    /**
     * 获取模板引擎；
     * 在前台界面可以使用标签获取登录的用户信息
     *
     * @return
     */
//    @Bean
//    public SpringTemplateEngine getTemplateEngine() {
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        SpringSecurityDialect dialect = new SpringSecurityDialect();
//        Set<IDialect> set = new HashSet<IDialect>();
//        set.add(dialect);
//
//        engine.setAdditionalDialects(set);
//
//        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
//
//        resolver.setPrefix("/templates");
//        resolver.setSuffix(".html");
//        resolver.setOrder(engine.getTemplateResolvers().size());
//
//        engine.setTemplateResolver(resolver);
//
//        return engine;
//    }
}
