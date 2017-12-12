package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/22.
 */

import javax.persistence.*;

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
@Entity
@Table(name = "n_workoff")
public class WorkOff implements Comparable<WorkOff> {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    @Enumerated(EnumType.STRING)
    private WorkOffType type;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private String days;

    @Transient
    private String corp;

    @Column(name = "start_date")
    private String startDate;

    private String remark;

    public String getCorp() {
        return user.getCorp();
    }

    public Integer getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WorkOffType getType() {
        return type;
    }

    public void setType(WorkOffType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int compareTo(WorkOff o) {
        return o.getId().compareTo(id);
    }
}
