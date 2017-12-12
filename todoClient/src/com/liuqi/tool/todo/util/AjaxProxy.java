package com.liuqi.tool.todo.util;/**
 * Created by icaru on 2017/7/20.
 */

import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.*;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/20 11:32
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/20 11:32
 **/
public class AjaxProxy {
    private static Logger logger = LoggerFactory.getLogger(AjaxProxy.class);

    /**
     * 保存待办事项
     *
     * @param reRun
     * @return
     * @throws TodoException
     */
    public static ReRun saveReRun(ReRun reRun) throws TodoException {
        return Ajax.postForObject(ConfigProxy.INSTANCE.getUrl() + "rerun/save", reRun, ReRun.class);
    }

    /**
     * 获取所有重跑日志
     *
     * @return
     * @throws TodoException
     */
    public static List<ReRun> getAllReRuns() throws TodoException {
        List<ReRun> list;
        list = call("getAllReRuns",
                () -> Ajax.post(ConfigProxy.INSTANCE.getUrl() + "rerun/list", null, ReRun.class));

        if (null == list) {
            list = new ArrayList<>();
        }

        return list;
    }

    /**
     * 删除文档附件
     *
     * @param noteFile
     * @throws TodoException
     */
    public static void deleteNoteFile(NoteFile noteFile) throws TodoException {
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "note/deleteNoteFile", noteFile);
    }

    /**
     * 保存文档附件
     *
     * @param noteFile
     * @return
     * @throws TodoException
     */
    public static NoteFile saveNoteFile(NoteFile noteFile) throws TodoException {
        return Ajax.postForObject(ConfigProxy.INSTANCE.getUrl() + "note/saveNoteFile", noteFile, NoteFile.class);
    }

    /**
     * 下载文件并保存到磁盘文件中
     * @param fileName 下载的文件，包含服务器端文件名称
     * @param savedFileName 保存的文件路径及名称
     * @throws TodoException 下载失败时抛出异常
     */
    public static void downloadFile(String fileName, String savedFileName) throws TodoException {
        Ajax.downloadFile(fileName, savedFileName);
    }

    /**
     * 上传 文件
     * @param fileName
     * @return 返回上传的文件路径及名称
     * @throws TodoException 文件不存在或者服务器连接处理异常时抛出异常
     */
    public static String uploadFile(String fileName) throws TodoException {
        return Ajax.uploadFile(fileName);
    }

    /**
     * 获取所有请假事项清单
     * @return 返回数据不会为空
     * @throws TodoException 当调用远程接口失败时抛出异常
     */
    public static List<WorkOff> findAllWorkOffs() throws TodoException {
        List<WorkOff> list;
        list = call("listWorkOffs",
                () -> Ajax.post(ConfigProxy.INSTANCE.getUrl() + "user/workOff/list", null, WorkOff.class));

        if (null == list) {
            list = new ArrayList<>();
        }

        return list;
    }

    /**
     * 返回所有笔记
     * @return
     * @throws TodoException
     */
    public static List<Note> findAllNotes() throws TodoException {
        List<Note> result = call("findAllNotes",
                () -> Ajax.post(ConfigProxy.INSTANCE.getUrl() + "note/list", null, Note.class));

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * 删除笔记及其下级节点
     * @param note
     * @throws TodoException
     */
    public static void deleteNote(Note note) throws TodoException {
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "note/delete", note);
    }

    /**
     * 根据名称获取对象
     *
     * @param id
     * @return
     * @throws TodoException
     */
    public static Note findNoteById(String id) throws TodoException {
        return call("findNoteById" + "-" + id,
                () -> Ajax.postForObject(ConfigProxy.INSTANCE.getUrl() + "note/find/" + id, null, Note.class));
    }

    /**
     * 保存记录
     * @param note
     * @throws TodoException
     */
    public static Note saveNote(Note note) throws TodoException {
        if (null == note) {
            return null;
        }

        Note rNote = Ajax.postForObject(ConfigProxy.INSTANCE.getUrl() + "note/save", note, Note.class);
        return rNote;
    }

    /**
     * 保存请假信息
     * @param workOff
     * @throws TodoException
     */
    public static void saveWorkOff(WorkOff workOff) throws TodoException {
        if (null == workOff) {
            return;
        }
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "user/workOff/save", workOff);
    }

    /**
     * 获取所有环境信息
     *
     * @return
     * @throws TodoException
     */
    public static List<Environment> listAllEnvs() throws TodoException {
        List<Environment> result = call("listAllEnvs",
                () -> Ajax.post(ConfigProxy.INSTANCE.getUrl() + "env/list", null, Environment.class));

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * 保存环境信息
     * @param env
     * @throws TodoException
     */
    public static void saveEnv(Environment env) throws TodoException {
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "env/save", env);
    }

    /**
     * 查找所有需求
     * @return
     * @throws TodoException
     */
    public static List<Requirement> listAllRequirements() throws TodoException {
        List<Requirement> result = call("listAllRequirements",
                () -> Ajax.post(ConfigProxy.INSTANCE.getUrl() + "requirement/list/" + Cache.getLoginUser().getName()
                        , null, Requirement.class));

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * 保存用户
     * @param user
     * @throws TodoException
     */
    public static void saveUser(User user) throws TodoException {
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "user/saveUser",  user);
    }

    /**
     * 保存需求
     * @param requirement
     * @throws TodoException
     */
    public static void saveRequirement(Requirement requirement) throws TodoException {
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "requirement/save", requirement);
    }

    /**
     * 获取所有待办事项
     *
     * @return
     * @throws TodoException
     */
    public static List<Unrequirement> list() throws TodoException {
        List<Unrequirement> result = call("list",
                () ->  Ajax.post(ConfigProxy.INSTANCE.getUrl() + "unrequirement/list/" + Cache.getLoginUser().getName(),
                        null, Unrequirement.class));

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * 查找当天的待办事项
     *
     * @param date
     * @return
     * @throws TodoException
     */
    public static List<Unrequirement> listForPlanDate(Date date) throws TodoException {
        String dateStr = CalendarUtil.formatDateStr(date);
        List<Unrequirement> result = call("listForPlanDate-" + dateStr,
                () ->   Ajax.post(ConfigProxy.INSTANCE.getUrl() + "unrequirement/listForPlanDate/" + Cache.getLoginUser().getName()
                        + "/"
                        + dateStr, null, Unrequirement.class));

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * 保存待办事项对象
     *
     * @param u
     * @throws TodoException
     */
    public static void save(Unrequirement u) throws TodoException {
        Ajax.post(ConfigProxy.INSTANCE.getUrl() + "unrequirement/save", u);
    }

    /**
     * 获取所有用户列表
     *
     * @return
     * @throws TodoException
     */
    public static List<User> listAllUsers() throws TodoException {
        List<User> result = call("listAllUsers",
                () ->  Ajax.post(ConfigProxy.INSTANCE.getUrl() + "user/list", null, User.class));

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    /**
     * 获取所有提过需求的业务人员清单
     *
     * @return  如果获取失败，则返回空的List；  不会为NULL
     */
    public static List<String> listAllBusiUsers() {
        List<String> result = null;
        try {
            result = call("listAllBusiUsers",
                    () ->  Ajax.post(ConfigProxy.INSTANCE.getUrl() + "requirement/listBusiUsers", null, String.class));
        } catch (TodoException e) {
            logger.error("Get all busi users failed!", e);
        }

        if (null == result) {
            return new ArrayList<>();
        }

        return result;
    }

    private static <R> R call(String cacheKey, AjaxCallback<R> callback) throws TodoException {
        try {
            R r = callback.call();

            if (null != r) {
                AjaxCache.INSTANCE.put(cacheKey, r);
            }

            return r;
        } catch (TodoException ex) {
            if (ex.getCode().equals(ExceptionCodes.CONN_FAILED)) {
                //连接失败时，先从缓存中获取缓存的数据；
                Object obj = AjaxCache.INSTANCE.get(cacheKey);
                if (null == obj) {
                    return null;
                } else {
                    return (R)obj;
                }
            }

            throw ex;
        }
    }

    private static interface AjaxCallback<R> {
        public R call() throws TodoException;
    }
}
