package com.liuqi.tool.todo.ui.note;/**
 * Created by icaru on 2017/8/29.
 */

import com.liuqi.learn.model.Note;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     文档展示界面
 *     显示文档用户信息以及文档具体内容 ；
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/29 11:01
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/29 11:01
 **/
public class ShowView extends BorderPane{
    private ToolBar messageBar;
    private Text userText;
    private Text createTimeText;

    private WebView webView;

    private ToolBar fileBar;

    private Note note;

    private static Logger logger = LoggerFactory.getLogger(ShowView.class);

    public ShowView(Note note) {
        this.note = note;
        this.messageBar = new ToolBar();
        this.webView = new WebView();
        this.userText = new Text();
        this.createTimeText = new Text();
        this.fileBar = new ToolBar();

        this.webView.setContextMenuEnabled(false);

        initMessageBar();
        initFileBar();

        this.setBottom(messageBar);
        this.setCenter(webView);

        setNote(note);
    }

    /**
     * 初始化附件栏
     */
    private final void initFileBar() {
        this.fileBar.setStyle("-fx-background-color: #efefef;");
    }

    /**
     * 初始化信息栏
     * 包含用户/创建时间等信息
     */
    private final void initMessageBar() {
        this.userText.setFill(Color.BLUE);
        this.createTimeText.setFill(Color.BLUE);

        this.messageBar.setStyle("-fx-background-color: #efefef;");
        this.messageBar.getItems().addAll(new Text("作者："), userText,
                new Text("                      创建时间："),
                createTimeText);
    }

    /**
     * 设置面板所要展示的对象
     *
     * @param note
     */
    public void setNote(Note note) {
        this.note = note;

        if (null == note) {
            return;
        }

        this.userText.setText(note.getUser().getName());
        this.createTimeText.setText(note.getCreateTime());
        this.webView.getEngine().loadContent(note.getContent());

        //初始化附件展示
        if (null != note.getFileList() && 0 != note.getFileList().size()) {
            this.setTop(fileBar);
            note.getFileList().forEach(item -> fileBar.getItems().add(new DownloadFileButton(item)));
        } else {
            this.setTop(null);
        }
    }
}
