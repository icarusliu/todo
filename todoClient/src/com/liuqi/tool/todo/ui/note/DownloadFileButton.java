package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/9/1.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.NoteFile;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * <p>
 *     文件按钮
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
class DownloadFileButton extends Button {
    private NoteFile noteFile;

    private final FileChooser fileChooser = new FileChooser();

    private static final Logger logger = LoggerFactory.getLogger(DownloadFileButton.class);

    DownloadFileButton(NoteFile noteFile) {
        this.noteFile = noteFile;
        this.setText(getFileName());
        this.setStyle("-fx-background-color: none; -fx-border-style: none; -fx-text-decoration: underline; ");
        this.setCursor(Cursor.HAND);

        this.setOnAction(e -> downloadFile());
    }

    /**
     * 下载文件
     */
    private final void downloadFile() {
        fileChooser.setInitialFileName(noteFile.getFileName());
        File file = fileChooser.showSaveDialog(null);
        if (null == file) {
            return;
        }

        try {
            AjaxProxy.downloadFile(noteFile.getSavedFileName(), file.getPath());
        } catch (TodoException e) {
            logger.error("Download file failed!", e);

            AlertProxy.showErrorAlert("下载文件失败， 错误信息：" + e.getMessage());
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
