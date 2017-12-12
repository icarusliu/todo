package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/22.
 */

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/22 10:24
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/22 10:24
 **/
public enum WorkOffType {
    ILL("病假"),
    ISSUE("事假"),
    MARRY("婚假"),
    YEAR("年假"),
    DEAD("丧假"),
    LAW("其它法定假期");

    private String name;

    WorkOffType(String name) {
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
