package com.liuqi.learn.controllers;/**
 * Created by icaru on 2017/8/31.
 */

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.liuqi.learn.common.CommonConfig;
import com.liuqi.learn.exceptions.ExceptionCodes;
import com.liuqi.learn.exceptions.TodoException;
import org.hibernate.id.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

/**
 * <p>
 * </p>
 *
 * @Author icaru
 * @Date 2017/8/31 8:48
 * @Version V1.0
 * --------------Modify Logs------------------
 * @Version V1.*
 * @Comments <p></p>
 * @Author icaru
 * @Date 2017/8/31 8:48
 **/
@RequestMapping("/comm")
@RestController
public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private CommonConfig config;

    /**
     * 下载文件
     * @param fileName
     */
    @RequestMapping("/downloadFile")
    public ResponseEntity downloadFile(@RequestParam String fileName) throws TodoException {
        File file = new File(config.getFilePath() + fileName);
        InputStream inputStreamReader = null;

        HttpHeaders headers = new HttpHeaders();
        try {
            inputStreamReader = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new TodoException(ExceptionCodes.FILE_NOT_EXISTS, "文件不存在, 文件："
                    + config.getFilePath() + fileName);
        }
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("charset", "utf-8");
        //设置下载文件名
        headers.add("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

        Resource resource = new InputStreamResource(inputStreamReader);

        return ResponseEntity.ok()
                .headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);
    }

    /**
     * 文件上传接口
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile")
    public String uploadFile(@RequestParam MultipartFile file, @RequestParam String prefix) {
        if (file.isEmpty()) {
            return "";
        }

        //文件名使用UUID来进行存储
        UUID uuid = UUID.randomUUID();

        String fileName = config.getFilePath() + uuid.toString() + "." + prefix;

        try {
            //如果目录不存在，则先创建目录
            File dir = new File(config.getFilePath());
            if (!dir.exists()) {
                dir.mkdir();
            }

            //创建文件
            File savedFile = new File(fileName);
            if (!savedFile.exists()) {
                savedFile.createNewFile();
            }

            file.transferTo(savedFile);
        } catch (FileNotFoundException e) {
            logger.error("File not found!", e);
            return "";
        } catch (IOException e) {
            logger.error("Create file failed!", e);
            return "";
        }

        return uuid.toString() + "." + prefix;
    }
}
