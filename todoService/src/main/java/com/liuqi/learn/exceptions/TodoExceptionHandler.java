package com.liuqi.learn.exceptions;/**
 * Created by icaru on 2017/7/4.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/4 16:26
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/4 16:26
 **/
@ControllerAdvice
public class TodoExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(TodoExceptionHandler.class);

    @ExceptionHandler(TodoException.class)
    @ResponseBody
    public Map<String, String> handleUserException(TodoException e) {
        logger.error("Catch todoException:  ", e);

        logger.info("Begin to process userException!");

        Map<String, String> map = new HashMap<String, String>();
        map.put("errorCode", e.getCode());
        map.put("status", "0");
        map.put("errorMessage", e.getMessage());

        return map;
    }

    @ExceptionHandler
    @ResponseBody
    public Map<String, String> defaultExceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("Catch other exception!", e);

        logger.info("Begin to process other exception!");

        Map<String, String> map = new HashMap<String, String>();
        map.put("errorCode", "");
        map.put("status", "0");
        map.put("errorMessage", e.getMessage());

        return map;
    }
}
