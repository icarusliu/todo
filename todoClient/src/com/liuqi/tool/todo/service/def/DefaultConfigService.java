/**
 * 文件名称：DefaultConfigService.java
 * 作　　者：刘奇
 * 创建日期： 上午8:36:40
 * 版　　本：1.0
 */
package com.liuqi.tool.todo.service.def;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import org.apache.log4j.Logger;

import com.liuqi.tool.todo.service.ConfigService;

/**
 * 默认配置项类 
 * 从文件中加载配置项并向外提供读取接口  
 * 
 * 
 * @author ctx334
 *
 */
public class DefaultConfigService implements ConfigService {
	//配置项
	private Properties props = new Properties(); 
	private ConfigInitCallable initCallable = null;  
	
	private static Logger logger = Logger.getLogger(DefaultConfigService.class);  
	
	/**
	 * 获取初始化时需要设置默认的服务类 
	 * 通过传入的ConfigInitCallable对象进行默认值设置
	 * 
	 * @param callable
	 */
	public DefaultConfigService(ConfigInitCallable callable) {
		this.initCallable = callable;  
		
		try {
			loadConfigs();
		} catch (TodoException e) {
			logger.error("加载配置文件失败", e);  
		}  
	}

	/** 
	 * 加载配置文件中的内容；  
	 * 调用initConfigs方法，如果配置文件不存在则进行创建并初始化默认配置项
	 * 可进行重复调用，用以从文件中刷新配置项
	 * 
	 * @see ConfigService#loadConfigs()
	 */
	@Override
	public final void loadConfigs() throws TodoException {
		if (initConfigs()) {
			//如果配置文件已经存在，则加载配置文件
			FileReader reader = null;
			
			try {
				reader = new FileReader(new File(CONFIG_FILE_PATH)); 
				props.clear();
				props.load(reader);
			} catch (FileNotFoundException e) {
				logger.error("配置文件不存在", e);
			} catch (IOException e) {
				logger.error("读取配置文件失败", e);
			} finally {
				if (null != reader) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("关闭配置文件失败", e);
					}
				}
			}
		}
	}
	
	/**
	 * 初始化配置文件
	 * 检查配置文件是否存在，如果不存在的话则进行创建，并插入默认的配置项；  
	 * 如果配置文件存在则不进行任何处理
	 * @return 当配置文件存在时，返回true 否则 返回false 
	 * @throws TodoException 当创建配置文件失败或者读取文件失败或者保存配置文件失败时，抛出异常
	 */
	private final boolean initConfigs() throws TodoException {
		File file = new File(CONFIG_FILE_PATH); 
		if (!file.exists()) {
			//如果文件不存在，则创建文件 
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("创建配置文件失败", e);
				
				throw new TodoException(ExceptionCodes.UNDEFINED, "创建配置文件失败！");
			}  
			
			
			Properties pProps = (Properties) props.clone();
			pProps.clear();
			
			initCallable.init(pProps);
			props.clear();
			props.putAll(pProps);
			
			save();
			
			return false; 
		} else {
			return true;
		}
	}

	/**
	 * 将内存中数据刷新到磁盘中 
	 * 
	 * @return
	 * @throws TodoException
	 */
	private void save() throws TodoException {
		File file = new File(CONFIG_FILE_PATH); 
		FileWriter writer;
		
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			logger.error("读取配置文件失败", e);
			
			throw new TodoException(ExceptionCodes.UNDEFINED, "读取文件失败");
		}
		
		try {
			props.store(writer, "");
		} catch (IOException e) {
			logger.error("保存配置文件失败", e);
			
			throw new TodoException(ExceptionCodes.UNDEFINED, "保存配置文件失败！");
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				logger.error("关闭打开的文件失败", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.liuqi.tool.todo.service.ConfigService#getConfigValue(java.lang.String)
	 */
	@Override
	public final String getConfigValue(String name) {
		return props.getProperty(name);
	}
	
	/**
	 * 配置项初始化接口
	 * @author ctx334
	 *
	 */
	public static interface ConfigInitCallable {
		/**
		 * 初始化配置项
		 * 向props中添加各项需要提供默认值的键值对
		 * 
		 * @param props
		 */
		public void init(Properties props) ;  
	}

	/**
	 * 配置文件
	 */
	public static final String CONFIG_FILE_PATH = "config.properties";

	/* (non-Javadoc)
	 * @see com.liuqi.tool.todo.service.ConfigService#put(java.lang.String, java.lang.String)
	 */
	@Override
	public void put(String configName, String value) throws TodoException {
		props.put(configName, value); 
		this.save();
	}  
}
