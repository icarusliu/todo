package com.liuqi.tool.todo.ui.env;/**
 * Created by icaru on 2017/8/17.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.EnvServerType;
import com.liuqi.learn.model.Environment;
import com.liuqi.learn.model.EnvironmentType;
import com.liuqi.tool.todo.ui.common.LTableView;
import com.liuqi.tool.todo.ui.common.LToolBarTableView;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.AlertProxy;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 13:05
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 13:05
 **/
public class EnvView extends LToolBarTableView<EnvTableObject> {
    private static Logger logger = LoggerFactory.getLogger(EnvView.class);
    private NewEnvDialog dialog = new NewEnvDialog();

    public void afterConstruct() {
        this.tableView.setStyle("");
    }

    @Override
    public void initColumns() {
        this.tableView.addColumn("系统", "system")
            .addTableColumn("环境", "envs", param -> getSubTable(), 1000, 2000)
                ;
    }

    private LTableView<Environment> getSubTable() {
        LTableView<Environment> tableView =
                new LTableView<Environment>() {
                    @Override
                    public Paint getRowFill(Environment environment) {
                        return Color.BLACK;
                    }

                    @Override
                    public void saveRow(Environment environment) {
                        try {
                            AjaxProxy.saveEnv(environment);
                        } catch (TodoException e) {
                            logger.error("Save environment failed, error message: " + e.getMessage(), e);
                        }
                    }
                };

        tableView.addComboBoxColumn("类型", "type"
                    , Stream.of(EnvironmentType.values()).collect(Collectors.toList()))
                .addComboBoxColumn("服务器类型", "serverType"
                    , Stream.of(EnvServerType.values()).collect(Collectors.toList()))
                .addColumn("地址", "address")
                .addColumn("用户", "users")
                .addColumn("备注", "remark")
                ;

        return tableView;
    }

    @Override
    public void initToolBar() {
        Button addButton = new Button("新增");
        this.toolBar.getItems().add(addButton);
        addButton.setOnAction(e -> {
            Optional<Environment> result = dialog.showAndWait();
            result.ifPresent(environment -> refreshData());
        });

        TextField keyField = new TextField();
        keyField.setTooltip(new Tooltip("关键字搜索"));
        this.toolBar.getItems().add(keyField);
        keyField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String key = keyField.getText().trim();
                if ("".equals(key)) {
                    refreshData();
                    return;
                }

                List<EnvTableObject> list = getItems();
                Iterator<EnvTableObject> it = list.iterator();
                while (it.hasNext()) {
                    EnvTableObject item = it.next();
                    if (item.getSystem().contains(key)) {
                        continue;
                    }

                    boolean find = false;

                    if (null != item.getEnvs()) {
                        for (Environment environment : item.getEnvs()) {
                            String pKey = environment.getAddress() + environment.getRemark();
                            if (pKey.contains(key)) {
                                find = true;
                                break;
                            }
                        }
                    }

                    if (!find) {
                        it.remove();
                    }
                }

                tableView.getItems().clear();
                tableView.getItems().addAll(list);
            }
        });
    }

    @Override
    public void saveRow(EnvTableObject envTableObject) {

    }

    @Override
    public Paint getRowFill(EnvTableObject environment) {
        return Color.BLACK;
    }

    @Override
    public List<EnvTableObject> getItems() {
        try {
            List<Environment> envs = AjaxProxy.listAllEnvs();
            List<EnvTableObject> result = new ArrayList<>();
            String pSystem = "";
            EnvTableObject obj = null;

            for (Environment e : envs) {
                String system = e.getSystem();
                if (system.equals(pSystem)) {
                    //与上一个处理的是同一个系统
                    obj.getEnvs().add(e);
                } else {
                    if (null != obj) {
                        result.add(obj);
                    }

                    pSystem = system;
                    obj = new EnvTableObject();
                    obj.setSystem(system);

                    List<Environment> children = new ArrayList<>();
                    children.add(e);

                    obj.setEnvs(children);
                }
            }

            if (null != obj) {
                result.add(obj);
            }

            return result;
        } catch (TodoException e) {
            logger.error("Get all envs failed, errormessage: " + e.getMessage(), e);

            AlertProxy.showErrorAlert("获取环境信息失败, 错误信息：" + e.getMessage());
            return new ArrayList<>();
        }
    }

}
