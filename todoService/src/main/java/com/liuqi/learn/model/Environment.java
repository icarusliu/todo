package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/17.
 */

import javax.persistence.*;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 17:52
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 17:52
 **/
@Entity
@Table(name = "n_environment")
public class Environment implements Comparable<Environment>{
    @Id
    @GeneratedValue
    private Integer id;
    private String system;

    @Enumerated(EnumType.STRING)
    private EnvironmentType type;

    @Column(name = "server_type")
    @Enumerated(EnumType.STRING)
    private EnvServerType serverType;

    private String address;

    private String users;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public EnvironmentType getType() {
        return type;
    }

    public void setType(EnvironmentType type) {
        this.type = type;
    }

    public EnvServerType getServerType() {
        return serverType;
    }

    public void setServerType(EnvServerType serverType) {
        this.serverType = serverType;
    }

    @Override
    public int compareTo(Environment o) {
        return type.ordinal() > o.getType().ordinal() ? 1 : (
                type.ordinal() == o.getType().ordinal() ? 0 : -1
                );
    }
}
