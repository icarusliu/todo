package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/17.
 */

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 17:58
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 17:58
 **/
public enum  EnvServerType {
    APP("应用服务器"),
    WEB("WEB服务器"),
    WEB_URL("WEB应用地址"),
    DB("数据库"),
    F5("单点地址"),
    DOMAIN("域名")
    ;

    private String name;

    EnvServerType(String name) {
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
