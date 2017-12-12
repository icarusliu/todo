package com.liuqi.tool.todo.ui.requirement;/**
 * Created by icaru on 2017/7/26.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Requirement;
import com.liuqi.learn.model.RequirementStatus;
import com.liuqi.tool.todo.ui.common.ExcelExportDialogProxy;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.ui.common.LTableView;
import com.liuqi.tool.todo.ui.common.LToolBarTableView;
import com.liuqi.tool.todo.util.*;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/26 23:47
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/26 23:47
 **/
public class RequirementView extends LToolBarTableView<Requirement> {
    private ToolBar statusBar;

    private static Logger logger = LoggerFactory.getLogger(RequirementView.class);

    private Button addButton;
    private TextField keyField;
    private LComboBox<String> systemComboBox;
    private LComboBox<String> corpComboBox;
    private LComboBox<String> statusComboBox;
    private Button exportButton;

    private NewRequirementDialog addDialog = new NewRequirementDialog();

    private Text countText;
    private Text jobText;
    private Text reportCountText;

    public void afterConstruct() {
        initStatusBar();
        this.setBottom(this.statusBar);
    }

    @Override
    public void initColumns() {
        this.tableView.addColumn("需求编号", "requirementId")
                .addComboBoxColumn("系统", "system", ConfigProxy.INSTANCE.getPrjs())
                .addColumn("内容", "title", 200)
                .addComboBoxColumn("状态", "status",
                        Stream.of(RequirementStatus.values()).collect(Collectors.toList()))
                .addComboBoxColumn("部门", "dept",
                        Constants.DEPT_LIST)
                .addColumn("联系人", "user")
                .addColumn("报表张数", "count")
                .addColumn("工作量", "job")
                .addComboBoxColumn("公司", "corp", ConfigProxy.INSTANCE.getCorps())
                .addColumn("接收时间", "receiveDate")
                .addColumn("SIT时间", "sitDate")
                .addColumn("计划上线", "planDate")
                .addColumn("实际上线", "onlineDate")
                .addColumn("结算时间", "payDate")
                .addColumn("任务单号", "taskNo")
                .addColumn("备注", "detail", 200, 300, 400);
    }

    @Override
    public void initToolBar() {
        //初始化工具栏
        addButton = new Button("新增");
        keyField = new TextField();
        statusComboBox = new LComboBox<>("未完成", "完成");
        exportButton = new Button("导出");
        this.systemComboBox = new LComboBox<>(ConfigProxy.INSTANCE.getPrjs());
        this.corpComboBox = new LComboBox<>(ConfigProxy.INSTANCE.getCorps());

        if (Cache.getLoginUser().isAdmin()) {
            this.toolBar.getItems().addAll(addButton, keyField, systemComboBox, corpComboBox, statusComboBox, exportButton);
        } else {
            //非管理员不能新增/筛选系统及公司
            this.toolBar.getItems().addAll(keyField, statusComboBox, exportButton);
        }
        addButton.setOnAction(e -> {
            Optional<Requirement> result = addDialog.showAndWait();
            result.ifPresent(u -> {
                if (null != u) {
                    tableView.getItems().add(u);
                }
            });
        });
        exportButton.setOnAction(e -> {
            boolean result = ExcelExportDialogProxy.INSTANCE.saveFile(tableView, "需求清单.xls");
            if (result) {
                AlertProxy.showMessage("保存成功！");
            }
        });

        keyField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                refreshData();
            }
        });

        corpComboBox.select(0);
        systemComboBox.setOnAction(e -> refreshData());
        corpComboBox.setOnAction(e -> refreshData());
        statusComboBox.setOnAction(e -> refreshData());
    }

    @Override
    public void saveRow(Requirement requirement) {
        try {
            if (requirement.isCompleted()) {
                if (null == requirement.getOnlineDate()
                        || "".equals(requirement.getOnlineDate())) {
                    requirement.setOnlineDate(CalendarUtil.getNowDateStr());
                }
            } else {
                requirement.setOnlineDate("");
            }

            AjaxProxy.saveRequirement(requirement);

//                    search();
        } catch (TodoException e) {
            logger.error("Save requirement failed!", e);

            AlertProxy.showErrorAlert("保存需求失败！");
        }
    }

    @Override
    public Paint getRowFill(Requirement requirement) {
        if (requirement.isCompleted()) {
            return Color.GRAY;
        }

        return Color.BLACK;
    }

    @Override
    public List<Requirement> getItems() {
        String key = keyField.getText().trim();
        String system = systemComboBox.getSelectedItemStr();
        String corp = corpComboBox.getSelectedItemStr();
        String status = statusComboBox.getSelectedItemStr();

        List<Requirement> list = null;
        try {
            list = AjaxProxy.listAllRequirements();
        } catch (TodoException e) {
            logger.error("Get requirements failed!", e);

            AlertProxy.showErrorAlert("获取需求清单失败！");
        }
        if (null == list) {
            return new ArrayList<>();
        }

        List<Requirement> result = new ArrayList<>();
        list.forEach(r -> {
            //关键字处理
            if (!"".equals(key)) {
                String pKey = r.getTitle() + r.getRequirementId() + r.getDetail()
                        + r.getCorp() + r.getDept() + r.getSystem() + r.getUser() + r.getStatus().getName();
                if (r.getPlanDate() != null) {
                    pKey += r.getPlanDate();
                }

                if (r.getReceiveDate() != null) {
                    pKey += r.getReceiveDate();
                }

                if (r.getOnlineDate() != null) {
                    pKey += r.getOnlineDate();
                }

                if (null != r.getSitDate()) {
                    pKey += r.getSitDate();
                }

                if (null != r.getPayDate()) {
                    pKey += r.getPayDate();
                }

                if (!pKey.contains(key)) {
                    return;
                }
            }

            if (Cache.getLoginUser().isAdmin()) {
                //非管理员不能筛选系统/公司
                //系统搜索处理
                if (!"".equals(system)) {
                    if (!r.getSystem().equals(system)) {
                        return;
                    }
                }

                //公司搜索处理
                if (!"".equals(corp) && !corp.equals(r.getCorp())) {
                    return;
                }
            }

            //状态搜索处理
            if (!"".equals(status)) {
                if (status.equals("完成") && !r.isCompleted()) {
                    return;
                } else if (status.equals("未完成") && r.isCompleted()) {
                    return;
                }
            }

            result.add(r);
        });

        return result;
    }

    /**
     * 初始化状态栏
     */
    private final void initStatusBar() {
        countText = new Text();
        jobText = new Text();
        reportCountText = new Text();
        this.statusBar = new ToolBar();
        HBox box = new HBox();
        box.setSpacing(30);

        Text tCountText = new Text("需求数：");
        Text tJobText = new Text("工作量：");
        Text tReportCountText = new Text("报表数：");

        Color c = Color.BLUE;
        tCountText.setFill(c);
        tJobText.setFill(c);
        tReportCountText.setFill(c);

        box.getChildren().addAll(tCountText, countText,
                tJobText, jobText,
                tReportCountText, reportCountText);

        this.statusBar.getItems().add(box);
    }

    private final void refreshStatusBar() {
        List<Requirement> list = tableView.getItems();
        if (null != list) {
            int count = list.size();
            double job = 0;
            int reportCount = 0;

            for (Requirement requirement : list) {
                String jobStr = requirement.getJob();
                if (null != jobStr && !"".equals(jobStr)) {
                    job += Double.valueOf(jobStr);
                }

                String reportCountStr = requirement.getCount();
                if (null != reportCountStr && !"".equals(reportCountStr)) {
                    int pCount = 0;
                    try {
                        pCount = Integer.valueOf(reportCountStr);
                    } catch (Exception e) {
                        pCount = 0;
                    }

                    reportCount += pCount;
                }
            }

            this.jobText.setText(String.format("%.2f", job));
            this.countText.setText(String.valueOf(count));
            this.reportCountText.setText(String.valueOf(reportCount));
        }
    }

    @Override
    public void refreshData(List<Requirement> list) {
        if (null != list) {
            super.refreshData(list);

            this.refreshStatusBar();
        }
    }
}
