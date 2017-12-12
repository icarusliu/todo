package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/8/2.
 */

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/2 19:18
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/2 19:18
 **/
public class ColumnUserObject {
    private String title;
    private String property;
    private double width;

    public ColumnUserObject(String title, String property, double width) {
        this.title = title;
        this.property = property;
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
