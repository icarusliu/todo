package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/17.
 */

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.boot.test.util.EnvironmentTestUtils;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 17:55
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 17:55
 **/
public enum EnvironmentType {
    DEV("开发环境"),
    TEST("测试环境"),
    PRD("生产环境")
    ;

    private String name;

    EnvironmentType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
