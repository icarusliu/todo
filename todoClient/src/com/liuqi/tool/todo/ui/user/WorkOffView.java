package com.liuqi.tool.todo.ui.user;/**
 * Created by icaru on 2017/8/22.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.User;
import com.liuqi.learn.model.WorkOff;
import com.liuqi.learn.model.WorkOffType;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.ui.common.LToolBarTableView;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.ConfigProxy;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/22 10:44
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/22 10:44
 **/
public class WorkOffView extends LToolBarTableView<WorkOff> {
    private static Logger logger = LoggerFactory.getLogger(WorkOffView.class);
    private NewWorkOffDialog addDialog;

    private LComboBox<String> corpComboBox;
    private LComboBox<User> userLComboBox;

    public void afterConstruct() {
        User loginUser = Cache.getLoginUser();
        if (loginUser.isNormalUser()) {
            tableView.setEditable(false);
        } else {
            tableView.setEditable(true);
        }
    }

    @Override
    public void initColumns() {
        this.tableView.addColumn("事项", "title")
                .addColumn("公司", "corp")
            .addObjectColumn("姓名", "user", new StringConverter<User>() {
                @Override
                public String toString(User object) {
                    return object.getName();
                }

                @Override
                public User fromString(String string) {
                    List<User> userList = Cache.getUserList();
                    for (User pUser : userList) {
                        if (pUser.getName().equals(string)) {
                            return pUser;
                        }
                    }

                    return null;
                }
            })
            .addComboBoxColumn("类型", "type", Stream.of(WorkOffType.values()).collect(Collectors.toList()))
            .addColumn("开始日期", "startDate")
            .addColumn("请假天数", "days")
            .addColumn("备注", "remark")
        ;
    }

    @Override
    public void initToolBar() {
        Button addButton = new Button("新增");
        this.toolBar.getItems().add(addButton);
        addButton.setOnAction(e -> {
            if (null == addDialog) {
                addDialog = new NewWorkOffDialog();
            }

            Optional<WorkOff> result = addDialog.showAndWait();
            result.ifPresent(r -> refreshData());
        });

        //设置筛选项
         this.corpComboBox = new LComboBox<>(ConfigProxy.INSTANCE.getCorps());
         this.userLComboBox  = new LComboBox<>(Cache.getAuthUserList());

         //如果管理员，能筛选公司和用户； 项目经理能筛选用户； 其它不能筛选
        if (!Cache.getLoginUser().isNormalUser()) {
            if (Cache.getLoginUser().isAdmin()) {
                this.toolBar.getItems().add(corpComboBox);
                corpComboBox.setOnAction(e -> search());
                corpComboBox.select(0);
            }

            this.toolBar.getItems().add(userLComboBox);
            userLComboBox.setOnAction(e -> search());
            userLComboBox.select(0);
        }
    }

    /**
     * 过滤待办事项
     */
    private final void search() {
        String selCorp = Cache.getLoginUser().getCorp();
        if (Cache.getLoginUser().isAdmin()) {
            selCorp = this.corpComboBox.getSelectedItemStr();
        }

        User selUser = this.userLComboBox.getSelectedItem();
        List<WorkOff> list = getItems();
        Iterator<WorkOff> it = list.iterator();

        while (it.hasNext()) {
            WorkOff wo = it.next();
            User user = wo.getUser();
            String corp = wo.getCorp();

            if (!"".equals(selCorp) && !selCorp.equals(corp)) {
                it.remove();
            } else if (null != selUser && !selUser.getName().equals(user.getName())) {
                it.remove();
            }
        }

        this.refreshData(list);
    }

    @Override
    public void saveRow(WorkOff workOff) {
        //只有管理员和公司管理员可以修改
        User loginUser = Cache.getLoginUser();
        if (loginUser.isNormalUser()) {
            logger.error("User is normal user, cannot modify workoff datas!");
            return;
        }

        try {
            AjaxProxy.saveWorkOff(workOff);
        } catch (TodoException e) {
            logger.error("Save workoff failed, error message: " + e.getMessage(), e);
        }
    }

    @Override
    public Paint getRowFill(WorkOff workOff) {
        return Color.BLACK;
    }

    @Override
    public List<WorkOff> getItems() {
        try {
            List<WorkOff> list = AjaxProxy.findAllWorkOffs();
            User loginUser = Cache.getLoginUser();
            if (loginUser.isAdmin()) {
                return list;
            }

            Iterator<WorkOff> it = list.iterator();
            while (it.hasNext()) {
                WorkOff wo = it.next();
                User pUser = wo.getUser();
                if (pUser == null) {
                    it.remove();
                } else if (loginUser.isCorpAdmin() && !loginUser.getCorp().equals(pUser.getCorp())) {
                    it.remove();
                } else if (loginUser.isNormalUser() && !loginUser.getName().equals(pUser.getName())) {
                    it.remove();
                }
            }

            return list;
        } catch (TodoException e) {
            logger.error("Get work offs failed, error message:  " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
