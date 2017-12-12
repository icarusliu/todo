package com.liuqi.learn.model;/**
 * Created by icaru on 2017/8/24.
 */

import javax.persistence.*;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/24 9:50
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/24 9:50
 **/
@Entity
@Table(name = "n_note")
public class Note implements Comparable<Note> {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private Boolean directory;

    private Integer parent;

    @OneToMany(mappedBy = "parent")
    private List<Note> childrens;

    @Column
    @Lob
    private String content;

    @Column(name = "create_time")
    private String createTime;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "note_id")
    private List<NoteFile> fileList;

    public Boolean getDirectory() {
        return directory;
    }

    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public List<Note> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Note> childrens) {
        this.childrens = childrens;
    }

    public List<NoteFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<NoteFile> fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Note o) {
        if (null == o) {
            return -1;
        } else {
            Boolean isDirectory1 = this.getDirectory();
            Boolean isDirectory2 = o.getDirectory();
            if (isDirectory1.equals(isDirectory2)) {
                return this.getName().compareTo(o.getName());
            } else {
                if (isDirectory1) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }
}
