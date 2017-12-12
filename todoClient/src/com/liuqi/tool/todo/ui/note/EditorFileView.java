package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/8/31.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Note;
import com.liuqi.learn.model.NoteFile;
import com.liuqi.tool.todo.ui.common.LButton;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     文件附件编辑页面
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/31 11:12
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/31 11:12
 **/
class EditorFileView extends HBox {
    private Note note;

    private Button addButton;

    private FileChooser fileChooser;

    private static Logger logger = LoggerFactory.getLogger(EditorFileView.class);

    EditorFileView() {
        this.fileChooser = new FileChooser();
        this.addButton = new LButton("添加附件", e -> addFile());

        this.getChildren().add(addButton);
    }

    public void setNote(Note note) {
        this.note = note;

        List<NoteFile> list = note.getFileList();
        if (null == list) {
            return;
        }

        list.forEach(item -> getChildren().add(0, new DeleteFileButton(item, e -> {
            list.remove(item);
            getChildren().remove(e);
            return null;
        })));
    }

    public Note getNote() {
        return note;
    }

    /**
     * 上传文件
     */
    private final void addFile() {
        if (null == note) {
            AlertProxy.showErrorAlert("请先保存文档再添加附件！");

            return;
        }

        File file = this.fileChooser.showOpenDialog(null);
        if (null == file) {
            return;
        }

        //上传文件
        String filePath = null;
        try {
            filePath = AjaxProxy.uploadFile(file.getAbsolutePath());
        } catch (TodoException e) {
            logger.error("Upload file failed!", e);
            return;
        }

        //上传成功，新增NOTEFILE记录
        if (null != filePath && !"".equals(filePath)) {
            NoteFile nf = new NoteFile();
            nf.setFileName(file.getName());
            nf.setSavedFileName(filePath);
            List<NoteFile> fileList = note.getFileList();
            if (null == fileList) {
                fileList = new ArrayList<>();
                note.setFileList(fileList);
            }

            fileList.add(nf);

            final List<NoteFile> pFileList = fileList;

            //显示文件删除按钮
            getChildren().add(0, new DeleteFileButton(nf, e -> {
                pFileList.remove(nf);
                getChildren().remove(e);
                return null;
            }));
        }
    }
}
