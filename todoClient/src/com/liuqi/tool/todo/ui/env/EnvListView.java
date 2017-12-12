package com.liuqi.tool.todo.ui.env;/**
 * Created by icaru on 2017/8/28.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Environment;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/28 11:44
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/28 11:44
 **/
public class EnvListView extends GridPane{
    private ObservableList<EnvTableObject> items;

    private static Logger logger = LoggerFactory.getLogger(EnvListView.class);

    public EnvListView() {
        this.items = new ObservableListWrapper<>(Collections.emptyList());

        initEvents();

        refreshItems();
    }

    /**
     * 初始化事件
     */
    private final void initEvents() {
        //显示数据内容改变时，更新界面
        getItems().addListener((ListChangeListener<EnvTableObject>) c -> {
            refreshView();
        });
    }

    /**
     * 刷新 界面
     */
    private final void refreshView() {
        //清空界面
        this.getChildren().clear();
        addHeader();

        //添加清单
        if (0 == items.size()) {
            return;
        }

        int p = 1;
        items.forEach(item -> {
        });
    }

    /**
     * 初始化界面显示
     */
    private final void addHeader() {
        this.addRow(0, new Text("系统"), new Text("类型"),
                new Text("服务器类型"), new Text("地址"), new Text("用户"), new Text("备注"));
    }

    /**
     * 获取显示的数据项
     *
     * @return
     */
    public ObservableList<EnvTableObject> getItems() {
        return items;
    }

    /**
     * 刷新数据
     */
    private final void refreshItems() {
        items.clear();

        try {
            List<Environment> envs = AjaxProxy.listAllEnvs();
            List<EnvTableObject> result = new ArrayList<>();
            String pSystem = "";
            EnvTableObject obj = null;

            for (Environment e : envs) {
                String system = e.getSystem();
                if (system.equals(pSystem)) {
                    //与上一个处理的是同一个系统
                    obj.getEnvs().add(e);
                } else {
                    if (null != obj) {
                        result.add(obj);
                    }

                    pSystem = system;
                    obj = new EnvTableObject();
                    obj.setSystem(system);

                    List<Environment> children = new ArrayList<>();
                    children.add(e);

                    obj.setEnvs(children);
                }
            }

            if (null != obj) {
                result.add(obj);
            }

            items.addAll(result);
        } catch (TodoException e) {
            logger.error("Get all envs failed, errormessage: " + e.getMessage(), e);

            AlertProxy.showErrorAlert("获取环境信息失败, 错误信息：" + e.getMessage());
        }
    }
}
