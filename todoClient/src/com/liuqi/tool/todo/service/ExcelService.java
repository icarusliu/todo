/**
 * 文件名称：ExcelService.java
 * 作　　者：刘奇
 * 创建日期： 下午7:23:01
 * 版　　本：1.0
 */
package com.liuqi.tool.todo.service;

import com.liuqi.tool.todo.ui.common.ColumnUserObject;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Excel导出工具服务接口
 * 
 * @author ctx334
 *
 */
public interface ExcelService {
	/**
	 * 将表格中的数据导出成Excel
	 * 
	 * @param fileName
	 */
	public <T> void toExcel(List<ColumnUserObject> columns, List<T> items, String fileName);
}
