package com.liuqi.tool.todo.util;/**
 * Created by icaru on 2017/7/19.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import com.liuqi.learn.model.Unrequirement;
import com.liuqi.learn.model.UnrequirementType;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * <p>
 *     AJAX方法类，提供GET等方法调用URL并处理JSON格式返回的数据；
 * </p>
 *
 * @Author icaru
 * @Date 2017/7/19 10:08
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/7/19 10:08
 **/
public class Ajax {
    private static Logger logger = LoggerFactory.getLogger(Ajax.class);

    /**
     * 调用POST处理输入输出请求
     *
     * @param urStr
     * @param t
     * @param clazz
     * @param <T>
     * @param <R>
     * @return
     * @throws TodoException
     */
    public static <T, R> List<R> post(String urStr, T t, Class<R> clazz) throws TodoException {
        String respStr = internalPost(urStr, t);
        if ("".equals(respStr)) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);

            return mapper.readValue(respStr, javaType);
        } catch (IOException e) {
            logger.error("Read resp object failed!", e);

            throw new TodoException(ExceptionCodes.CONN_FAILED, "获取返回对象失败！");
        }
    }

    public static <T, R> R postForObject(String urlStr, T t, Class<R> clazz) throws TodoException {
        String respStr = internalPost(urlStr, t);
        if ("".equals(respStr)) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            JavaType javaType = mapper.getTypeFactory().constructType(clazz);

            return mapper.readValue(respStr, javaType);
        } catch (IOException e) {
            logger.error("Read resp object failed!", e);

            throw new TodoException(ExceptionCodes.CONN_FAILED, "获取返回对象失败！");
        }
    }

    /**
     * 向服务器发送无返回的请求
     *
     * @param urlStr
     * @param t
     * @param <T>
     * @throws TodoException
     */
    public static <T> void post(String urlStr, T t) throws TodoException {
        internalPost(urlStr, t);
    }

    /**
     * 下载文件
     *
     * @param fileName 下载的文件名称
     * @param savedFileName 保存的文件路径及名称
     */
    public static void downloadFile(String fileName, String savedFileName) throws TodoException {
        //组装文件名参数
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("fileName", new StringBody(fileName, ContentType.DEFAULT_TEXT))
                .build();

        //获取返回对象
        HttpEntity respEntity = connect(ConfigProxy.INSTANCE.getUrl() + "comm/downloadFile/", entity);

        //将返回对象内容写出到文件中
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(savedFileName);
            respEntity.writeTo(outputStream);

            outputStream.flush();
        } catch (IOException e) {
            logger.error("Read response data failed!", e);
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("Close output stream when writing data to file failed!", e);
                }
            }
        }
    }

    /**
     * 上传文件
     * @param fileName
     * @return 返回服务器保存的文件路径及名称
     * @throw 文件不存在或者服务器连接异常时时抛出异常
     */
    public static String uploadFile(String fileName) throws TodoException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new TodoException(ExceptionCodes.FILE_NOT_EXISTS, "文件不存在！");
        }

        String prefix = fileName.substring(fileName.lastIndexOf("."));

        FileBody fileBody = new FileBody(file, ContentType.create("text/plain", Consts.UTF_8));
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .addPart("prefix", new StringBody(prefix, ContentType.DEFAULT_TEXT))
                .setCharset(Consts.UTF_8)
                .build();

        return getResponseJsonData(ConfigProxy.INSTANCE.getUrl() + "comm/uploadFile", entity);
    }

    /**
     * 通过POST调用URL，返回 JSON格式字符串
     *
     * @param urlStr
     * @param t
     * @param <T>
     */
    private static <T> String internalPost(String urlStr, T t) throws TodoException {
        String sendStr = getSendStr(t);

        logger.debug("Send post request, url: " + urlStr + ", send json: " + sendStr);

        StringEntity sendEntity = new StringEntity(sendStr, "UTF-8");
        sendEntity.setContentType("application/json");

        return getResponseJsonData(urlStr, sendEntity);
    }

    /**
     * 连接服务器并返回httpEntity
     * 返回的httpEntity可能为空
     * @param url
     * @param entity
     * @return
     * @throws TodoException
     */
    private static HttpEntity connect(String url, HttpEntity entity) throws TodoException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        post.setEntity(entity);

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(post);
        } catch (IOException e) {
            logger.error("Get response from server failed!", e);

            try {
                httpClient.close();
            } catch (IOException e1) {
                logger.error("Close connection failed!", e);
            }

            throw new TodoException(ExceptionCodes.CONN_FAILED, "连接服务器失败！");
        }

        if (null != response) {
            HttpEntity responseObject = response.getEntity();
            if (200 != response.getStatusLine().getStatusCode()) {
                logger.error("Get response failed, errorCode: " + response.getStatusLine().getStatusCode());

                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("Close connection failed!", e);
                }

                try {
                    httpClient.close();
                } catch (IOException e1) {
                    logger.error("Close connection failed!", e1);
                }

                throw new TodoException(ExceptionCodes.CONN_FAILED, "请求失败！");
            }

            return responseObject;
        } else {
            return null;
        }
    }

    /**
     * 获取返回信息
     * @param url
     * @param entity
     * @return
     * @throws TodoException
     */
    private static String getResponseJsonData(String url, HttpEntity entity) throws TodoException {
        HttpEntity respEntity = connect(url, entity);
        String result = "";

        try {
            result = EntityUtils.toString(respEntity);
        } catch (IOException e) {
            logger.error("Convert response object to String failed!", e);

            throw new TodoException(ExceptionCodes.CONN_FAILED, "获取返回对象失败！");
        }

        if (!"".equals(result.toString())) {
            //判断结果是否成功 status = 0时表示失败，此时错误信息存储在errorMessage中
            String resultStr = result.toString();
            JSONTokener tokener = new JSONTokener(resultStr);
            Object obj = null;
            try {
                obj = tokener.nextValue();
            } catch (JSONException e) {
                logger.error("Parse response json object failed!", e);
            }
            if (null != obj && obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                try {
                    String status = jsonObject.getString("status");
                    if ("0".equals(status)) {
                        String errorMessage = jsonObject.getString("errorMessage");
                        String errorCode = jsonObject.getString("errorCode");
                        throw new TodoException(errorCode, null == errorMessage ? "" : errorMessage);
                    }
                } catch (JSONException e) {
                    logger.error("Get status and errorMessage values failed!", e);
                }
            }
        }

        logger.debug("Get response json: " + result.toString());

        return result.toString();
    }

    private static <T> String getSendStr(T t) throws TodoException {
        if (null == t) {
            return "";
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.error("Convert object to json failed!", e);

            throw new TodoException(ExceptionCodes.CONN_FAILED, "对象转JSON字符串失败！");
        }

        return json;
    }

    public static void main(String[] args) throws Exception {
//        downloadFile("1.doc", "d:/1.doc");
        String str = "d:/未解.docx";
        String result = new String(str.getBytes("UTF-8"), "UTF-8");
        System.out.println(result);
        uploadFile(result);
    }

}
