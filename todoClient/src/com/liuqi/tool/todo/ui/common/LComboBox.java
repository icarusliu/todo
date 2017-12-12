package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/7/27.
 */

import javafx.scene.control.ComboBox;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/27 9:02
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/27 9:02
 **/
public class LComboBox<T> extends ComboBox<T>{
    public LComboBox(List<T> list) {
        this.getItems().add(null);
        this.getItems().addAll(list);

        this.getSelectionModel().select(1);
    }

    public LComboBox(T...args) {
        this.getItems().add(null);
        this.getItems().addAll(args);

        this.getSelectionModel().select(1);
    }

    /**
     * 设置选项值
     */
    public void setItems(List<T> list) {
        this.getItems().addAll(list);
    }

    /**
     * 设置被 选中的对象
     * @param t
     */
    public void select(T t) {
        this.getSelectionModel().select(t);
    }

    /**
     * 设置被 选中的对象
     * @param idx
     */
    public void select(int idx) {
        this.getSelectionModel().select(idx);
    }

    /**
     * 返回选择的对象
     * @return
     */
    public T getSelectedItem() {
        return this.getSelectionModel().getSelectedItem();
    }

    /**
     * 返回选择的对象转换成字符串
     * 如果选择对象为空，返回""
     * @return
     */
    public String getSelectedItemStr() {
        if (getSelectedItem() == null) {
            return "";
        } else {
            return getSelectedItem().toString();
        }
    }
}
