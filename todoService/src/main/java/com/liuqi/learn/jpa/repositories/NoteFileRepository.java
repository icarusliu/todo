package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/8/31.
 */

import com.liuqi.learn.model.NoteFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/31 8:43
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/31 8:43
 **/
@Repository
public interface NoteFileRepository extends CrudRepository<NoteFile, Integer> {
}
