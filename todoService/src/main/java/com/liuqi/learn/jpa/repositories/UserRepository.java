package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/7/4.
 */

import com.liuqi.learn.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *     用户对象数据库操作类
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/4 8:25
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/4 8:25
 **/
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * 通过用户名查询用户对象
     *
     * @param name
     * @return
     */
    public User findUserByName(String name);
}
