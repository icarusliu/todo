package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/9/15.
 */

import com.liuqi.learn.jpa.repositories.ReRunRepository;
import com.liuqi.learn.model.ReRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     重跑日志控制器
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/15 10:02
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/15 10:02
 **/
@RestController
@RequestMapping("/rerun")
public class ReRunController {
    @Autowired
    private ReRunRepository rr;

    /**
     * 获取所有重跑记录
     *
     * @return
     */
    @RequestMapping("/list")
    public Iterable<ReRun> list() {
        return rr.findAll();
    }

    @RequestMapping("/save")
    public ReRun save(@RequestBody ReRun reRun) {
        return rr.save(reRun);
    }
}
