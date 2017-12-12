package com.liuqi.learn.model;/**
 * Created by icaru on 2017/7/19.
 */

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 22:30
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 22:30
 **/
public enum UnrequirementServer {
    WEEK_REPORT("周报")
    , EMAIL("邮件")
    , FLOW("流程")
            ;

    private UnrequirementServer(String name) {
        this.name = name;
    }

    private String name;

    @JsonValue
    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
