package com.liuqi.tool.todo.ui;/**
 * Created by icaru on 2017/7/25.
 */

import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.Cache;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/25 11:45
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/25 11:45
 **/
public class LoginForm extends Dialog<User> {
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private ButtonType okButtonType = ButtonType.OK;
    private ButtonType cancelButtonType = ButtonType.CANCEL;

    private static LoginForm INSTANCE;

    public static LoginForm getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new LoginForm();
        }

        return INSTANCE;
    }

    private LoginForm() {
        this.setTitle("登录");
        this.getDialogPane().setContentText("用户登录");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 40, 20, 40));

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.add(new Text("用户名: "), 0, 0);
        gridPane.add(usernameField, 1, 0);

        gridPane.add(new Text("密码: "), 0, 1);
        gridPane.add(passwordField, 1, 1);

        this.getDialogPane().setContent(gridPane);
        this.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        Node okNode = this.getDialogPane().lookupButton(okButtonType);
        okNode.addEventFilter(ActionEvent.ACTION, event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if ("".equals(username)) {
                AlertProxy.showErrorAlert("用户名不能为空");
                event.consume();
            } else if ("".equals(password)) {
                AlertProxy.showErrorAlert("密码不能为空");
                event.consume();
            } else {
                List<User> userList = Cache.getUserList();
                if (null == userList) {
                    AlertProxy.showErrorAlert("用户清单为空,请联系管理员添加用户!");
                    System.exit(0);
                } else {
                    User findUser = null;

                    for (User user : userList) {
                        if (username.equals(user.getName())) {
                            findUser = user;
                            break;
                        }
                    }

                    if (null == findUser) {
                        AlertProxy.showErrorAlert("用户不存在!");
                        event.consume();
                    } else if (!password.equals(findUser.getPassword())) {
                        AlertProxy.showErrorAlert("密码错误!");
                        event.consume();
                    } else {
                        Cache.setLoginUser(findUser);
                    }
                }
            }
        });

        this.setResultConverter(param -> {
            if (param.equals(okButtonType)) {
                return new User();
            } else {
                return null;
            }
        });


        Platform.runLater(() -> usernameField.requestFocus());
    }
}
