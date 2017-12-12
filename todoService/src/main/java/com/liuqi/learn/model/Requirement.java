package com.liuqi.learn.model;/**
 * Created by icaru on 2017/7/26.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * <p>
 *     需求对象
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/26 23:15
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/26 23:15
 **/
@Entity
@Table(name = "n_requirement")
public class Requirement implements Comparable<Requirement>{
    @GeneratedValue
    @Id
    private Integer id;

    private String requirementId;

    private String taskNo;

    private String system;

    @Enumerated(EnumType.STRING)
    private RequirementStatus status;

    private String title;

    private String user;

    private String dept;

    @Column(name = "receive_date")
    private String receiveDate;

    @Column(name = "plan_date")
    private String planDate;

    @Column(name = "sit_date")
    private String sitDate;

    @Column(name = "online_date")
    private String onlineDate;

    @Column(name = "pay_date")
    private String payDate;

    private String corp;

    private String count;

    private String job;

    private String detail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public RequirementStatus getStatus() {
        return status;
    }

    public void setStatus(RequirementStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getSitDate() {
        return sitDate;
    }

    public void setSitDate(String sitDate) {
        this.sitDate = sitDate;
    }

    public String getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(String onlineDate) {
        this.onlineDate = onlineDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    @JsonIgnore
    public Boolean isCompleted() {
        //根据状态判断是否是完成状态
        if (null == this.status) {
            return false;
        }

        if (this.status == RequirementStatus.DISPOSED
                || this.status == RequirementStatus.COMPLETED
                || this.status == RequirementStatus.O_COMPLETED
                || this.status == RequirementStatus.PAUSED) {
            return true;
        }

        return false;
    }

    @Override
    public int compareTo(Requirement o) {
        //先按状态进行排序；先区分
        if (!isCompleted().equals(o.isCompleted())) {
            return isCompleted() ? 1 : -1;
        }

        //完成状态一样时；
        //先按计划完成时间，再按需求编号
        int ret = getPlanDate().compareTo(o.getPlanDate());
        if (0 != ret) {
            return ret;
        }

        //计划完成时间一样
        return this.getRequirementId().compareTo(o.getRequirementId());
    }
}
