package com.liuqi.tool.todo.ui;

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.User;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.ConfigProxy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Main extends Application {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        //检查是否保存用户名
        String userName = ConfigProxy.INSTANCE.getSavedName();
        List<User> userList = Cache.getUserList();
        if (null == userList) {
            logger.error("No user exists, create an user first!");
            return;
        }

        //根据保存的用户名查找用户
        User findUser = null;
        if (null != userName && !"".equals(userName)) {
            for (User user : userList) {
                if (userName.equals(user.getName()) || userName.equals(user.getId())) {
                    findUser = user;
                    break;
                }
            }
        }

        if (null == findUser) {
            //未找到用户，进行登录
            LoginForm form = LoginForm.getInstance();
            Optional<User> result = form.showAndWait();
            result.ifPresent(user -> {
                if (null != user) {
                    //保存登录的用户与密码
                    User loginUser = Cache.getLoginUser();
                    try {
                        ConfigProxy.INSTANCE.saveLoginInfo(loginUser.getName());
                    } catch (TodoException e) {
                        logger.error("Save login user info faield!", e);
                    }

                    //显示主界面
                    showMainView(primaryStage);
                }
            });
        } else {
            logger.info("Get saved user info succeeded, username: " + findUser.getName());

            //保存登录的用户信息
            Cache.setLoginUser(findUser);

            //找到用户，直接显示主界面
            showMainView(primaryStage);
        }
    }

    private void showMainView(Stage primaryStage) {
        BorderPane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } catch (IOException e) {
            logger.error("Load fxml file failed!", e);
            System.exit(0);
        }
        primaryStage.setTitle("项目管理");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);

        TabPane tabPane = (TabPane) root.getCenter();
        Iterator<Tab> it = tabPane.getTabs().iterator();
        while (it.hasNext()) {
            Tab tab = it.next();

            if ("todo".equals(tab.getId()) || "requirement".equals(tab.getId())) {
                continue;
            }

            User loginUser = Cache.getLoginUser();
            if ("myCalendar".equals(tab.getId())) {
                if (!loginUser.isAdmin()
                        || !ConfigProxy.INSTANCE.showMyCalendar()) {
                    it.remove();
                }
            } else if ("calendar".equals(tab.getId())) {
                if (!ConfigProxy.INSTANCE.showCalendar()) {
                    it.remove();
                }
            } else if ("userManager".equals(tab.getId())) {
                if (loginUser.isNormalUser()) {
                    it.remove();
                }
            } else if ("env".equals(tab.getId())) {
                if (!loginUser.isAdmin()) {
                    it.remove();
                }
//            } else if ("note".equals(tab.getId())) {
//                if (!loginUser.isAdmin()) {
//                    it.remove();
//                }
            } else if ("reRun".equals(tab.getId())) {
                //重跑日志普通用户不能看
                if (loginUser.isNormalUser()) {
                    it.remove();
                }
            }
        }


//        primaryStage.initStyle(StageStyle.UNDECORATED);
//        javafx.geometry.Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
//        primaryStage.setX(bounds.getMinX());
//        primaryStage.setY(bounds.getMinY());
//        primaryStage.setWidth(bounds.getWidth());
//        primaryStage.setHeight(bounds.getHeight());

        primaryStage.initStyle(StageStyle.UNIFIED);

        root.getStylesheets().add("/main.css");

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
