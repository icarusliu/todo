package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/7/25.
 */

import com.liuqi.tool.todo.util.ReflectUtil;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/25 21:30
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/25 21:30
 **/
public abstract class LTreeTableView<T> extends TreeTableView<T> {
    private TreeItem<T> rootItem;

    public LTreeTableView() {
        rootItem = new TreeItem<>();
        this.setRoot(rootItem);
        this.setShowRoot(false);

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
     * 添加CheckBox类型的列
     *
     * @param title
     * @param property
     * @return
     */
    public LTreeTableView<T> addCheckBoxColumn(String title, String property) {
        TreeTableColumn<T, CheckBox> column = new TreeTableColumn<>(title);
        column.setCellValueFactory(param -> {
            CheckBox checkBox = new CheckBox();
            ObservableValue<Boolean> value = new BeanObservableValue<>(property, param.getValue().getValue());
            if (null != value.getValue()) {
                checkBox.setSelected(value.getValue());
            } else {
                checkBox.setSelected(false);
            }

            checkBox.setOnAction(event -> {
                ReflectUtil.setBeanValue(param.getValue().getValue(), property, checkBox.isSelected());

                saveRow(param.getValue().getValue());

                List<TreeItem<T>> list = param.getValue().getChildren();
                if (null != list && checkBox.isSelected()) {
                    //父节点设置成完成时，将所有子节点设置成完成
                    list.forEach(item -> {
                        T t = item.getValue();
                        if (null != t) {
                            ReflectUtil.setBeanValue(t, property, true);
                            saveRow(t);
                        }
                    });
                }

            });

            checkBox.setTextFill(getRowFill(param.getValue().getValue()));

            return new ReadOnlyObjectWrapper<>(checkBox);
        });

        //设置column宽度
        column.setMaxWidth(60d);

        this.getColumns().add(column);

        column.setUserData(new ColumnUserObject(title, property, 60d));

        return this;
    }

    /**
     * 添加下拉的列
     *
     * @param title
     * @param property
     * @return
     */
    public <R> LTreeTableView<T> addComboBoxColumn(String title, String property,
                                               List<R> itemList) {
        TreeTableColumn<T, R> column = new TreeTableColumn<>(title);

        column.setCellValueFactory(param -> {
            ObservableValue<R> value = new BeanObservableValue<>(property, param.getValue().getValue());

            return value;
        });

        column.setCellFactory(param -> {
            TreeTableCell<T, R> cell = new ComboBoxTreeTableCell<T, R>(FXCollections.observableArrayList(itemList)) {
                @Override
                public void updateItem(R item, boolean empty) {
                    super.updateItem(item, empty);

                    if (null != getTreeTableRow() && null != getTreeTableRow().getItem()) {
                        setTextFill(getRowFill((T) getTreeTableRow().getItem()));
                    }
                }
            };

            return cell;
        });

        column.setOnEditCommit(event -> {
            T t = event.getRowValue().getValue();
            ReflectUtil.setBeanValue(t, property, event.getNewValue());
            saveRow(t);
        });

        this.getColumns().add(column);

        column.setUserData(new ColumnUserObject(title, property, column.getWidth()));

        return this;
    }

    /**
     * 添加普通列
     *
     * @param title  列显示标题
     * @param property 列在对象中的属性名称
     * @return
     */
    public LTreeTableView<T> addColumn(String title, String property, double...width) {
        TreeTableColumn<T, String> column = new TreeTableColumn<>(title);
        //设置数据处理器
        column.setCellValueFactory(param -> {
            ObservableValue<String> value = new BeanObservableValue<>(property, param.getValue().getValue());

            return value;
        });

        column.setOnEditCommit(event -> {
            T t = event.getRowValue().getValue();
            ReflectUtil.setBeanValue(t, property, event.getNewValue());
            saveRow(t);
        });

        column.setCellFactory(new Callback<TreeTableColumn<T, String>, TreeTableCell<T, String>>() {
            @Override
            public TreeTableCell<T, String> call(TreeTableColumn<T, String> param) {
                TreeTableCell<T, String> cell = new TextFieldTreeTableCell<T, String>(new DefaultStringConverter()) {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (null != getTreeTableRow() && null != getTreeTableRow().getItem()) {
                            setTextFill(getRowFill((T) getTreeTableRow().getItem()));
                        }
                    }
                };

                return cell;
            }
        });

        column.setOnEditCommit(event -> {
            T t = event.getRowValue().getValue();
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
