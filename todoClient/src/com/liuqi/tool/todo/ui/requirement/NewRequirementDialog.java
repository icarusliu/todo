package com.liuqi.tool.todo.ui.requirement;/**
 * Created by icaru on 2017/7/24.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.*;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.util.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/24 8:36
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/24 8:36
 **/
public class NewRequirementDialog extends Dialog<Requirement>{
    private TextField idField = new TextField("lqm0000");
    private LComboBox<String> systemComboBox;
    private TextField titleField = new TextField();
    private LComboBox deptField = new LComboBox<>(Constants.DEPT_LIST);
    private LComboBox<String> userField = new LComboBox<>();
    private DatePicker receiveField = new DatePicker();
    private TextField countField = new TextField();
    private TextField jobField = new TextField();
    private LComboBox<String> corpComboBox;

    private DatePicker planDateField = new DatePicker();
    private TextArea contentArea = new TextArea();

    private ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
    private ButtonType cancelButtonType = ButtonType.CANCEL;

    private static Logger logger = LoggerFactory.getLogger(NewRequirementDialog.class);

    private Unrequirement unrequirement;

    public NewRequirementDialog() {
        init();
    }

    public void init() {
        //初始化界面
        initView();

        //初始化日历格式化对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        StringConverter converter = new LocalDateStringConverter(formatter, formatter) ;
        receiveField.setConverter(converter);
        planDateField.setConverter(converter);

        //初始化事件
        Node okButton = this.getDialogPane().lookupButton(okButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String title = titleField.getText().trim();
            if ("".equals(title)) {
                AlertProxy.showErrorAlert("标题不能为空！");

                event.consume();
            } else if (idField.getText().trim().equals("")) {
                  AlertProxy.showErrorAlert("需求编号不能为空");

                  event.consume();
            } else {
                Requirement u = new Requirement();
                u.setRequirementId(idField.getText().trim());
                u.setTitle(title);
                u.setSystem(systemComboBox.getSelectedItemStr());
                u.setUser(userField.getEditor().getText().trim());
                u.setStatus(RequirementStatus.STATISTICS);
                u.setReceiveDate(receiveField.getEditor().getText().trim());
                u.setPlanDate(planDateField.getEditor().getText().trim());
                u.setCorp(corpComboBox.getSelectedItemStr());
                u.setCount(countField.getText().trim());
                u.setJob(jobField.getText().trim());
                u.setDept(deptField.getSelectedItemStr());
                u.setDetail(contentArea.getText().trim());

                try {
                    AjaxProxy.saveRequirement(u);

                    this.setResult(u);
                } catch (TodoException e) {
                    logger.error("Save requirement object failed!", e);

                    AlertProxy.showErrorAlert("保存失败！");
                    event.consume();
                }
            }
        });

        //初始化业务人员选项
        userField.setEditable(true);
        userField.setItems(AjaxProxy.listAllBusiUsers());

        userField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            List<String> busiUserList = userField.getItems();

            //删除时，不进行处理
            if (oldValue.startsWith(newValue) && oldValue.length() > newValue.length()) {
                return;
            }

            for (String s : busiUserList) {
                if (null == s) {
                    continue;
                }

                if (!"".equals(newValue) && s.startsWith(newValue)) {
                    userField.getEditor().setText(s);
                    Platform.runLater( () -> userField.getEditor().selectRange(newValue.length(), s.length()));

                    return;
                }
            }

        });

        this.setOnShown(e -> Platform.runLater(() -> {
            idField.requestFocus();
            idField.selectEnd();
        }));
    }

    /**
     * 初始化界面
     */
    private void initView() {
        this.systemComboBox = new LComboBox<>(ConfigProxy.INSTANCE.getPrjs());
        this.corpComboBox = new LComboBox<>(ConfigProxy.INSTANCE.getCorps());

        this.setTitle("新增需求");
        this.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        //设置默认值
        receiveField.setValue(LocalDate.now());
        planDateField.setValue(LocalDate.now());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Text("需求编号："), 0, 0);
        gridPane.add(idField, 1, 0, 1, 1);

        gridPane.add(new Text("需求标题："), 0, 1);
        gridPane.add(titleField, 1, 1, 3, 1);

        gridPane.add(new Text("归口人："), 0, 2);
        gridPane.add(userField, 1, 2);
        gridPane.add(new Text("归口部门："), 2, 2);
        gridPane.add(deptField, 3, 2);

        gridPane.add(new Text("接收时间："), 0, 3);
        gridPane.add(receiveField, 1, 3);
        gridPane.add(new Text("计划完成："), 2, 3);
        gridPane.add(planDateField, 3, 3);

        gridPane.add(new Text("公司："), 0, 4);
        gridPane.add(corpComboBox, 1, 4);
        gridPane.add(new Text("系统："), 2, 4);
        gridPane.add(systemComboBox, 3, 4);

        gridPane.add(new Text("报表张数："), 0, 5);
        gridPane.add(countField, 1, 5);
        gridPane.add(new Text("工作量："), 2, 5);
        gridPane.add(jobField, 3, 5);

        gridPane.add(new Text("具体内容："), 0, 6);
        gridPane.add(contentArea, 1, 6, 3, 1);
        this.contentArea.setWrapText(true);

        this.getDialogPane().setContent(gridPane);

        this.setResultConverter(e -> new Requirement());
    }


}
