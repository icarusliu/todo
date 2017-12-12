/**
 * 文件名称：ExcelExportDialog.java
 * 作　　者：刘奇
 * 创建日期： 上午12:42:45
 * 版　　本：1.0
 */
package com.liuqi.tool.todo.ui.common;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.liuqi.tool.todo.service.ExcelService;
import com.liuqi.tool.todo.service.ServiceFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 * 文件导出对话框
 * 在需要导出文件的界面中进行调用； 
 * 可以选择文件保存路径来进行存储；
 * 只能导出Excel格式的文件；
 * 做成单例模式，在多次调用时使用同一个对象 
 * <br>
 * 作　　者：刘奇<br>
 * 创建时间：2017年6月30日<br>
 * 对象版本：V1.0<br>
 *－－－－－－－－－－修改记录－－－－－－－－－－<br>
 * 版　　本：V1.*<br>
 * 修改内容：<br>
 * 修改　人：刘奇<br>
 * 修改时间：<br>
 *－－－－－－－－－－－－－－－－－－－－－－－－<br>
 */
public enum ExcelExportDialogProxy{
	INSTANCE;

	private ExcelExportDialogProxy() {
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel文件", "*.xls"));
    }
	
	//文件保存对话框组件
	private final FileChooser fileChooser = new FileChooser();

	public <T> boolean saveFile(TreeTableView<T> table, String fileName) {
		//设置默认的文件名称
		fileChooser.setInitialFileName(fileName);

		//显示文件保存对话框
		File file = fileChooser.showSaveDialog(null);

		if (null != file) {
			ExcelService excelService = ServiceFactory.getExcelService();
			List<ColumnUserObject> list = table.getColumns().parallelStream().map(e -> (ColumnUserObject)e.getUserData())
					.collect(Collectors.toList());

			List<TreeItem<T>> children = table.getRoot().getChildren();
			List<T> items = children.parallelStream().map(e -> e.getValue())
					.collect(Collectors.toList());

			excelService.toExcel(list, items, file.getAbsolutePath());
			return true;
		}

		//选择取消按钮时，未进行文件保存
		return false;
	}

	/**
	 * 将表格中的数据保存到Excel中；  
	 * 
	 * @param table 需要保存数据的表格 
	 * @param fileName 保存的默认文件名
	 * @return
	 */
	public <T> boolean saveFile(TableView<T> table, String fileName) {
		//设置默认的文件名称
		fileChooser.setInitialFileName(fileName);

		//显示文件保存对话框
		File file = fileChooser.showSaveDialog(null);

		if (null != file) {
            ExcelService excelService = ServiceFactory.getExcelService();
            List<ColumnUserObject> list = table.getColumns().parallelStream().map(e -> (ColumnUserObject)e.getUserData())
					.collect(Collectors.toList());

			excelService.toExcel(list, table.getItems(), file.getAbsolutePath());
            return true;
        }

		//选择取消按钮时，未进行文件保存
		return false;
	}
}
