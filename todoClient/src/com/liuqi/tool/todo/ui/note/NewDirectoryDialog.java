package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/8/24.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Note;
import com.liuqi.tool.todo.ui.calendar.CalendarView;
import com.liuqi.tool.todo.ui.common.LDialog;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.CalendarUtil;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/24 10:26
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/24 10:26
 **/
public class NewDirectoryDialog extends LDialog<Note> {
    private TextField titleField;
    private TreeItem<Note> parent;

    public NewDirectoryDialog() {
        super("新增目录");

        Platform.runLater(() -> titleField.requestFocus());

        this.setResultConverter(param -> null);
    }

    public void setParent(TreeItem<Note> parent) {
        this.parent = parent;
    }

    @Override
    protected void save() throws TodoException {
        String title = titleField.getText().trim();
        Note note = new Note();
        note.setDirectory(true);
        note.setName(title);
        note.setCreateTime(CalendarUtil.getNowTimeStr());
        note.setUser(Cache.getLoginUser());
        if (null != parent) {
            note.setParent(parent.getValue().getId());
            if (null == parent.getValue().getChildrens())  {
                List<Note> list = new ArrayList<>();
                parent.getValue().setChildrens(list);
            }
        }
        Note rNote = AjaxProxy.saveNote(note);
        if (null != parent) {
            parent.getValue().getChildrens().add(rNote);
        }

        this.setResultConverter(e -> rNote);
    }

    @Override
    protected void check() throws TodoException {
        String title = titleField.getText().trim();
        if ("".equals(title)) {
            throw new TodoException(ExceptionCodes.UI_VALUE_NULL, "名称不能为空！");
        }
    }

    @Override
    protected void initView(GridPane gridPane) {
        this.titleField = new TextField();
        gridPane.addRow(0, new Text("目录名称"), titleField);
    }
}
