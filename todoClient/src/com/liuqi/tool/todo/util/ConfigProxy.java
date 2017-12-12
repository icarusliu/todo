/**
 * 文   件  名：CommonConfig.java
 * 作          者：刘奇
 * 创建日期：2014-8-9
 * 版          本：1.0
 */
package com.liuqi.tool.todo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.tool.todo.service.ConfigService;
import com.liuqi.tool.todo.service.ServiceFactory;
import com.liuqi.tool.todo.service.def.DefaultConfigService.ConfigInitCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置项代理类
 * 封装一些常用配置项的常量及读取工作
 * 
 * @作者： 刘奇
 * @时间：2014-8-9
 *
 */
public enum ConfigProxy {
	/**
	 * ConfigProxy对象 
	 */
	INSTANCE; 
	
	private final ConfigService configService;

	private static Logger logger = LoggerFactory.getLogger(ConfigProxy.class);

	private ConfigProxy() {
		this.configService = ServiceFactory.getConfigService(new ConfigInitCallable(){

			@Override
			public void init(Properties props) {
			}

		});
	}


	/**
	 * 获取保存的用户名
	 * 
	 * @return 如果没有保存或者保存的是个空字符串，返回null，否则返回保存的用户名
	 */
	public String getSavedName() {
		String name = configService.getConfigValue(USER_NAME);
		return "".equals(name) ? null : name; 
	}

	/**
	 * 返回服务器地址
	 *
	 * @return
	 */
	public String getUrl() {
		String url = "http://localhost/";
		String pUrl = configService.getConfigValue(SERVER_URL);
		if (null == pUrl || "".equals(pUrl)) {
			pUrl = url;

            try {
                configService.put(SERVER_URL, pUrl);
            } catch (TodoException e) {
                 logger.error("Save config failed!", e);
            }
        }

		return pUrl;
	}
	
	/**
	 * 保存用户与密码
	 *
	 * @param name
	 * @throws TodoException
	 */
	public void saveLoginInfo(String name) throws TodoException {
		configService.put(USER_NAME, name);
	}

	/**
	 * 获取系统清单
	 *
	 * @return
	 */
	public List<String> getPrjs() {
		return getConfigListValue(PRJ_LIST, Constants.PRJ_LIST);
	}

	/**
	 * 获取公司清单
	 *
	 * @return
	 */
	public List<String> getCorps() {
		return getConfigListValue(CORP_LIST, Constants.CORP_LIST);
	}

	public boolean showMyCalendar() {
		String str = configService.getConfigValue(SHOW_MYCALENDAR);
		if (null == str || "".equals(str)) {
			try {
				configService.put(SHOW_MYCALENDAR, "");
			} catch (TodoException e) {
				logger.error("Save config value failed!", e);
			}
		}

		if ("true".equals(str)) {
			return true;
		}

		return false;
	}

	public boolean showCalendar() {
		String str = configService.getConfigValue(SHOW_CALENDAR);
		if (null == str || "".equals(str)) {
			try {
				configService.put(SHOW_CALENDAR, "");
			} catch (TodoException e) {
				logger.error("Save config value failed!", e);
			}
		}

		if ("false".equals(str)) {
			return false;
		}

		return true;
	}

	/**
	 * 根据配置项名称获取配置值 ；
	 * 如果无配置项，则保存为空的配置项，并返回默认的配置项值
	 * @param configName
	 * @param defList
	 * @return
	 */
	private final List<String> getConfigListValue(String configName, List<String> defList) {
		String value = configService.getConfigValue(configName);
		List<String> list = new ArrayList<>();

		if (null == value || "".equals(value)) {
			try {
				configService.put(configName, "");
			} catch (TodoException e) {
				logger.error("Save config file failed!", e);
			}

			list.addAll(defList);
		} else {
			String[] strs = value.split(",");
			list.addAll(Stream.of(strs).collect(Collectors.toList()));
		}

		return list;
	}

	/**
	 * 保存的用户名  
	 */
	public static final String USER_NAME = "user.name";

	/**
	 * 服务器URL配置项名称
	 */
    public static final String SERVER_URL = "server.url";

	/**
	 * 公司列表
	 */
	public static final String CORP_LIST = "corp.list";

	/**
	 * 系统清单
	 */
    public static final String PRJ_LIST = "prj.list";

    public static final String SHOW_MYCALENDAR = "show.myCalendar";

    public static final String SHOW_CALENDAR = "show.calendar";
}
