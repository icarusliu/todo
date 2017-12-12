package com.liuqi.tool.todo.ui.todo;/**
 * Created by icaru on 2017/7/24.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.learn.model.UnrequirementServer;
import com.liuqi.learn.model.UnrequirementType;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.Cache;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
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
public class NewTodoDialog extends Dialog<Unrequirement>{
    private Unrequirement parent;

    private TextField titleField = new TextField();
    private TextField userField = new TextField();
    private DatePicker receiveField = new DatePicker();
    private ComboBox<UnrequirementType> typeComboBox = new ComboBox<>();
    private ComboBox<UnrequirementServer> serverComboBox = new ComboBox<>();
    private ComboBox<User> devField = new ComboBox<>();
    private DatePicker planDateField = new DatePicker();
    private TextArea contentArea = new TextArea();

    private ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
    private ButtonType cancelButtonType = ButtonType.CANCEL;

    private static Logger logger = LoggerFactory.getLogger(NewTodoDialog.class);

    public NewTodoDialog(Unrequirement parent) {
        this.parent = parent;

        init();
    }

    public NewTodoDialog() {
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
                AlertProxy.showErrorAlert("待办事项不能为空！");

                event.consume();
            } else {
                Unrequirement u = new Unrequirement();
                u.setTitle(title);
                u.setType(typeComboBox.getSelectionModel().getSelectedItem());
                u.setStatus(false);
                User dev = devField.getSelectionModel().getSelectedItem();
                u.setDev(dev);
                u.setUser(userField.getText().trim());
                u.setServer(serverComboBox.getSelectionModel().getSelectedItem());
                u.setContent(contentArea.getText().trim());

                u.setReceiveDate(receiveField.getEditor().getText().trim());
                u.setPlanDate(planDateField.getEditor().getText().trim());
                u.setRequirementId("无");

                if (null != parent) {
                    u.setParent(parent.getId());
                }

                try {
                    AjaxProxy.save(u);
                } catch (TodoException e) {
                    logger.error("Save unrequirement object failed!", e);

                    AlertProxy.showErrorAlert("保存待办事项失败！");
                    event.consume();
                }
            }
        });

        this.setOnShown(e -> {
            Platform.runLater(() -> titleField.requestFocus());
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {
        this.setTitle("新增待办事项");
        this.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        //初始化下拉选项
        typeComboBox.getItems().add(null);
        typeComboBox.getItems().addAll(Stream.of(UnrequirementType.values()).collect(Collectors.toList()));
        typeComboBox.getSelectionModel().select(UnrequirementType.DEV);

        serverComboBox.getItems().add(null);
        serverComboBox.getItems().addAll(Stream.of(UnrequirementServer.values()).collect(Collectors.toList()));
        serverComboBox.getSelectionModel().select(UnrequirementServer.WEEK_REPORT);

        devField.getItems().add(null);
        devField.getItems().addAll(Cache.getUserList());

        devField.getSelectionModel().select(Cache.getLoginUser());

        //设置默认值
        receiveField.setValue(LocalDate.now());
        planDateField.setValue(LocalDate.now());
        userField.setText(Cache.getLoginUser().getName());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Text("待办事项："), 0, 0);
        gridPane.add(titleField, 1, 0, 3, 1);

        gridPane.add(new Text("事项类型："), 0, 1);
        gridPane.add(typeComboBox, 1, 1);
        gridPane.add(new Text("反馈渠道："), 2, 1);
        gridPane.add(serverComboBox, 3, 1);

        gridPane.add(new Text("反馈人："), 0, 2);
        gridPane.add(userField, 1, 2);
        gridPane.add(new Text("反馈时间："), 2, 2);
        gridPane.add(receiveField, 3, 2);

        gridPane.add(new Text("责任人："), 0, 3);
        gridPane.add(devField, 1, 3);
        gridPane.add(new Text("计划完成："), 2, 3);
        gridPane.add(planDateField, 3, 3);

        gridPane.add(new Text("具体内容："), 0, 4);
        gridPane.add(contentArea, 1, 4, 3, 1);

        this.getDialogPane().setContent(gridPane);

        this.setResultConverter(param -> new Unrequirement());
    }
}
