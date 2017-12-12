package com.liuqi.tool.todo.ui.env;/**
 * Created by icaru on 2017/8/18.
 */

import com.liuqi.learn.model.Environment;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/18 10:24
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/18 10:24
 **/
public class EnvTableObject implements Comparable<EnvTableObject> {
    private String system;
    private List<Environment> envs;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public List<Environment> getEnvs() {
        return envs;
    }

    public void setEnvs(List<Environment> envs) {
        this.envs = envs;
    }

    @Override
    public int compareTo(EnvTableObject o) {
        return system.compareTo(o.getSystem());
    }
}
