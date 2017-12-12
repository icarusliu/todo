package com.liuqi.learn.exceptions;/**
 * Created by icaru on 2017/7/4.
 */

/**
 * <p>
 *     用户操作异常类
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/4 8:41
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/4 8:41
 **/
public class TodoException extends Exception {
    private String code;
    private String msg;

    public TodoException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
