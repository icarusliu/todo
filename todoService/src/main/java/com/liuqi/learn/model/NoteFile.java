package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/31.
 */

import javax.persistence.*;

/**
 * <p>
 *     文档附件
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/31 8:32
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/31 8:32
 **/
@Table(name = "n_note_file")
@Entity
public class NoteFile {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "saved_file_name")
    private String savedFileName;

    @Column(name = "note_id")
    private Integer noteId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getSavedFileName() {
        return savedFileName;
    }

    public void setSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
    }
}
