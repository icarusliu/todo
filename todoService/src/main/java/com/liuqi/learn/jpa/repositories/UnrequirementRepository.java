package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/7/19.
 */

import com.liuqi.learn.model.Unrequirement;
import com.liuqi.learn.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 22:37
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 22:37
 **/
@Repository
public interface UnrequirementRepository extends CrudRepository<Unrequirement, Integer>{
    /**
     * 通过责任人查找待办事项
     *
     * @param dev
     * @return
     */
    public Iterable<Unrequirement> findByDev(User dev);

    /**
     * 通过责任人及计划日期查找待办事项
     *
     * @param dev
     * @param planDate
     * @return
     */
    @Query("select u from Unrequirement u where dev=?1 and planDate is not null and planDate <> '' and " +
            "(planDate = ?2 or (?2 = ?3 and planDate < ?2 and status = false))")
    public Iterable<Unrequirement> findByDevAndPlanDate(User dev, String planDate, String nowDate);

    /**
     * 通过计划日期查找待办事项
     *
     * @param planDate
     * @return
     */
    @Query("select u from Unrequirement u where planDate = ?1 " +
            "or (?2 = ?1 and planDate < ?1 and status = false)")
    public Iterable<Unrequirement> findByPlanDate(String planDate, String nowDate);
}
