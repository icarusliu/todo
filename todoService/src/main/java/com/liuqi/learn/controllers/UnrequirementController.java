package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/7/19.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.learn.model.User;
import com.liuqi.learn.jpa.repositories.UnrequirementRepository;
import com.liuqi.learn.jpa.repositories.UserRepository;
import com.liuqi.learn.util.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 22:35
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 22:35
 **/
@RestController
@RequestMapping("/unrequirement")
public class UnrequirementController {
    private static Logger logger = LoggerFactory.getLogger(UnrequirementController.class);

    @Autowired
    private UnrequirementRepository ur;

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据登录的用户及计划日期查找当天待办事项
     *
     * @param loginUser
     * @param date
     * @return
     * @throws TodoException
     */
    @RequestMapping("/listForPlanDate/{loginUser}/{date}")
    public Iterable<Unrequirement> listForPlanDate(@PathVariable String loginUser,
                                                   @PathVariable String date) throws TodoException {
        User user = userRepository.findUserByName(loginUser);
        if (null == user) {
            throw new TodoException(ExceptionCodes.USER_NOT_EXIST, "用户不存在！");
        }

        if (user.isAdmin()) {
            return ur.findByPlanDate(date, CalendarUtil.getNowDateStr());
        } else if (user.isCorpAdmin()) {
            //公司管理员，看公司所有的，以及分配用户是所有人的
            String corp = user.getCorp();
            Iterable<Unrequirement> list = ur.findByPlanDate(date, CalendarUtil.getNowDateStr());
            List<Unrequirement> result = new ArrayList<>();

            list.iterator().forEachRemaining(item -> {
                User pUser = item.getDev();
                if (null != pUser) {
                    if (corp.equals(pUser.getCorp()) || pUser.isAll()) {
                        result.add(item);
                    }
                }
            });
            return result;
        } else {
            //普通用户，只能看自己的，以及分配给本公司的，以及分配给所有人的
            List<Unrequirement> result = new ArrayList<>();
            Iterable<Unrequirement> list = ur.findByPlanDate(date, CalendarUtil.getNowDateStr());

            list.iterator().forEachRemaining(item -> {
                User pUser = item.getDev();
                if (null != pUser) {
                    //分配给所有人的/公司的以及自己的都可以看到
                    if (pUser.isAll()
                            || (pUser.isCorpUser() && pUser.getCorp().equals(user.getCorp()))
                            || user.getName().equals(pUser.getName())) {
                        result.add(item);
                    }
                }
            });
            return result;
        }
    }

    /**
     * 根据登录的用户查询所有的待办事项
     * 如果用户是管理员，查看所有待办事项
     * 如果是公司管理员，查看所在公司待办事项
     * 如果是普通用户，查看自己的所有待办事项
     *
     * @param loginUser
     * @return
     * @throws TodoException
     */
    @RequestMapping("/list/{loginUser}")
    public Iterable<Unrequirement> list(@PathVariable String loginUser) throws TodoException {
        User user = userRepository.findUserByName(loginUser);
        if (null == user) {
            throw new TodoException(ExceptionCodes.USER_NOT_EXIST, "用户不存在！");
        }

        if (user.isAdmin()) {
            return ur.findAll();
        } else if (user.isCorpAdmin()) {
            //公司管理员，看公司所有的
            String corp = user.getCorp();
            Iterable<Unrequirement> list = ur.findAll();
            List<Unrequirement> result = new ArrayList<>();

            list.iterator().forEachRemaining(item -> {
                User pUser = item.getDev();
                if (null != pUser) {
                    if (corp.equals(pUser.getCorp()) || pUser.isAll()) {
                        result.add(item);
                    }
                }
            });
            return result;
        } else {
            List<Unrequirement> result = new ArrayList<>();
            //普通用户，只能看自己的，以及分配给本公司的
            Iterable<Unrequirement> ownList = ur.findAll();
            ownList.iterator().forEachRemaining(item -> {
                User pUser = item.getDev();
                if (null != pUser) {
                    //分配给所有人的/公司的以及自己的都可以看到
                    if (pUser.isAll()
                            || (pUser.isCorpUser() && pUser.getCorp().equals(user.getCorp()))
                            || user.getName().equals(pUser.getName())) {
                        result.add(item);
                    }
                }
            });

            return result;
        }
    }

    /**
     * 更新对应ID的待办事项状态
     * 同时更新完成时间
     *
     * @param id
     */
    @RequestMapping("/updateStatus/{id}/{status}")
    public void updateStatus(@PathVariable Integer id, @PathVariable boolean status) throws TodoException {
        Unrequirement u = ur.findOne(id);
        if (null == u) {
            throw new TodoException(ExceptionCodes.TODO_NOT_EXIST, "无此编号的待办事项");
        }

        u.setStatus(status);
        if (status) {
            u.setActualDate(CalendarUtil.getNowDateStr());
        } else {
            u.setActualDate(null);
        }

        ur.save(u);
    }

    /**
     * 保存待办事项
     * 接收JSON格式上传的对象并保存
     *
     * @param unrequirement
     */
    @RequestMapping("/save")
    public void save(@RequestBody Unrequirement unrequirement) {
        logger.info("Save unrequirement!");

        ur.save(unrequirement);
    }
}
