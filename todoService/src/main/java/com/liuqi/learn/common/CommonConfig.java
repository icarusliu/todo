package com.liuqi.learn.common;/**
 * Created by icaru on 2017/8/31.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/31 8:53
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/31 8:53
 **/
@ConfigurationProperties(prefix = "comm")
@Component
public class CommonConfig {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
