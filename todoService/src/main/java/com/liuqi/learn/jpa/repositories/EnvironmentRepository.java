package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/8/18.
 */

import com.liuqi.learn.model.Environment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
@Repository
public interface EnvironmentRepository extends CrudRepository<Environment, Integer>{

}
