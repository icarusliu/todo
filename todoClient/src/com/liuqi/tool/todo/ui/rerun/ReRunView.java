package com.liuqi.tool.todo.ui.rerun;/**
 * Created by icaru on 2017/9/15.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.ReRun;
import com.liuqi.tool.todo.ui.common.LButton;
import com.liuqi.tool.todo.ui.common.LToolBarTableView;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *     重跑日志展示界面
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/15 10:10
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/15 10:10
 **/
public class ReRunView extends LToolBarTableView<ReRun>{
    private static Logger logger = LoggerFactory.getLogger(ReRunView.class);

    private NewReRunDialog newReRunDialog;

    @Override
    public void initColumns() {
        this.tableView.addColumn("重跑原因", "reason")
                .addColumn("反馈人", "feedbackUser")
                .addColumn("反馈时间", "feedbackDate")
                .addColumn("确认人", "confirmUser")
                .addColumn("确认时间", "confirmDate")
                .addColumn("确认邮件标题", "confirmTitle")
                .addColumn("涉及报表", "reports")
                .addColumn("数据日期", "dataDate")
                .addColumn("操作人", "operator")
                .addColumn("操作日期", "operateDate")
                .addColumn("备注", "remark")
        ;
    }

    @Override
    public void initToolBar() {
        //新增按钮
        this.toolBar.getItems().add(new LButton("新增", e -> addReRun()));

        //搜索
        TextField searchField = new TextField();
        this.toolBar.getItems().add(searchField);
        searchField.setOnKeyReleased(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                search(searchField);
            }
        });
    }

    /**
     * 搜索
     */
    private final void search(TextField searchField) {
        List<ReRun> items = getItems();
        if (null != items) {
            List<ReRun> result = new ArrayList<>();
            items.forEach(e -> {
                String key = getKey(e);
                if (key.contains(searchField.getText().trim())) {
                    result.add(e);
                }
            });

            this.refreshData(result);
        }
    }

    /**
     * 根据对象获取查询关键字
     * @param reRun
     * @return
     */
    private final String getKey(ReRun reRun) {
        return reRun.getReason() + reRun.getFeedbackUser() + reRun.getFeedbackDate()
                + reRun.getConfirmTitle() + reRun.getConfirmUser()
                + reRun.getConfirmDate() + reRun.getReports()
                + reRun.getDataDate() + reRun.getOperateDate()
                + reRun.getOperator() + reRun.getRemark();
    }

    /**
     * 新增重跑日志
     */
    private final void addReRun() {
        if (null == newReRunDialog) {
            newReRunDialog = new NewReRunDialog();
        }

        Optional<ReRun> result = newReRunDialog.showAndWait();
        result.ifPresent(e -> refreshData());
    }

    @Override
    public void saveRow(ReRun reRun) {
        try {
            AjaxProxy.saveReRun(reRun);
        } catch (TodoException e) {
            logger.error("Save ReRun failed!", e);
            AlertProxy.showErrorAlert("保存失败！");
        }
    }

    @Override
    public Paint getRowFill(ReRun reRun) {
        return Color.BLACK;
    }

    @Override
    public List<ReRun> getItems() {
        try {
            return AjaxProxy.getAllReRuns();
        } catch (TodoException e) {
            logger.error("Get all reruns failed!", e);

            return new ArrayList<>();
        }
    }
}
