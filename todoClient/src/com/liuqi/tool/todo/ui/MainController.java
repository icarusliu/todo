package com.liuqi.tool.todo.ui;/**
 * Created by icaru on 2017/8/9.
 */

import com.liuqi.tool.todo.ui.calendar.CalendarView;
import com.liuqi.tool.todo.ui.calendar.MyCalendarView;
import com.liuqi.tool.todo.ui.requirement.RequirementView;
import com.liuqi.tool.todo.ui.todo.TodoListView;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.ConfigProxy;
import com.sun.javafx.stage.StageHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/9 21:19
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/9 21:19
 **/
public class MainController implements Initializable{
    @FXML
    private StackPane stackPane;

    @FXML
    private MyCalendarView myCalendar;

    @FXML
    private CalendarView calendar;

    @FXML
    private TodoListView todoList;

    @FXML
    private RequirementView requirementList;

    @FXML
    private Button myCalendarButton;

    @FXML
    private Button calendarButton;

    @FXML
    private Button todoButton;

    @FXML
    private Button requirementButton;

    @FXML
    private HBox buttonPanel;

    private List<Button> buttonList;

    public void exit() {
        System.exit(0);
    }

    public void min() {
        StageHelper.getStages().get(0).setIconified(true);
    }

    private final void changeView(Button button) {
        buttonList.forEach(b -> {
            if (button.equals(b)) {
                b.setStyle("-fx-background-color: #fff");
            } else {
                b.setStyle("-fx-background-color: none");
            }

            b.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    b.setStyle("-fx-background-color: #fff");
                } else {
                    if (button.equals(b)) {
                        b.setStyle("-fx-background-color: #fff");
                    } else {
                        b.setStyle("-fx-background-color: none");
                    }
                }
            });
        });
    }

    public void myCalendar(ActionEvent actionEvent) {
        myCalendar.toFront();

        changeView(myCalendarButton);
    }

    public void calendar(ActionEvent actionEvent) {
        calendar.toFront();

        changeView(calendarButton);
    }

    public void showRequirementView(ActionEvent actionEvent) {
        requirementList.toFront();

        changeView(requirementButton);
    }

    public void showTodoView(ActionEvent actionEvent) {
        todoList.toFront();

        changeView(todoButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.buttonList = Stream.of(myCalendarButton, calendarButton, todoButton,
                requirementButton).collect(Collectors.toList());

        if (!Cache.getLoginUser().isAdmin() || !ConfigProxy.INSTANCE.showMyCalendar()) {
            myCalendarButton.setVisible(false);
            buttonPanel.getChildren().remove(myCalendarButton);
        }

        if (!ConfigProxy.INSTANCE.showCalendar()) {
            calendarButton.setVisible(false);
            buttonPanel.getChildren().remove(calendarButton);
        }

        if (myCalendarButton.isVisible()) {
            myCalendar(null);
        } else if (calendarButton.isVisible()) {
            calendar(null);
        } else {
            showRequirementView(null);
        }
    }
}
