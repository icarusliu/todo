package com.liuqi.learn.jpa.repositories;/**
 * Created by icaru on 2017/8/24.
 */

import com.liuqi.learn.model.Note;
import org.springframework.data.repository.CrudRepository;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/24 10:27
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/24 10:27
 **/
public interface NoteRepository extends CrudRepository<Note, Integer>{
    /**
     * 根据名称获取对象
     * @param name
     * @return
     */
    public Note findByName(String name);
}
