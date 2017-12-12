package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/7/26.
 */

import com.liuqi.learn.jpa.repositories.RequirementRepository;
import com.liuqi.learn.jpa.repositories.UserRepository;
import com.liuqi.learn.model.Requirement;
import com.liuqi.learn.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.modelmbean.RequiredModelMBean;
import java.util.ArrayList;

/**
 * <p>
 *     需求对象Controller
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/26 23:35
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/26 23:35
 **/
@RestController
@RequestMapping("/requirement")
public class RequirementController {
    private Logger logger = LoggerFactory.getLogger(RequirementController.class);

    @Autowired
    private RequirementRepository rr;

    @Autowired
    private UserRepository ur;

    /**
     * 保存需求对象
     * @param requirement
     */
    @RequestMapping("/save")
    public void save(@RequestBody Requirement requirement) {
        logger.info("Save requirement: " + requirement);
        rr.save(requirement);
    }

    /**
     * 获取保存的需求清单中，提出需求的业务人员清单
     *
     * @return
     */
    @RequestMapping("/listBusiUsers")
    public Iterable<String> getBusiUsers() {
        return rr.getBusiUsers();
    }

    /**
     * 通过用户查找需求清单
     *
     * @param username
     * @return
     */
    @RequestMapping("/list/{username}")
    public Iterable<Requirement> list(@PathVariable String username) {
        User user = ur.findUserByName(username);
        if (null == user) {
            logger.error("User does not exist, username:  " + username);

            return new ArrayList<>();
        }

        if (user.isAdmin()) {
            return rr.findAll();
        } else {
            return rr.findByCorp(user.getCorp());
        }
    }
}
