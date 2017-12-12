package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/9/15.
 */

import com.liuqi.learn.model.ReRun;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/9/15 10:01
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/9/15 10:01
 **/
@Repository
public interface ReRunRepository extends CrudRepository<ReRun, Integer> {
}
