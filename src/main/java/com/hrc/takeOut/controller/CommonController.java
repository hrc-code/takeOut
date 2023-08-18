package com.hrc.takeOut.controller;

import com.hrc.takeOut.core.commom.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**文件的上传和下载*/
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${takeOut.basePath}")
    private String basePath;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("common/upload");
        //获得文件原始名
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        assert originalFilename != null;
        String postfix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        //使用UUID生成文件名，防止文件名重复
        String prefix = UUID.randomUUID().toString();
        String filename = prefix + postfix;
        File dir = new File(basePath);
        //服务器存放图片的目录不存在就创建
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("创建服务器存放图片目录成功");
            } else {
                System.out.println("服务器存放图片目录已经创建");
            }
        }

        try {
            //将文件存入服务器中
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        return Result.success(filename);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //输出流
        ServletOutputStream outputStream = response.getOutputStream();
        //输入流
        FileInputStream fileInputStream = new FileInputStream(basePath + name);
        response.setContentType("image/jpeg");
        //读文件与写文件的固定格式
        int len;
        byte[] buff = new byte[1024];
        while ((len = fileInputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
            outputStream.flush();
        }
        log.info("图片下载完成");
        //关闭资源，遵循先开后关原则
        fileInputStream.close();
        outputStream.close();
    }
}
