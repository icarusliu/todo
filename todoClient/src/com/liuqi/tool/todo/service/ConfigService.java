/**
 * 文   件  名：ConfigUtil.java
 * 作          者：刘奇
 * 创建日期：2014-8-9
 * 版          本：1.0
 */
package com.liuqi.tool.todo.service;

import com.liuqi.learn.exceptions.TodoException;

/**
 * 配置项服务类
 * 
 * @作者： 刘奇
 * @时间：2014-8-9
 *
 */
public interface ConfigService {
	/**
	 * 加载配置项
	 * 如果不存在配置文件，则需要创建配置文件；并添加默认配置项  
	 * 否则，直接读取配置文件中的内容
	 * @throws TodoException 
	 */
	public void loadConfigs() throws TodoException;
	
	/**
	 * 获取配置项的值
	 * 
	 * @param name
	 * @return
	 */
	public String getConfigValue(String name);  
	
	/**
	 * 保存配置项
	 * 
	 * @param configName 配置项名称　
	 * @param value　配置项值　
	 * @throws TodoException 
	 */
	public void put(String configName, String value) throws TodoException;  
}
