package com.liuqi.tool.todo.util;/**
 * Created by icaru on 2017/9/4.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/4 11:13
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/4 11:13
 **/
enum AjaxCache {
    INSTANCE;

    private Map<String, Object> cacheMap;

    public void put(String key, Object obj) {
        if (null == cacheMap) {
            cacheMap = new HashMap<>();
        }

        cacheMap.put(key, obj);
    }

    public Object get(String key) {
        if (null == cacheMap) {
            return null;
        }

        return cacheMap.get(key);
    }
}
