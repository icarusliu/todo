package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/7/26.
 */

import com.liuqi.learn.model.Requirement;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

/**
 * <p>
 *    需求对象数据库操作类
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/26 23:34
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/26 23:34
 **/
@Repository
public interface RequirementRepository extends CrudRepository<Requirement, Integer>{
    /**
     * 通过公司查找需求
     *
     * @param corp
     * @return
     */
    public Iterable<Requirement> findByCorp(String corp);

    /**
     * 获取提需求的业务人员清单
     *
     * @return
     */
    @Query("select distinct user from Requirement")
    public Iterable<String> getBusiUsers();
}
