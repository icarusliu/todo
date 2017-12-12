package com.liuqi.tool.todo.ui.rerun;/**
 * Created by icaru on 2017/9/15.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.ReRun;
import com.liuqi.tool.todo.ui.common.LDatePicker;
import com.liuqi.tool.todo.ui.common.LDialog;
import com.liuqi.tool.todo.util.AjaxProxy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/15 20:17
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/15 20:17
 **/
public class NewReRunDialog extends LDialog<ReRun> {
    private TextField reasonText;
    private TextField feedbackUserText;
    private LDatePicker feedbackDatePicker;
    private TextField confirmUserText;
    private LDatePicker confirmDatePicker;
    private TextField confirmTitle;
    private TextArea reportsText;
    private TextField dataDateText;
    private TextField operatorText;
    private LDatePicker operateDatePicker;
    private TextArea remarkArea;

    public NewReRunDialog() {
        super("新增重跑日志");

        this.setResultConverter( e -> new ReRun());
    }

    @Override
    protected void save() throws TodoException {
        ReRun reRun = new ReRun();
        reRun.setReason(reasonText.getText().trim());
        reRun.setFeedbackUser(feedbackUserText.getText().trim());
        reRun.setFeedbackDate(feedbackDatePicker.getDateStr());
        reRun.setConfirmUser(confirmUserText.getText().trim());
        reRun.setConfirmDate(confirmDatePicker.getDateStr());
        reRun.setConfirmTitle(confirmTitle.getText().trim());
        reRun.setReason(reportsText.getText().trim());
        reRun.setDataDate(dataDateText.getText().trim());
        reRun.setOperateDate(operateDatePicker.getDateStr());
        reRun.setOperator(operatorText.getText().trim());
        reRun.setRemark(remarkArea.getText().trim());

        AjaxProxy.saveReRun(reRun);
    }

    @Override
    protected void check() throws TodoException {
        checkNull(reasonText, feedbackUserText, confirmUserText, confirmTitle, dataDateText, operatorText);
    }

    @Override
    protected void initView(GridPane gridPane) {
        reasonText = new LTextField();
        feedbackUserText = new LTextField();
        feedbackDatePicker = new LDatePicker();
        confirmUserText = new LTextField();
        confirmDatePicker = new LDatePicker();
        confirmTitle = new LTextField();
        reportsText = new TextArea();
        dataDateText = new LTextField();
        operatorText = new LTextField();
        operateDatePicker = new LDatePicker();
        remarkArea = new TextArea();

        reasonText.setMaxWidth(800);
        gridPane.addRow(0,  new Text("重跑原因"), reasonText);
        GridPane.setColumnSpan(reasonText, 3);

        gridPane.addRow(1,  new Text("反馈人"), feedbackUserText);
        gridPane.addRow(1,  new Text("反馈时间"), feedbackDatePicker);

        gridPane.addRow(2,  new Text("确认人"), confirmUserText);
        gridPane.addRow(2,  new Text("确认时间"), confirmDatePicker);

        confirmTitle.setMaxWidth(800);
        gridPane.addRow(3,  new Text("确认邮件标题"), confirmTitle);
        GridPane.setColumnSpan(confirmTitle, 3);

        reportsText.setMaxWidth(800);
        gridPane.addRow(4,  new Text("涉及报表"), reportsText);
        GridPane.setColumnSpan(reportsText, 3);

        dataDateText.setMaxWidth(800);
        gridPane.addRow(5,  new Text("重跑日期"), dataDateText);
        GridPane.setColumnSpan(dataDateText, 3);

        gridPane.addRow(6,  new Text("操作人"), operatorText);
        gridPane.addRow(6,  new Text("操作时间"), operateDatePicker);

        gridPane.addRow(7,  new Text("备注"), remarkArea);
        GridPane.setColumnSpan(remarkArea, 3);
    }

    private class LTextField extends TextField {
        public LTextField() {
            this.setMaxWidth(200);
        }
    }
}
