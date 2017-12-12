package com.liuqi.tool.todo.ui.calendar;/**
 * Created by icaru on 2017/7/28.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.Detail;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/28 12:50
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/28 12:50
 **/
public class DetailView extends BorderPane {
    private TextField titleText = new TextField();
    private TextArea contentText = new TextArea();

    private Button saveButton = new Button("保存");

    private Unrequirement u;

    private static Logger logger = LoggerFactory.getLogger(DetailView.class);

    public DetailView() {
        this.setPadding(new Insets(20, 10, 10, 10));

        Label mainTitleText = new Label("详细信息");
        this.setTop(mainTitleText);
        BorderPane.setAlignment(mainTitleText, Pos.BASELINE_CENTER);
        mainTitleText.setTextFill(Color.BLUE);

        GridPane pane = new GridPane();
        pane.setVgap(10);
        Text title = new Text("待办事项：");
        title.setFill(Color.GRAY);
        pane.addRow(0, title, titleText);

        Text contentTitle = new Text("详细信息：");
        contentTitle.setFill(Color.GRAY);
        this.setPrefWidth(100);  //如果不设置，宽度将被拉宽；
        contentText.setWrapText(true);
        pane.addRow(1, contentTitle, contentText);

        this.setCenter(pane);

        this.setBottom(saveButton);
        BorderPane.setAlignment(saveButton, Pos.BASELINE_RIGHT);

        saveButton.setOnAction(e -> {
            if (null == u) {
                return;
            }

            if ("".equals(titleText.getText().trim())) {
                return;
            }

            u.setTitle(titleText.getText().trim());
            u.setContent(contentText.getText().trim());

            try {
                AjaxProxy.save(u);
            } catch (TodoException e1) {
                logger.error("Save unrequirement failed!", e1);

                AlertProxy.showErrorAlert("保存待办事项失败！");
            }
        });

        saveButton.setDisable(true);
    }

    public void setUnrequirement(Unrequirement u) {
        if (null == u) {
            saveButton.setDisable(true);
            return;
        }

        saveButton.setDisable(false);

        this.u = u;

        this.titleText.setText(u.getTitle());
        this.contentText.setText(u.getContent());
    }
}
