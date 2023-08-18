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
        log.info("common/upload:{}", file.toString());
        //获得文件原始名
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        assert originalFilename != null;
        String postfix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        //使用UUID生成文件名，防止文件名重复
        String prefix = UUID.randomUUID().toString();
        String filename = prefix + postfix;
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
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
        //文件输出地点
        ServletOutputStream outputStream = response.getOutputStream();
        //文件输入地点
        FileInputStream fileInputStream = new FileInputStream(basePath + name);
        int len;
        byte[] buff = new byte[1024];
        while ((len = fileInputStream.read(new byte[1024])) != -1) {
            outputStream.write(buff, 0, len);
            outputStream.flush();
        }
        response.setContentType("image/jpeg");
        log.info("图片下载完成");
        fileInputStream.close();
        outputStream.close();
    }
}
