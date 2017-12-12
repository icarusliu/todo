package com.liuqi.learn.model;/**
 * Created by icaru on 2017/7/4.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

/**
 * <p>
 *     用户对象
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/4 8:21
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/4 8:21
 **/
@Entity
@Table(name = "n_user")
public class User implements Comparable<User>{
    @Id
    @GeneratedValue
    private Long Id;

    private String name;

    private String password;

    private String corp;

    private String role;

    private Boolean status;

    @Column(name = "in_date")
    private String inDate;

    @Column(name = "out_date")
    private String outDate;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getRole() {
        return role;
    }

    /**
     * 是否是系统管理员
     *
     * @return
     */
    @JsonIgnore
    public boolean isAdmin() {
        return "admin".equals(role);
    }

    /**
     * 是否是公司管理员
     * @return
     */
    @JsonIgnore
    public boolean isCorpAdmin() {
        return "corpadmin".equals(role);
    }

    /**
     * 是否是普通用户
     * @return
     */
    @JsonIgnore
    public boolean isNormalUser() {
        return !isAdmin() && !isCorpAdmin();
    }

    /**
     * 用户是否是”所有人“
     * @return
     */
    @JsonIgnore
    public boolean isAll() {
        return this.name.equals("所有人");
    }

    /**
     * 是否是公司用户
     *
     * @return
     */
    @JsonIgnore
    public boolean isCorpUser() {
        return this.name.equals(this.corp);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(User o) {
        if (null == this.getStatus() && null == this.getStatus()) {
            return this.getCorp().compareTo(o.getCorp());
        } else if (null == this.getStatus()) {
            if (o.getStatus()) {
                return -1;
            } else {
                return this.getCorp().compareTo(o.getCorp());
            }
        } else if (null == o.getStatus()) {
            return this.getStatus() ? 1 : this.getCorp().compareTo(o.getCorp());
        } else {
            return this.getStatus().compareTo(o.getStatus());
        }
    }
}
