package com.liuqi.tool.todo.ui.calendar;/**
 * Created by icaru on 2017/8/6.
 */

import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.CalendarUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/6 20:35
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/6 20:35
 **/
public class CalendarView extends BorderPane {
    private int childCount = 7;

    private CToolBar toolBar;

    /**
     * 当前显示的开始日期
     */
    private Calendar nowStartCalendar;

    private List<CalendarDayView> dayPaneList;

    private DetailView detailView;

    private GridPane gridPane;

    private Boolean isMyCalendar = false;

    public CalendarView() {
        init();
    }

    public CalendarView(Boolean isMyCalendar) {
        this.isMyCalendar = isMyCalendar;
        init();

        this.selUser(Cache.getLoginUser());
    }

    public void fore() {
        this.nowStartCalendar.add(Calendar.DAY_OF_WEEK, -childCount);

        refreshChildren();
    }

    public void next() {
        this.nowStartCalendar.add(Calendar.DAY_OF_WEEK, childCount);

        refreshChildren();
    }

    public void now() {
        this.nowStartCalendar = CalendarUtil.getNowWeekStartDay();

        refreshChildren();
    }

    private final void init() {
        this.nowStartCalendar = CalendarUtil.getNowWeekStartDay();
        this.dayPaneList = new ArrayList<>();
        this.detailView = new DetailView();
        this.gridPane = new GridPane();
        this.toolBar = new CToolBar();

        this.setCenter(gridPane);
        this.setTop(toolBar);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 0, 10, 0));

        Calendar pCalendar = (Calendar) nowStartCalendar.clone();

        for (int i = 0; i < childCount; i++) {
            CalendarDayView view = new CalendarDayView((Calendar) pCalendar.clone(), detailView, isMyCalendar);
            dayPaneList.add(view);

            GridPane.setVgrow(view, Priority.ALWAYS);
            GridPane.setHgrow(view, Priority.ALWAYS);
            gridPane.add(view, i % 4, i / 4);

            pCalendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        gridPane.add(detailView, 3, 1);
    }

    public void refreshChildren() {
        Calendar pCalendar = (Calendar) nowStartCalendar.clone();

        dayPaneList.forEach(a -> {
            a.setDate((Calendar) pCalendar.clone());

            pCalendar.add(Calendar.DAY_OF_WEEK, 1);
        });
    }

    public void selUser(User user) {
        dayPaneList.forEach(e -> e.selUser(user));
    }

    private class CToolBar extends ToolBar {
        private Button foreButton = new Button("上周");
        private Button nowButton = new Button("本周");
        private Button nextButton = new Button("下周");
        private ComboBox<User> userComboBox = new ComboBox<>();

        private CToolBar() {
            this.getItems().addAll(foreButton, nowButton, nextButton, userComboBox);

            initUserComboBox();

            foreButton.setOnAction(e -> fore());
            nowButton.setOnAction(e -> now());
            nextButton.setOnAction(e -> next());

            if (Cache.getLoginUser().isNormalUser() || isMyCalendar) {
                userComboBox.setVisible(false);
            }
        }

        /**
         * 初始化用户选择框
         */
        private void initUserComboBox() {
            userComboBox.getItems().add(null);
            userComboBox.getItems().addAll(Cache.getUserList());

            User loginUser = Cache.getLoginUser();
            if (loginUser.isNormalUser()) {
                //普通用户不能进行用户选择
                userComboBox.setVisible(false);
            }
            userComboBox.setOnAction(e1 -> selUser(userComboBox.getSelectionModel().getSelectedItem()));
        }
    }
}
