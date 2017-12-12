package com.liuqi.tool.todo.ui.common;/**
 * Created by ctx334 on 2017/7/17.
 */

import com.liuqi.learn.model.Unrequirement;
import com.liuqi.tool.todo.util.ReflectUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;


/**
 * <br>
 * 作　　者：刘奇<br>
 * 创建时间：2017/7/17<br>
 * 对象版本：V1.0<br>
 * －－－－－－－－－－修改记录－－－－－－－－－－<br>
 * 版　　本：V1.1<br>
 * 修改内容：<br>
 * 修改　人：刘奇<br>
 * 修改时间：<br>
 **/
public abstract class LTableView<T> extends TableView<T> {
    public LTableView() {
        this.setEditable(true);
    }

    /**
     * 获取行的颜色
     *
     * @param t
     * @return
     */
    public abstract Paint getRowFill(T t);

    /**
     * 保存某一行对象
     */
    public abstract void saveRow(T t);

    /**
     * 隐藏标题
     */
    public void hideHeader() {
        this.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Pane header = (Pane) lookup("TableHeaderRow");
                if (null != header) {
                    header.setPrefHeight(0);
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setVisible(false);
                    header.setManaged(false);
                }
            }
        });
    }

    /**
     * 添加CheckBox类型的列
     *
     * @param title
     * @param property
     * @return
     */
    public LTableView<T> addCheckBoxColumn(String title, String property) {
        //添加列属性名称
        TableColumn<T, CheckBox> column = new TableColumn<>(title);
        column.setCellValueFactory(param -> {
            CheckBox checkBox = new CheckBox();
            ObservableValue<Boolean> value = new BeanObservableValue<>(property, param.getValue());
            if (null != value.getValue()) {
                checkBox.setSelected(value.getValue());
            } else {
                checkBox.setSelected(false);
            }

            checkBox.setOnAction(event -> {
                ReflectUtil.setBeanValue(param.getValue(), property, checkBox.isSelected());

                saveRow(param.getValue());
            });

            checkBox.setTextFill(getRowFill(param.getValue()));

            return new ReadOnlyObjectWrapper<>(checkBox);
        });

        //设置column宽度
        column.setMaxWidth(30d);

        this.getColumns().add(column);

        column.setUserData(new ColumnUserObject(title, property, column.getWidth()));

        return this;
    }

    /**
     * 添加下拉的列
     *
     * @param title
     * @param property
     * @return
     */
    public <R> LTableView<T> addComboBoxColumn(String title, String property,
                                               List<R> itemList) {
        TableColumn<T, R> column = new TableColumn<>(title);

        column.setCellValueFactory(param -> {
            ObservableValue<R> value = new BeanObservableValue<>(property, param.getValue());

            return value;
        });

        column.setCellFactory(param -> {
            TableCell<T, R> cell = new ComboBoxTableCell<T, R>(FXCollections.observableArrayList(itemList)) {
                @Override
                public void updateItem(R item, boolean empty) {
                    super.updateItem(item, empty);

                    if (null != getTableRow() && null != getTableRow().getItem()) {
                        setTextFill(getRowFill((T) getTableRow().getItem()));
                    }
                }
            };

            return cell;
        });

        column.setOnEditCommit(event -> {
            T t = event.getRowValue();
            ReflectUtil.setBeanValue(t, property, event.getNewValue());
            saveRow(t);
        });

        this.getColumns().add(column);

        column.setUserData(new ColumnUserObject(title, property, column.getWidth()));

        return this;
    }

    /**
     * 增加表格列
     *
     * @param title
     * @param property
     * @return
     */
    public <S> LTableView<T> addTableColumn(String title, String property,
                                            Callback<Void, LTableView<S>> callback,
                                            double...width) {
        TableColumn<T, LTableView<S>> column = new TableColumn<>(title);
        column.setCellValueFactory(param -> {
            List<S> list = ReflectUtil.getBeanValue(param.getValue(), property);
            LTableView<S> tableView = callback.call(null);

            if (null != list && null != tableView) {
                tableView.getItems().clear();
                tableView.getItems().addAll(list);

                tableView.setPrefHeight(28 * (list.size() + 1));
            }

            return new ReadOnlyObjectWrapper<>(tableView);
        });

        this.getColumns().add(column);

        if (1 == width.length) {
            column.setMaxWidth(width[0]);
        } else if (2 == width.length) {
            column.setMinWidth(width[0]);
            column.setPrefWidth(width[0]);
            column.setMaxWidth(width[1]);
        }

        return this;
    }

    /**
     * 添加普通列
     *
     * @param title  列显示标题
     * @param property 列在对象中的属性名称
     * @return
     */
    public LTableView<T> addColumn(String title, String property, double...width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        //设置数据处理器
        column.setCellValueFactory(param -> {
            ObservableValue<String> value = new BeanObservableValue<>(property, param.getValue());

            return value;
        });

        column.setCellFactory(new Callback<TableColumn<T, String>, TableCell<T, String>>() {
            @Override
            public TableCell<T, String> call(TableColumn<T, String> param) {
                TableCell<T, String> cell = new TextFieldTableCell<T, String>(new DefaultStringConverter()) {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(null == item ? "" : item, empty);

                        if (null != getTableRow() && null != getTableRow().getItem()) {
                            setTextFill(getRowFill((T) getTableRow().getItem()));
                        }
                    }
                };

                return cell;
            }
        });

        column.setOnEditCommit(event -> {
            T t = event.getRowValue();
            ReflectUtil.setBeanValue(t, property, event.getNewValue());
            saveRow(t);
        });

        //设置列宽
        if (1 == width.length) {
            column.setMaxWidth(width[0]);
        } else if (2 == width.length) {
            column.setMinWidth(width[0]);
            column.setPrefWidth(width[0]);
            column.setMaxWidth(width[1]);
        }

        this.getColumns().add(column);

        column.setUserData(new ColumnUserObject(title, property, column.getWidth()));

        return this;
    }

    /**
     * 添加对象列
     * @param title
     * @param property
     * @param width
     * @return
     */
    public <I> LTableView<T> addObjectColumn(String title, String property, StringConverter<I> convert, double...width) {
        TableColumn<T, I> column = new TableColumn<>(title);
        //设置数据处理器
        column.setCellValueFactory(param -> {
            ObservableValue<I> value = new BeanObservableValue<>(property, param.getValue());

            return value;
        });

        column.setCellFactory(new Callback<TableColumn<T, I>, TableCell<T, I>>() {
            @Override
            public TableCell<T, I> call(TableColumn<T, I> param) {
                TableCell<T, I> cell = new TextFieldTableCell<T, I>(convert) {
                    @Override
                    public void updateItem(I item, boolean empty) {
                        super.updateItem(item, empty);

                        if (null != getTableRow() && null != getTableRow().getItem()) {
                            setTextFill(getRowFill((T) getTableRow().getItem()));
                        }
                    }
                };

                return cell;
            }
        });

        column.setOnEditCommit(event -> {
            T t = event.getRowValue();
            ReflectUtil.setBeanValue(t, property, event.getNewValue());
            saveRow(t);
        });

        //设置列宽
        if (1 == width.length) {
            column.setMaxWidth(width[0]);
        } else if (2 == width.length) {
            column.setMinWidth(width[0]);
            column.setPrefWidth(width[0]);
            column.setMaxWidth(width[1]);
        }

        this.getColumns().add(column);

        column.setUserData(new ColumnUserObject(title, property, column.getWidth()));

        return this;
    }
}
