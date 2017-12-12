package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/8/17.
 */

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 18:07
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 18:07
 **/
public abstract class LToolBarTableView<T extends Comparable<T>> extends BorderPane {
    protected ToolBar toolBar;
    protected LTableView<T> tableView;

    public LToolBarTableView() {
        this.toolBar = new ToolBar();
        this.tableView = new LTableView<T>(){

            @Override
            public Paint getRowFill(T t) {
                return LToolBarTableView.this.getRowFill(t);
            }

            @Override
            public void saveRow(T t) {
                LToolBarTableView.this.saveRow(t);
            }
        };

        initColumns();

        //工具栏默认添加刷新按钮
        this.toolBar.getItems().add(new LButton("刷新", e -> refreshData()));

        initToolBar();

        this.setTop(toolBar);
        this.setCenter(tableView);

        afterConstruct();

        this.refreshData();
    }

    public void afterConstruct() {};

    public abstract void initColumns();

    public abstract void initToolBar();

    public abstract void saveRow(T t);

    public abstract Paint getRowFill(T t);

    public abstract List<T> getItems();

    public void refreshData() {
        List<T> list = getItems();

        refreshData(list);
    }

    public void refreshData(List<T> list) {
        if (null != list) {
            this.tableView.getItems().clear();
            list.sort(T::compareTo);
            this.tableView.getItems().addAll(list);
        }
    }
}
