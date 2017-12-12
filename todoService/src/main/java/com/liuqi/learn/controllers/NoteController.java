package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/8/24.
 */

import com.liuqi.learn.common.CommonConfig;
import com.liuqi.learn.jpa.repositories.NoteFileRepository;
import com.liuqi.learn.jpa.repositories.NoteRepository;
import com.liuqi.learn.model.Note;
import com.liuqi.learn.model.NoteFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/24 10:34
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/24 10:34
 **/
@RestController
@RequestMapping("/note")
public class NoteController {
    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private static final String NOTE_FILE_PATH = "d:/workspace/note/";

    @Autowired
    private NoteRepository rp;

    @Autowired
    private NoteFileRepository nfp;

    @Autowired
    private CommonConfig config;

    /**
     * 获取保存的文件名称
     * 文件名称中包含路径
     *
     * @param id
     * @return
     */
    private static final String getFileName(Integer id) {
        return NOTE_FILE_PATH + id + ".txt";
    }

    @RequestMapping("/deleteNoteFile")
    public void deleteNoteFile(@RequestBody NoteFile noteFile) {
        //参数检测
        if (null == noteFile) {
            return;
        }

        //删除服务器文件
        File file = new File(config.getFilePath() + noteFile.getSavedFileName());
        if (file.exists()) {
            file.delete();
        }

        //删除服务器信息
        nfp.delete(noteFile);
    }

    @RequestMapping("/saveNoteFile")
    public NoteFile saveNoteFile(@RequestBody NoteFile noteFile) {
        return nfp.save(noteFile);
    }

    @RequestMapping("/save")
    public Note save(@RequestBody Note note) {
        String content = note.getContent();

        //需要设置内容为空，内容保存到文件中去，而不是保存到Db中，DB字段无法保留很长串的文件
        note.setContent("");
        Note rNote = rp.save(note);

        //返回的对象需要设置内容
        rNote.setContent(content);

        //根据返回的对象的ID来保存内容到文件中去
        saveFile(getFileName(rNote.getId()), content);

        return rNote;
    }

    @RequestMapping("/list")
    public Iterable<Note> list() {
        Iterable<Note> list = rp.findAll();
        list.forEach(note -> note.setContent(readFromFile(getFileName(note.getId()))));

        return list;
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody Note note) {
        if (null != note) {
            internalDelete(note);
        }
    }

    @RequestMapping("/find/{id}")
    public Note getById(@PathVariable Integer id) {
        Note note = this.rp.findOne(id);
        note.setContent(readFromFile(getFileName(id)));

        return note;
    }

    /**
     * 删除节点及其下所有的子节点
     * @param note
     */
    private final void internalDelete(Note note) {
        if (null != note.getChildrens()) {
            note.getChildrens().forEach(e -> internalDelete(e));
            note.getChildrens().clear();
        }

        rp.delete(note);
    }

    /**
     * 将内容写入文件
     */
    private final static void saveFile(String fileName, String content) {
        //先创建目录
        String dirPath = NOTE_FILE_PATH;
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File file = new File(fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("Create file failed!", e);
                return;
            }
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            logger.error("Get buffered writer failed!", e);
            return;
        }

        try {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            logger.error("Write content to file failed!", e);
            return;
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                logger.error("Close writer failed!", e);
            }
        }
    }

    /**
     * 从文件中获取内容
     *
     * @param fileName
     * @return 返回文件内容 如果文件不存在，则返回空字符串
     */
    private final static String readFromFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            logger.error("Get file reader failed!", e);

            return "";
        }
        String line = "";
        StringBuffer result = new StringBuffer();
        try {
            while (null != (line = reader.readLine())) {
                result.append(line);
            }
        } catch (IOException e) {
            logger.error("Read from file failed!", e);
            return "";
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error("Close file reader failed!", e);
            }
        }

        return result.toString();
    }
}
