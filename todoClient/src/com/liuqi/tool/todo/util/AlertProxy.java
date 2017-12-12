package com.liuqi.tool.todo.util;/**
 * Created by icaru on 2017/7/25.
 */

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;

import java.util.Optional;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/25 9:32
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/25 9:32
 **/
public class AlertProxy {
    /**
     * 显示警告对话框
     *
     * @param content
     */
    public static void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("警告");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * 显示消息提示
     *
     * @param message
     */
    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 显示提示对话框
     *
     * @param message
     * @param okCallback 点击OK执行的函数
     */
    public static void showConfirm(String message, Callback<?, ?> okCallback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("提示");
        alert.setContentText(message);
        Optional<ButtonType> optional = alert.showAndWait();

        optional.ifPresent(e -> {
            if (e.equals(ButtonType.OK)) {
                okCallback.call(null);
            }
        });
    }
}
