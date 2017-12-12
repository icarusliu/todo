package com.liuqi.tool.todo.util;/**
 * Created by icaru on 2017/7/24.
 */

import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/24 18:13
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/24 18:13
 **/
public class Cache {
    private static List<User> userList;

    private static User loginUser;

    private static Logger logger = LoggerFactory.getLogger(Cache.class);

    public static List<User> refreshUsers() {
        userList = null;
        return getUserList();
    }

    /**
     * 获取缓存的用户清单，返回的是一个克隆的清单
     */
    public static List<User> getUserList() {
        if (null == userList) {
            try {
                userList = AjaxProxy.listAllUsers();
            } catch (TodoException e) {
                logger.error("Get users failed!", e);
            }

            if (null == userList) {
                userList = new ArrayList<>();
            }
        }

        List<User> result = new ArrayList<>();
        result.addAll(userList);
        return result;
    }

    /**
     * 根据登录的用户获取有权限查看的用户清单
     * @return
     */
    public static List<User> getAuthUserList() {
        User user = getLoginUser();
        List<User> userList = null;

        if (user.isNormalUser()) {
            userList = new ArrayList<>();
            userList.add(user);
            return userList;
        }

        userList = refreshUsers();

        if (user.isAdmin()) {
            return userList;
        }

        Iterator<User> it = userList.iterator();
        while (it.hasNext()) {
            User pUSer = it.next();
            if (!pUSer.getCorp().equals(user.getCorp())) {
                it.remove();
            }
        }

        return userList;
    }

    /**
     * 设置当前登录用户
     */
    public static void setLoginUser(User user) {
        loginUser = user;
    }

    /**
     * 获取登录用户
     *
     * @return
     */
    public static User getLoginUser() {
       return loginUser;
    }
}
