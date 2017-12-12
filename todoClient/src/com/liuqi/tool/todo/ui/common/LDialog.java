package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/7/24.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Requirement;
import com.liuqi.learn.model.RequirementStatus;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.ConfigProxy;
import com.liuqi.tool.todo.util.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
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
import java.util.List;

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
public abstract class LDialog<T> extends Dialog<T>{
    private ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
    private ButtonType cancelButtonType = ButtonType.CANCEL;

    private static Logger logger = LoggerFactory.getLogger(LDialog.class);

    public LDialog(String title) {
        this.setTitle(title);

        init();
    }

    public void init() {
        this.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        //初始化界面
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setPadding(new Insets(10));
        initView(pane);
        this.getDialogPane().setContent(pane);

        //初始化事件
        Node okButton = this.getDialogPane().lookupButton(okButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                check();
            } catch (TodoException e) {
                logger.error("Check failed, error message: " + e.getMessage(), e);

                AlertProxy.showErrorAlert(e.getMessage());

                event.consume();

                return;
            }

            try {
                save();
            } catch (TodoException e) {
                logger.error("Save failed, error message: " + e.getMessage(), e);

                AlertProxy.showErrorAlert("保存失败，错误消息： " + e.getMessage());

                event.consume();
            }
        });

    }

    /**
     * 提交后进行保存
     */
    protected abstract void save() throws TodoException;

    /**
     * 提交前校验
     * 校验失败抛出异常
     */
    protected abstract void check() throws TodoException;

    /**
     * 初始化界面
     */
    protected abstract void initView(GridPane gridPane);

    /**
     * 如果未输入，抛出异常
     *
     * @param textField
     * @throws TodoException
     */
    protected void checkNull(TextField...textField) throws TodoException {
        for (TextField field : textField) {
            if ("".equals(field.getText().trim())) {
                throw new TodoException(ExceptionCodes.UI_VALUE_NULL, "请输入所有必输项！");
            }
        }
    }
}
