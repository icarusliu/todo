package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/8/18.
 */

import com.liuqi.learn.jpa.repositories.EnvironmentRepository;
import com.liuqi.learn.model.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/18 8:34
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/18 8:34
 **/
@RestController
@RequestMapping("/env")
public class EnvironmentController {
    @Autowired
    private EnvironmentRepository er;

    /**
     * 获取所有环境信息
     * @return
     */
    @RequestMapping("/list")
    public Iterable<Environment> list() {
        return er.findAll();
    }

    /**
     * 保存环境信息
     * @param env
     */
    @RequestMapping("/save")
    public void save(@RequestBody Environment env) {
        er.save(env);
    }
}
