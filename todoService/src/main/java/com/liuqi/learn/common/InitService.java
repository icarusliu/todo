package com.liuqi.learn.common;

import com.liuqi.learn.jpa.repositories.UserRepository;
import com.liuqi.learn.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 初始化服务
 * 完成以下功能：
 * 1. 系统第一次启动时添加admin用户
 */
@Service
public class InitService implements InitializingBean{
    @Autowired
    private UserRepository ur;

    private static Logger logger = LoggerFactory.getLogger(InitService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Init service begin to execute...");
        }

        User user = ur.findUserByName("admin");
        if (null == user) {
            logger.warn("Admin does not exist, add it first!");
            user = new User();
            user.setName("admin");
            user.setPassword("123456");
            ur.save(user);
        } else {
            logger.debug("Admin already exists!");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Init service execution has completed!");
        }
    }
}
