package com.liuqi.tool.todo.ui.user;/**
 * Created by icaru on 2017/8/22.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.User;
import com.liuqi.learn.model.WorkOff;
import com.liuqi.learn.model.WorkOffType;
import com.liuqi.tool.todo.ui.common.LComboBox;
import com.liuqi.tool.todo.ui.common.LDatePicker;
import com.liuqi.tool.todo.ui.common.LDialog;
import com.liuqi.tool.todo.util.AjaxProxy;
import com.liuqi.tool.todo.util.Cache;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/22 11:01
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/22 11:01
 **/
public class NewWorkOffDialog extends LDialog<WorkOff> {
    private LComboBox<User> userComboBox;
    private LComboBox<WorkOffType> typeComboBox;
    private LDatePicker startDatePicker;
    private TextField daysField;
    private TextArea remarkArea;
    private TextField titleField;

    public NewWorkOffDialog() {
        super("新增请假信息");

        this.setResultConverter(e -> new WorkOff());
    }

    @Override
    protected void save() throws TodoException {
        User user = userComboBox.getSelectedItem();
        WorkOffType type = typeComboBox.getSelectedItem();
        String startDate = startDatePicker.getDateStr();
        String days = daysField.getText().trim();
        String title = titleField.getText().trim();
        String remark = remarkArea.getText().trim();

        WorkOff workOff = new WorkOff();
        workOff.setUser(user);
        workOff.setTitle(title);
        workOff.setType(type);
        workOff.setStartDate(startDate);
        workOff.setDays(days);
        workOff.setRemark(remark);

        AjaxProxy.saveWorkOff(workOff);
    }

    @Override
    protected void check() throws TodoException {
        String user = userComboBox.getSelectedItemStr();
        String type = typeComboBox.getSelectedItemStr();
        String startDate = startDatePicker.getDateStr();
        String days = daysField.getText().trim();
        String title = titleField.getText().trim();
//        String remark = remarkArea.getText().trim();

        checkStr(user, type, startDate, days, title);
    }

    private final void checkStr(String...strs) throws TodoException {
        for (String str : strs) {
            if ("".equals(str)) {
                throw new TodoException(ExceptionCodes.UI_VALUE_NULL, "请填写所有必填项!");
            }
        }
    }

    @Override
    protected void initView(GridPane gridPane) {
        this.userComboBox = new LComboBox<>(Cache.getAuthUserList());
        this.typeComboBox = new LComboBox<>(WorkOffType.values());
        this.startDatePicker = new LDatePicker();
        this.daysField = new TextField();
        this.remarkArea = new TextArea();
        this.titleField = new TextField();

        this.remarkArea.setWrapText(true);
        this.userComboBox.select(Cache.getLoginUser());
        this.typeComboBox.select(1);

        this.addRow(gridPane, 0, "请假事项", titleField);
        this.addRow(gridPane, 1, "用户", userComboBox);
        this.addRow(gridPane, 2, "请假分类", typeComboBox);
        this.addRow(gridPane, 3, "开始日期", startDatePicker);
        this.addRow(gridPane, 4, "天数", daysField);
        this.addRow(gridPane, 5, "说明", remarkArea);
    }

    private final void addRow(GridPane gridPane, int row, String title, Node node) {
        gridPane.addRow(row, new Text(title), node);
    }
}
