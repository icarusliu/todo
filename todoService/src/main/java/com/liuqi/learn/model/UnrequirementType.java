package com.liuqi.learn.model;/**
 * Created by icaru on 2017/7/19.
 */

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 22:18
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 22:18
 **/
public enum UnrequirementType {
    DEV("需求开发")
    , OPTIMIZE("需求优化")
    , SYS_OPTIMIZE("系统优化")
    , PROBLEM_PROCESS("问题处理")
    , BUSI_CONSULT("业务咨询")
    , DATA_FETCH("取数需求")
    , TESTING_COORPERATE("配合测试")
    , DISPOSED("废弃")
    , OTHER("其它")
            ;

    private UnrequirementType(String name) {
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
