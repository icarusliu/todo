package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/7/19.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.jpa.repositories.UserRepository;
import com.liuqi.learn.jpa.repositories.WorkOffRepository;
import com.liuqi.learn.model.User;
import com.liuqi.learn.model.WorkOff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 23:53
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 23:53
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository ur;

    @Autowired
    private WorkOffRepository wor;

    @RequestMapping("/saveUser")
    public void saveUser(@RequestBody User user) throws TodoException {
        Long id = user.getId();

        if (null == id) {
            User findUser = ur.findUserByName(user.getName());
            if (null != findUser) {
                throw new TodoException(ExceptionCodes.USER_EXISTS, "新增用户失败， 用户已存在！");
            }
        }

        ur.save(user);
    }

    @RequestMapping("/save/{userName}/{corp}/{role}")
    public void save(@PathVariable String userName,
                     @PathVariable String corp,
                     @PathVariable String role) throws TodoException {
        User findUser = ur.findUserByName(userName);
        if (null != findUser) {
            throw new TodoException(ExceptionCodes.USER_EXISTS, "新增用户失败， 用户已存在！");
        }

        User user = new User();
        user.setName(userName);
        user.setPassword("123456");
        user.setCorp(corp);
        user.setRole(role);
        ur.save(user);
    }

    @RequestMapping("/list")
    public Iterable<User> list() {
        return ur.findAll();
    }

    /**
     * 请假
     * @param workOff
     */
    @RequestMapping("/workOff/save")
    public void workOff(@RequestBody WorkOff workOff) {
        wor.save(workOff);
    }

    /**
     * 获取所有请假信息
     * @return
     */
    @RequestMapping("/workOff/list")
    public Iterable<WorkOff> listAllWorkOffs() {
        return wor.findAll();
    }
}
