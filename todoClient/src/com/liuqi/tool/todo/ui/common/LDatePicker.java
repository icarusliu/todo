package com.liuqi.tool.todo.ui.common;/**
 * Created by icaru on 2017/8/22.
 */

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/22 11:05
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/22 11:05
 **/
public class LDatePicker extends DatePicker {
    public LDatePicker(String format) {
        setFormat(format);
    }

    public LDatePicker() {
        setFormat("yyyyMMdd");
    }

    public void setFormat(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringConverter converter = new LocalDateStringConverter(formatter, formatter) ;
        this.setConverter(converter);
    }

    public String getDateStr() {
        return this.getEditor().getText().trim();
    }
}
