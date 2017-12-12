package com.liuqi.tool.todo.ui.todo;/**
 * Created by icaru on 2017/7/21.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.learn.model.UnrequirementServer;
import com.liuqi.learn.model.UnrequirementType;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.ui.common.ExcelExportDialogProxy;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.ui.common.LTreeTableView;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.CalendarUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
 * @Date 2017/7/21 22:09
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/21 22:09
 **/
public class TodoListView extends BorderPane implements EventHandler, InvalidationListener{
    private ToolBar toolBar = new ToolBar();
    private LTreeTableView<Unrequirement> tableView;

    private Button refreshButton = new Button("刷新");
    private Button addButton = new Button("新增");
    private Button addSubButton = new Button("新增子任务");
    private TextField keyField = new TextField();
    private ComboBox<String> dateComboBox = new ComboBox<>();
    private ComboBox<User> userComboBox = new ComboBox<>();
    private LComboBox<String> statusComboBox = new LComboBox<>("未完成", "完成");
    private LComboBox<String> typeComboBox = new LComboBox<>("需求开发", "其它");
    private Button exportButton = new Button("导出");

    private static Logger logger = LoggerFactory.getLogger(TodoListView.class);

    public TodoListView() {
        initView();

        refreshData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        User loginUser = Cache.getLoginUser();

        //初始化表格
        initTableView();

        //初始化工具栏；
        this.toolBar.getItems().addAll(refreshButton, addButton, addSubButton, keyField, dateComboBox, statusComboBox);

        //界面组件添加
        this.setCenter(tableView);
        this.setTop(toolBar);

        //刷新事件处理
        this.refreshButton.setOnAction(e -> search());

        //新增按钮事件处理
        this.addButton.setOnAction(this);

        //新增子待办事项事件处理
        this.addSubButton.setOnAction(this);

        //选择子待办事项时，不允许添加子待办事项
        tableView.getSelectionModel().selectedItemProperty().addListener(this);
        addSubButton.setDisable(true);

        //初始化用户下拉框
        initUserComboBox(loginUser);
        userComboBox.setOnAction(e -> search());

        //初始化时间选择下拉框及其事件
        dateComboBox.getItems().addAll("所有", "本周", "本月", "上月");
        dateComboBox.getSelectionModel().select(0);
        dateComboBox.setOnAction(e -> search());

        //默认选中本月
        dateComboBox.getSelectionModel().select(2);

        statusComboBox.setOnAction(e -> search());
        statusComboBox.select(0);

        typeComboBox.setOnAction(e -> search());
        typeComboBox.select(0);

        //初始化搜索框事件
        keyField.setOnKeyReleased(this);

        this.toolBar.getItems().addAll(typeComboBox, exportButton);
        exportButton.setOnAction(e -> export());

    }

    /**
     * 导出
     */
    private final void export() {
        boolean result = ExcelExportDialogProxy.INSTANCE.saveFile(tableView, "待办事项清单.xls");
        if (result) {
            AlertProxy.showMessage("保存成功！");
        }
    }

    /**
     * 初始化表格对象
     */
    private void initTableView() {
        this.tableView = new LTreeTableView<Unrequirement>() {
            @Override
            public Paint getRowFill(Unrequirement item) {
                if (item.isStatus()) {
                    return Color.GRAY;
                }
                return Color.BLACK;
            }

            @Override
            public void saveRow(Unrequirement unrequirement) {
                try {
                    //保存时，如果状态是完成状态并且完成时间为空，则设置完成时间
                    if (unrequirement.isStatus() &&
                            (null == unrequirement.getActualDate() || "".equals(unrequirement.getActualDate()))) {
                        unrequirement.setActualDate(CalendarUtil.getNowDateStr());
                    }

                    //如果是是未完成，则清除完成时间
                    if (!unrequirement.isStatus()) {
                        unrequirement.setActualDate("");
                    }

                    AjaxProxy.save(unrequirement);

                    search();
                } catch (TodoException e) {
                    e.printStackTrace();
                }
            }
        };

        //给表格添加列
        this.tableView.addCheckBoxColumn("状态", "status")
                .addColumn("标题", "title", 300, 400)
                .addComboBoxColumn("分类", "type",
                        Stream.of(UnrequirementType.values()).collect(Collectors.toList()))
                .addComboBoxColumn("来源", "server",
                        Stream.of(UnrequirementServer.values()).collect(Collectors.toList()))
                .addColumn("提交人", "user")
                .addColumn("提交时间", "receiveDate")
                .addComboBoxColumn("责任人", "dev",
                        Cache.getUserList())
                .addColumn("计划完成", "planDate")
//                .addColumn("计划上线", "planOnlineDate")
                .addColumn("实际完成", "actualDate")
//                .addColumn("实际上线", "actualOnlneDate")
                .addColumn("需求编号", "requirementId", 100)
                .addColumn("内容", "content", 300, 600);
    }

    /**
     * 初始化用户选择下拉搜索框
     * @param loginUser
     */
    private void initUserComboBox(User loginUser) {
        userComboBox.getItems().add(null);
        List<User> userList = Cache.getUserList();
        userList.forEach(user -> {
            if (loginUser.isAdmin()
                    || (loginUser.isCorpAdmin() && loginUser.getCorp().equals(user.getCorp()))) {
                //如果是管理员看所有用户， 公司 管理员看该公司所有人的；
                userComboBox.getItems().add(user);
            }
        });
        if (loginUser.isAdmin() || loginUser.isCorpAdmin()) {
            this.toolBar.getItems().add(userComboBox);
        }
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        search();
    }

    /**
     * 根据传入的数据进行表格刷新
     * @param list
     */
    private final void refreshData(List<Unrequirement> list) {
        ObservableList<TreeItem<Unrequirement>> children = this.tableView.getRoot().getChildren();
        children.clear();

        if (null != list) {
            list.sort(Unrequirement::compareTo);

            //筛选掉有父节点的数据；
            list.forEach(u -> {
                if (null == u.getParent() && 1 != u.getDev().getId()) {
                    //先添加一级节点
                    TreeItem<Unrequirement> item = new TreeItem<>();
                    item.setValue(u);
                    children.add(item);

                    //添加u的下一级节点
                    if (null != u.getChildren()) {
                        u.getChildren().forEach(a -> {
                            TreeItem<Unrequirement> pItem = new TreeItem<>();
                            pItem.setValue(a);
                            item.getChildren().add(pItem);
                        });
                    }

                    item.setExpanded(true);
                }
            });
        }
    }

    /**
     * 事件处理
     * @param event
     */
    @Override
    public void handle(Event event) {
        if (event.getSource().equals(this.addButton)) {
            //新增待办事项
            NewTodoDialog dialog = new NewTodoDialog();

            Optional<Unrequirement> result = dialog.showAndWait();
            result.ifPresent(u -> {
                refreshData();
            });
        } else if (event.getSource().equals(this.addSubButton)) {
            //新增下级待办事项
            TreeItem<Unrequirement> selItem = tableView.getSelectionModel().getSelectedItem();
            if (null == selItem) {
                return;
            }

            Unrequirement unrequirement = selItem.getValue();

            if (null == unrequirement) {
                AlertProxy.showErrorAlert("未选择父待办事项");
                return;
            }

            NewTodoDialog dialog = new NewTodoDialog(unrequirement);

            Optional<Unrequirement> result = dialog.showAndWait();
            result.ifPresent(u -> {
                refreshData();
            });
        } else if (event.getSource().equals(keyField)) {
            KeyEvent e = (KeyEvent) event;
            if (e.getCode().equals(KeyCode.ENTER)) {
                search();
            }
        }
    }

    /**
     * 执行搜索操作
     * 处理关键字、日期以及人员选择三个筛选
     */
    private final void search() {
        User loginUser = Cache.getLoginUser();

        //从服务器获取所有待办事项
        List<Unrequirement> list = null;
        try {
            list = AjaxProxy.list();
        } catch (TodoException e) {
            logger.error("Get unrequirements failed!", e);
            return;
        }

        List<Unrequirement> result = new ArrayList<>();
        String keyStr = keyField.getText().trim();
        String searchDateStr = dateComboBox.getSelectionModel().getSelectedItem();
        User selUser = userComboBox.getSelectionModel().getSelectedItem();

        list.forEach(u -> {
            //用户筛选处理
            //如果是管理员，看所有事项；
//            if (loginUser.isCorpAdmin() && (u.getDev() == null || !loginUser.getCorp().equals(u.getDev().getCorp()))) {
//                //如果是公司管理员，不能看未分配人员的待办事项以及已经分配给其它公司的待办事项p
//                return;
//            }
//
//            if (loginUser.isNormalUser()) {
//                //如果是普通用户，不能看未分配的待办事项
//                if (u.getDev() == null) {
//                    return;
//                }
//
//                //普通用户不能看分配给其它人员的待办事项； 如果是分配给这个用户所属公司的用户对象，可以查看
//                if (!loginUser.getName().equals(u.getDev().getName())
//                        && !loginUser.getCorp().equals(u.getDev().getName())) {
//                    return;
//                }
//            }

            //根据选择的用户判断是否满足条件
            if (null != selUser) {
                if (null == u.getDev() || !selUser.getName().equals(u.getDev().getName())) {
                    return;
                }
            }

            //按状态筛选
            String pStatus = statusComboBox.getSelectedItemStr();
            if ("完成".equals(pStatus) && !u.isStatus()) {
                return;
            } else if ("未完成".equals(pStatus) && u.isStatus()) {
                return;
            }

            //按类型筛选
            String pType = typeComboBox.getSelectedItemStr();
            if ("需求开发".equals(pType) && !UnrequirementType.DEV.equals(u.getType())) {
                return;
            } else if ("其它".equals(pType) && UnrequirementType.DEV.equals(u.getType())) {
                return;
            }

            //关键字筛选处理
            //根据标题/内容等进行筛选
            if (!"".equals(keyStr)) {
                String findKeyStr = u.getTitle() + u.getContent();
                if (u.getPlanDate() != null) {
                    findKeyStr += u.getPlanDate();
                }
                if (u.getRequirementId() != null) {
                    findKeyStr += u.getRequirementId();
                }
                if (null != u.getType()) {
                    findKeyStr += u.getType().getName();
                }
                if (null != u.getServer()) {
                    findKeyStr += u.getServer().getName();
                }

                if (!findKeyStr.contains(keyStr)) {
                    return;
                }
            }

            //按日期筛选
            if ("所有".equals(searchDateStr)) {
                result.add(u);
            } else if ("本月".equals(searchDateStr)) {
                if (null == u.getActualDate() || "".equals(u.getActualDate())) {
                    result.add(u);
                    return;
                }

                //选择本月时，能看到所有计划完成时间在本月及以后的；以及本月完成的； 或者当前还未完成的；
                //上月完成的不算
                if (u.isStatus() && 0 < CalendarUtil.getNowMonthStartDayStr().compareTo(u.getActualDate())) {
                    return;
                }

                result.add(u);
            } else if ("上月".equals(searchDateStr)) {
                //选择上月时，能看到：上月完成的；计划上月完成的； 之前月份完成的不看；
                if (null == u.getActualDate() || "".equals(u.getActualDate())) {
                    result.add(u);
                    return;
                }

                if (u.isStatus() && 0 < CalendarUtil.getLastMonthStartDayStr().compareTo(u.getActualDate())) {
                    return;
                }

                result.add(u);
            } else if ("本周".equals(searchDateStr)) {
                //选择本周时，能看到本周完成的，以及目前尚未完成的
                if (null == u.getActualDate() || "".equals(u.getActualDate())) {
                    result.add(u);
                    return;
                } else if (0 <= u.getActualDate().compareTo(CalendarUtil.getNowWeekStartDayStr())){
                    //完成时间比本周第一天要大
                    result.add(u);
                    return;
                }
            }
        });

        refreshData(result);
    }

    @Override
    public void invalidated(Observable observable) {
        TreeItem<Unrequirement> selItem = tableView.getSelectionModel().getSelectedItem();
        if (null == selItem) {
            return;
        }

        Unrequirement u = selItem.getValue();
        if (null != u && null != u.getParent()) {
            addSubButton.setDisable(true);
        } else {
            addSubButton.setDisable(false);
        }
    }
}
