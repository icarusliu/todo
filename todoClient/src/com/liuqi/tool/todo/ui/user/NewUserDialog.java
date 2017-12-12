package com.liuqi.tool.todo.ui.user;/**
 * Created by icaru on 2017/8/17.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.CalendarUtil;
import com.liuqi.tool.todo.util.ConfigProxy;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 11:22
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 11:22
 **/
public class NewUserDialog extends Dialog<User> {
    private TextField nameField;
    private LComboBox<String> corpComboBox;
    private LComboBox<String> roleComboBox;

    private ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
    private ButtonType cancelButtonType = ButtonType.CANCEL;

    private Logger logger = LoggerFactory.getLogger(NewUserDialog.class);

    public NewUserDialog() {
        this.setTitle("新增用户");

        initView();
    }

    private final void initView() {
        this.nameField = new TextField();
        this.corpComboBox = new LComboBox<>(ConfigProxy.INSTANCE.getCorps());
        this.roleComboBox = new LComboBox<>("开发人员", "项目经理", "管理员");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, new Text("姓名："), nameField);
        gridPane.addRow(1, new Text("公司："), corpComboBox);
        gridPane.addRow(2, new Text("角色："), roleComboBox);

        this.getDialogPane().setContent(gridPane);
        this.getDialogPane().getButtonTypes().addAll(this.okButtonType, cancelButtonType);

        this.setResultConverter(e -> new User());

        Node okButton = this.getDialogPane().lookupButton(okButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String name = nameField.getText().trim();
            String corp = corpComboBox.getSelectedItemStr();
            String role = roleComboBox.getSelectedItemStr();

            if ("".equals(name)) {
                AlertProxy.showErrorAlert("姓名不能为空！");
                event.consume();
                return;
            }

            User user = new User();
            user.setName(name);
            user.setCorp(corp);
            user.setInDate(CalendarUtil.getNowDateStr());
            user.setRole(role.equals("开发人员") ? "user" : (role.equals("项目经理") ? "corpadmin" : "admin"));
            try {
                AjaxProxy.saveUser(user);
            } catch (TodoException e) {
                logger.error("Save user failed, error message: " + e.getMessage(), e);

                if (ExceptionCodes.USER_EXISTS.equals(e.getCode())) {
                    AlertProxy.showErrorAlert("用户已存在！");
                } else {
                    AlertProxy.showErrorAlert("保存失败，错误信息：" + e.getMessage());
                }

                event.consume();
                return;
            }
        });
    }
}
