/**
 * 文件名称：DefaultExcelService.java
 * 作　　者：刘奇
 * 创建日期： 下午7:27:25
 * 版　　本：1.0
 */
package com.liuqi.tool.todo.service.def;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.liuqi.tool.todo.ui.common.ColumnUserObject;
import com.liuqi.tool.todo.util.ReflectUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.liuqi.tool.todo.service.ExcelService;

/**
 * 默认的Excel服务类
 * <br>
 * 作　　者：刘奇<br>
 * 创建时间：2017年6月25日<br>
 * 对象版本：V1.0<br>
 *－－－－－－－－－－修改记录－－－－－－－－－－<br>
 * 版　　本：V1.1<br>
 * 修改内容：修改导出时未判断对象值是否为空的BUG<br>
 * 修改　人：刘奇<br>
 * 修改时间：20170625<br>
 *－－－－－－－－－－－－－－－－－－－－－－－－<br>
 */
public class DefaultExcelService implements ExcelService {
	private static Logger logger = Logger.getLogger(DefaultExcelService.class); 

	/* (non-Javadoc)
	 * @see com.liuqi.tool.todo.service.ExcelService#toExcel(java.util.List, java.lang.String)
	 */
	@Override
	public <T> void toExcel(List<ColumnUserObject> columns, List<T> items, String fileName) {
		File file = new File(fileName);
		 try {
			WritableWorkbook wb = Workbook.createWorkbook(file);
			WritableSheet sheet = wb.createSheet("结果", 0);
			
			//设置是否显示边框 
			sheet.getSettings().setShowGridLines(true);
			
			int pColumn = 0;  
			int pRow = 0;  
			
			//标题行样式  
			WritableCellFormat titleFormat = new WritableCellFormat();
			titleFormat.setBackground(Colour.GRAY_25);
			titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			
			WritableCellFormat contentFormat = new WritableCellFormat();
			contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

			 for (ColumnUserObject cObj : columns) {
				 String title = cObj.getTitle();
				 String property = cObj.getProperty();

				//设置列宽
				sheet.setColumnView(pColumn, (int)cObj.getWidth() / 5);

				//处理每一列的标题
				Label label = new Label(pColumn, pRow++, title, titleFormat);
				sheet.addCell(label);

				//处理每一列的数据；
				for (T t: items) {
					//V1.1 对应的值可能为空
					Object objValue = ReflectUtil.getBeanValue(t, property);
					String value = "";
					if (null != objValue) {
						value = objValue.toString();
					}

					sheet.addCell(new Label(pColumn, pRow++, value, contentFormat));
				}

				pRow = 0;
				pColumn++;
			}

			wb.write();
			wb.close();
		} catch (IOException e) {
			logger.error("读取文件失败", e);
		} catch (RowsExceededException e) {
			logger.error("行数超限制", e);
		} catch (WriteException e) {
			logger.error("保存文件失败", e);
		} 
	}
	
}
