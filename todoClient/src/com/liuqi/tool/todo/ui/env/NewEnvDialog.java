package com.liuqi.tool.todo.ui.env;/**
 * Created by icaru on 2017/8/18.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.EnvServerType;
import com.liuqi.learn.model.Environment;
import com.liuqi.learn.model.EnvironmentType;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.ui.common.LDialog;
import com.liuqi.tool.todo.util.AjaxProxy;
import javafx.geometry.Insets;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/18 8:44
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/18 8:44
 **/
public class NewEnvDialog extends LDialog<Environment> {
    private TextField systemField;
    private LComboBox<EnvironmentType> typeComboBox;
    private LComboBox<EnvServerType> serverTypeComboBox;
    private TextField addressField ;
    private TextField usersField;
    private TextArea remarkArea;

    public NewEnvDialog() {
        super("新增环境信息");

        this.setResultConverter(e -> new Environment());
    }

    @Override
    protected void save() throws TodoException {
        String system = systemField.getText().trim();
        EnvironmentType type = typeComboBox.getSelectedItem();
        EnvServerType serverType = serverTypeComboBox.getSelectedItem();
        String address = addressField.getText().trim();
        String users = usersField.getText().trim();
        String remark = remarkArea.getText().trim();

        Environment env = new Environment();
        env.setSystem(system);
        env.setAddress(address);
        env.setType(type);
        env.setServerType(serverType);
        env.setUsers(users);
        env.setRemark(remark);

        AjaxProxy.saveEnv(env);
    }

    @Override
    protected void check() throws TodoException {
        String system = systemField.getText().trim();
        String type = typeComboBox.getSelectedItemStr();
        String serverType = serverTypeComboBox.getSelectedItemStr();
        String address = addressField.getText().trim();

        if ("".equals(system) || "".equals(type) || "".equals(serverType) || "".equals(address)) {
            throw new TodoException(ExceptionCodes.UNDEFINED, "请填写所有必填项");
        }
    }

    @Override
    protected void initView(GridPane gridPane) {
        this.systemField = new TextField();
        this.typeComboBox = new LComboBox(EnvironmentType.values());
        this.serverTypeComboBox = new LComboBox(EnvServerType.values());
        this.addressField = new TextField();
        this.usersField = new TextField();
        this.remarkArea = new TextArea();

        this.remarkArea.setWrapText(true);

        this.systemField.setPrefWidth(300);
        gridPane.addRow(0, new Text("系统"), this.systemField);
        gridPane.addRow(1, new Text("类型"), this.typeComboBox);
        gridPane.addRow(2, new Text("服务器类型"), this.serverTypeComboBox);
        gridPane.addRow(3, new Text("地址"), this.addressField);
        gridPane.addRow(4, new Text("用户"), this.usersField);
        gridPane.addRow(5, new Text("备注"), this.remarkArea);
    }
}
