package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/9/1.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.NoteFile;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * <p>
 *     删除文件按钮
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/1 20:13
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/1 20:13
 **/
class DeleteFileButton extends Button {
    private NoteFile noteFile;

    private static final Logger logger = LoggerFactory.getLogger(DeleteFileButton.class);

    DeleteFileButton(NoteFile noteFile, Callback<DeleteFileButton, ?> callback) {
        this.noteFile = noteFile;
        this.setText(getFileName() + "(删除)");
        this.setStyle("-fx-background-color: none; -fx-border-style: none; -fx-text-decoration: underline; ");
        this.setCursor(Cursor.HAND);

        this.setOnAction(e -> {
            deleteFile();
            callback.call(this);
        });
    }

    /**
     * 删除文件
     */
    private final void deleteFile() {
        try {
            AjaxProxy.deleteNoteFile(noteFile);
        } catch (TodoException e) {
            logger.error("Delete NoteFile failed!", e);

            AlertProxy.showErrorAlert("删除文档附件失败， 错误信息：" + e.getMessage());
        }
    }

    /**
     * 获取文件名称
     *
     * @return
     */
    private final String getFileName() {
        File file = new File(noteFile.getFileName());
        return file.getName();
    }
}
