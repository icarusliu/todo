/**
 * 文   件  名：ServiceFactory.java
 * 作          者：刘奇
 * 创建日期：2014-7-26
 * 版          本：1.0
 */
package com.liuqi.tool.todo.service;

import com.liuqi.tool.todo.service.def.DefaultConfigService;
import com.liuqi.tool.todo.service.def.DefaultConfigService.ConfigInitCallable;
import com.liuqi.tool.todo.service.def.DefaultExcelService;

/**
 * 
 * @作者： 刘奇
 * @时间：2014-7-26
 *
 */
public class ServiceFactory {
	private static ConfigService configService;
	private static ExcelService excelService;
	
	public static ConfigService getConfigService(ConfigInitCallable callable) {
		if (null == configService) {
			configService = new DefaultConfigService(callable);  
		}
		
		return configService; 
	}

	public static ExcelService getExcelService() {
		if (null == excelService) {
			excelService = new DefaultExcelService();
		}

		return excelService;
	}
	
}
