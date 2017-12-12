package com.liuqi.learn.model;/**
 * Created by icaru on 2017/9/15.
 */

import javax.persistence.*;

/**
 * <p>
 *     重跑记录
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/15 9:46
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/15 9:46
 **/
@Entity
@Table(name = "n_rerun")
public class ReRun implements Comparable<ReRun> {
    @Id
    @GeneratedValue
    private Integer id;

    //重跑原因
    private String reason;

    //反馈人
    @Column(name = "feedback_user")
    private String feedbackUser;

    //反馈时间
    @Column(name = "feedback_date")
    private String feedbackDate;

    //确认人
    @Column(name = "confirm_user")
    private String confirmUser;

    //确认时间
    @Column(name = "confirm_date")
    private String confirmDate;

    //确认邮件标题
    @Column(name = "confirm_title")
    private String confirmTitle;

    //涉及报表
    private String reports;

    //重跑的数据日期
    @Column(name = "data_date")
    private String dataDate;

    //操作人
    private String operator;

    //操作日期
    @Column(name = "operate_Date")
    private String operateDate;

    //备注
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(String feedbackUser) {
        this.feedbackUser = feedbackUser;
    }

    public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmTitle() {
        return confirmTitle;
    }

    public void setConfirmTitle(String confirmTitle) {
        this.confirmTitle = confirmTitle;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int compareTo(ReRun o) {
        return this.getConfirmDate().compareTo(o.getConfirmDate());
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }
}
