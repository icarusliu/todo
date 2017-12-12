package com.liuqi.learn.exceptions;/**
 * Created by icaru on 2017/8/17.
 */

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/17 12:49
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/17 12:49
 **/
public class ExceptionCodes {
    /**
     * 用户已存在
     */
    public static final String USER_EXISTS = "user.exists";

    /**
     * 用户不存在
     */
    public static final String USER_NOT_EXIST = "user.notExists";

    /**
     * 待办事项不存在
     */
    public static final String TODO_NOT_EXIST = "todo.notExists";

    /**
     * UI中值为空
     */
    public static final String UI_VALUE_NULL = "ui.value.null";

    public static final String CONN_FAILED = "conn.failed";

    /**
     * 文件不存在
     */
    public static final String FILE_NOT_EXISTS = "comm.file.notExists";

    /**
     * 未定义错误
     */
    public static final String UNDEFINED = "undefined";
}
