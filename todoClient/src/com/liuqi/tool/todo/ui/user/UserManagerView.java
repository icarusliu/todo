package com.liuqi.tool.todo.ui.user;/**
 * Created by icaru on 2017/8/17.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.ui.common.LTableView;
import com.liuqi.tool.todo.ui.common.LToolBarTableView;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.Cache;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 11:15
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 11:15
 **/
public class UserManagerView extends LToolBarTableView<User> {
    private Button addButton;

    private NewUserDialog newDialog;

    private Logger logger = LoggerFactory.getLogger(UserManagerView.class);

    public void afterConstruct() {

    }

    @Override
    public void initColumns() {
        this.tableView.addCheckBoxColumn("状态", "status")
                .addColumn("姓名", "name")
                .addColumn("公司", "corp")
                .addColumn("入场时间", "inDate")
                .addColumn("离场时间", "outDate");
    }

    @Override
    public void initToolBar() {
        this.addButton = new Button("新增");
        this.toolBar.getItems().add(addButton);
        addButton.setOnAction(e -> addUser());
    }

    @Override
    public void saveRow(User user) {
        try {
            AjaxProxy.saveUser(user);
            refreshData();
        } catch (TodoException e) {
            logger.error("Save user failed, msg: " + e.getMessage(), e);
        }
    }

    @Override
    public Paint getRowFill(User user) {
        if (null == user.getStatus() || !user.getStatus()) {
            return Color.BLACK;
        }
        return Color.GRAY;
    }

    @Override
    public List<User> getItems() {
        List<User> list = Cache.getAuthUserList();

        list.sort(User::compareTo);
        return list;
    }

    private final void addUser() {
        if (null == newDialog) {
            newDialog = new NewUserDialog();
        }

        Optional<User> result = newDialog.showAndWait();
        result.ifPresent(e -> refreshData());
    }
}
