package com.liuqi.learn.model;/**
 * Created by icaru on 2017/7/26.
 */

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/26 23:23
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/26 23:23
 **/
public enum RequirementStatus {
    STATISTICS("需求分析"),
    DEVELOPING("开发中"),
    TESTING("业务测试"),
    REPAIR("问题处理"),
    WAIT_ONLINE("待上线"),
    PRDTESTING("生产验证"),
    PRDVERIFYING("业务验证"),
    DISPOSED("废弃"),
    PAUSED("暂缓"),
    COMPLETED("完成"),
    O_COMPLETED("2016完成");

    private String name;

    private RequirementStatus(String name) {
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
