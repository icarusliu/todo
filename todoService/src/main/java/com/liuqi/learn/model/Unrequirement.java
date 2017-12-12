package com.liuqi.learn.model;/**
 * Created by icaru on 2017/7/19.
 */

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 22:13
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 22:13
 **/
@Entity
@Table(name = "n_unrequirement")
public class Unrequirement implements Comparable<Unrequirement>{
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private boolean status;

    @Enumerated(EnumType.STRING)
    private UnrequirementType type;

    @Enumerated(EnumType.STRING)
    private UnrequirementServer server;

    @Column(name = "receive_date")
    private String receiveDate;

    private String user;

    @OneToOne
    @JoinColumn(name = "dev")
    private User dev;

    @Column(name = "plan_date")
    private String planDate;

    @Column(name = "actual_date")
    private String actualDate;

    @Column(name = "plan_online_date")
    private String planOnlineDate;

    @Column(name = "actual_online_date")
    private String actualOnlineDate;

    private String requirementId;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Column(name = "parent")
    private Integer parent;

    @OneToMany(targetEntity = Unrequirement.class, cascade = CascadeType.ALL, mappedBy = "parent")
    @Fetch(FetchMode.SUBSELECT)
    private List<Unrequirement> children;

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UnrequirementType getType() {
        return type;
    }

    public void setType(UnrequirementType type) {
        this.type = type;
    }

    public UnrequirementServer getServer() {
        return server;
    }

    public void setServer(UnrequirementServer server) {
        this.server = server;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public User getDev() {
        return dev;
    }

    public void setDev(User dev) {
        this.dev = dev;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public String getPlanOnlineDate() {
        return planOnlineDate;
    }

    public void setPlanOnlineDate(String planOnlineDate) {
        this.planOnlineDate = planOnlineDate;
    }

    public String getActualOnlineDate() {
        return actualOnlineDate;
    }

    public void setActualOnlineDate(String actualOnlineDate) {
        this.actualOnlineDate = actualOnlineDate;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Override
    public int compareTo(Unrequirement o) {
        if (this.isStatus() == o.isStatus()) {
            //状态是一样的，先按计划完成时间排序
            if (getPlanDate() == null || "".equals(getPlanDate())) {
                if (o.getPlanDate() == null || "".equals(o.getPlanDate())) {
                    //计划完成时间一样，按提交时间排序
                    return -getReceiveDate().compareTo(o.getReceiveDate());
                }

                return -1;
            } else {
                if (o.getPlanDate() == null || "".equals(o.getPlanDate())) {
                    return 1;
                }

                return -getPlanDate().compareTo(o.getPlanDate());
            }
        } else if (!this.isStatus() ) {
            return -1;
        } else {
            return 1;
        }
    }

    public Integer getParent() {        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public List<Unrequirement> getChildren() {
        return children;
    }

    public void setChildren(List<Unrequirement> children) {
        this.children = children;
    }
}
