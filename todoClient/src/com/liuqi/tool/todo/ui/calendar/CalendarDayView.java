package com.liuqi.tool.todo.ui.calendar;/**
 * Created by ctx334 on 2017/7/16.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.learn.model.UnrequirementServer;
import com.liuqi.learn.model.UnrequirementType;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.ui.common.LTableView;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.CalendarUtil;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <br>
 * 作　　者：刘奇<br>
 * 创建时间：2017/7/16<br>
 * 对象版本：V1.0<br>
 * －－－－－－－－－－修改记录－－－－－－－－－－<br>
 * 版　　本：V1.1<br>
 * 修改内容：<br>
 * 修改　人：刘奇<br>
 * 修改时间：<br>
 **/
public class CalendarDayView extends BorderPane implements EventHandler{
    private Calendar date;
    private Label dateLabel = new Label();
    private TextField titleField = new TextField();
    private LTableView<Unrequirement> tableView;
    private User selUser;

    private static Logger logger = LoggerFactory.getLogger(CalendarDayView.class);

    private DetailView detailView;
    private Boolean isMyCalendar = false;

    public CalendarDayView(Calendar date, DetailView detailView, Boolean isMyCalendar) {
        this.date = date;
        this.detailView = detailView;
        this.isMyCalendar = isMyCalendar;

        initView();

        initEvent();
    }

    /**
     * 初始化事件
     */
    private final void initEvent() {
        titleField.setOnKeyReleased(this);

        tableView.getSelectionModel().selectedItemProperty().addListener(e -> {
            detailView.setUnrequirement(tableView.getSelectionModel().getSelectedItem());
        });
    }

    /**
     * 初始化界面
     */
    private final void initView() {
        tableView = new LTableView<Unrequirement>() {
            @Override
            public Paint getRowFill(Unrequirement item) {
                //如果状态是已完成，则设置前景色为灰色，否则设置成黑色
                if (item.isStatus()) {
                    return Color.GRAY;
                } else if (null != item.getPlanDate()
                        && 0 < CalendarUtil.formatDateStr(date.getTime()).compareTo(item.getPlanDate())) {
                    //如果当前面板显示的日期比待办事项的计划日期要大，则待办事项显示为红色
                    return Color.RED;
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

                    if (!unrequirement.isStatus()) {
                        unrequirement.setActualDate("");
                    }

                    AjaxProxy.save(unrequirement);
                    refreshData();
                } catch (TodoException e) {
                    logger.error("Save unrequrirement failed!", e);
                }
            }
        };

        VBox box = new VBox();
        box.getChildren().addAll(Stream.of(dateLabel, titleField, tableView).collect(Collectors.toList()));
        box.setSpacing(2);

        this.setCenter(box);

        tableView.addCheckBoxColumn("状态", "status");
        if (!isMyCalendar && !Cache.getLoginUser().isNormalUser()) {
            tableView.addObjectColumn("责任人", "dev",
                    new StringConverter<User>() {
                        @Override
                        public String toString(User object) {
                            if (null == object) {
                                return "";
                            }
                            return object.toString();
                        }

                        @Override
                        public User fromString(String string) {
                            List<User> userList = Cache.getUserList();
                            User user = null;
                            for (User user1 : userList) {
                                if (user1.getName().equals(string)) {
                                    user = user1;
                                    break;
                                }
                            }

                            return user;
                        }
                    },50);
        }
        tableView.addColumn("标题", "title", 200, 300);

        tableView.getColumns().forEach(c -> {
            c.setStyle("-fx-border-style: none; ");
        });

        tableView.hideHeader();

        tableView.setBorder(null);

        setDate(date);
    }

    /**
     * 选择某个用户时，对数据进行刷新
     * @param selUser
     */
    public void selUser(User selUser) {
        this.selUser = selUser;
        setDate(this.date);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        setDate(this.date);
    }

    /**
     * 重新设置显示的日期
     *
     * @param date
     */
    public void setDate(Calendar date) {
        this.date = date;

        //设置日期显示
        dateLabel.setText(CalendarUtil.formatWeekDateStr(date.getTime()));
        if (CalendarUtil.isWeekend(date.getTime())) {
            dateLabel.setStyle("-fx-text-fill: #00ff00");
        } else if (CalendarUtil.isNowDate(date.getTime())) {
            dateLabel.setStyle("-fx-text-fill: #ff0000");
        } else {
            dateLabel.setStyle("-fx-text-fill: #000000");
        }

        //设置数据
        try {
            List<Unrequirement> list = AjaxProxy.listForPlanDate(date.getTime());
            list.sort(Unrequirement::compareTo);

            if (null != list) {
                tableView.getItems().clear();

                list.forEach(i -> {
                    if (!isMyCalendar) {
                        if (i.getDev().getId() == 1) {
                            return;
                        }
                    }

                    if (null == i.getPlanDate() || "".equals(i.getPlanDate())) {
                        return;
                    }

                    if (null != selUser && null == i.getDev()) {
                        return;
                    }

                    if (null != selUser && !i.getDev().getName().equals(selUser.getName())) {
                        return;
                    }

                    tableView.getItems().add(i);
                });
            }
        } catch (TodoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Event event) {
        if (event.getSource().equals(this.titleField)) {
            //添加事件
            if (event instanceof KeyEvent) {
                KeyEvent e = (KeyEvent) event;
                if (e.getCode().equals(KeyCode.ENTER)) {
                    //按Enter键时进行新增操作
                    String title = titleField.getText().trim();
                    if ("".equals(title)) {
                        return;
                    }
                    Unrequirement u = new Unrequirement();
                    u.setTitle(title);
                    u.setPlanDate(CalendarUtil.formatDateStr(date.getTime()));
                    u.setServer(UnrequirementServer.WEEK_REPORT);
                    u.setType(UnrequirementType.DEV);
                    u.setReceiveDate(CalendarUtil.getNowDateStr());
                    u.setDev(Cache.getLoginUser());
                    u.setStatus(false);

                    u.setRequirementId("无");

                    try {
                        AjaxProxy.save(u);

                        this.refreshData();

                        titleField.setText("");
                    } catch (TodoException e1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("警告");
                        alert.setContentText("保存失败，错误信息：" + e1.getMessage());
                        alert.showAndWait();
                    }
                }
            }
        }
    }
}
