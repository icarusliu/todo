package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/8/23.
 */

import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * <p>
 *     按钮封装
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/23 16:22
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/23 16:22
 **/
public class LButton extends Button {
    public LButton(String title, EventHandler handler) {
        super(title);
        this.setOnAction(handler);
    }
}
