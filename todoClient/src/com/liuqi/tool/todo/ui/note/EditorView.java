package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/8/25.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Note;
import com.liuqi.tool.todo.ui.common.LButton;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import com.liuqi.tool.todo.util.Cache;
import com.liuqi.tool.todo.util.CalendarUtil;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * <p>
 *     文档编辑页面
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/25 16:27
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.0
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/25 16:27
 *
 * @Version V1.1
 * @Comments <p>添加上传及删除附件功能</p>
 * @Author icaru
 * @Date 2017/8/31 16:27
 **/
public class EditorView extends BorderPane {
    private TextField titleField;
    private HTMLEditor htmlEditor;

    private TreeItem<Note> parentTreeItem;
    private Note item;
    private EditorFileView editorFileView;

    private Callback<Note, ?> saveCallback;

    private static Logger logger = LoggerFactory.getLogger(EditorView.class);

    EditorView() {
        this.titleField = new TextField();
        this.htmlEditor = new HTMLEditor();
        this.editorFileView = new EditorFileView();
        this.titleField.setPrefHeight(30);

        this.setTop(titleField);
        this.setCenter(htmlEditor);

        Button saveButton = new LButton("保存", e -> save());
        VBox box = new VBox();
        box.getChildren().addAll(this.editorFileView, saveButton);
        this.setBottom(box);

        //初始化完成后，标题得到焦点
        Platform.runLater(() -> titleField.requestFocus());
    }

    /**
     * 保存动作
     */
    public final void save() {
        String title = titleField.getText().trim();
        String content = htmlEditor.getHtmlText();

        Note note = item;
        if (null == item) {
            note = new Note();
            note.setUser(Cache.getLoginUser());
            note.setCreateTime(CalendarUtil.getNowTimeStr());
            if (null != parentTreeItem && null != parentTreeItem.getValue()) {
                note.setParent(parentTreeItem.getValue().getId());
            }
            note.setDirectory(false);
            this.editorFileView.setNote(note);
        }

        note.setName(title);
        note.setContent(content);

        try {
            Note rNote = AjaxProxy.saveNote(note);

            //需要使用返回的对象更新其父对象中的子元素信息
            if (null != parentTreeItem && null != parentTreeItem.getValue()) {
                Note parentNote = parentTreeItem.getValue();
                if (null == parentNote.getChildrens()) {
                    parentNote.setChildrens(new ArrayList<>());
                }

                parentNote.getChildrens().add(rNote);
            }

            if (null != saveCallback) {
                saveCallback.call(rNote);
            }
        } catch (TodoException e1) {
            logger.error("Save note failed!", e1);
            AlertProxy.showErrorAlert("保存失败！");
        }
    }

    public void setParentTreeItem(TreeItem<Note> parent) {
        this.parentTreeItem = parent;
    }

    public TreeItem<Note> getParentTreeItem() {
        return parentTreeItem;
    }

    /**
     * 设置详细面板的展现对象
     * @param note
     */
    public void setItem(Note note) {
        this.item = note;
        this.editorFileView.setNote(note);

        if (null != note) {
            titleField.setText(note.getName());
            htmlEditor.setHtmlText(note.getContent());
        } else {
            titleField.setText("");
            htmlEditor.setHtmlText("");
        }
    }

    public Note getItem() {
        return item;
    }

    public void setSaveCallback(Callback<Note, ?> saveCallback) {
        this.saveCallback = saveCallback;
    }
}
