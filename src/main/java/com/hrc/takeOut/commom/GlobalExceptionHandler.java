package com.hrc.takeOut.commom;

import com.hrc.takeOut.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
/**全局处理异常*/
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
@Slf4j
public class GlobalExceptionHandler {
        /** 处理sql异常*/
       @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
           log.error(ex.getMessage());
           if (ex.getMessage().contains("Duplicate entry")) {
               String[] split = ex.getMessage().split("\\s");
               String msg = split[2] + "已存在";
               return Result.error( msg);
           }

           return Result.error("sql未知错误");
       }
        /** 处理客户端异常*/
       @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex) {
           log.error(ex.getMessage());
           return Result.error(ex.getMessage());
       }
       /** 处理文件异常*/
       @ExceptionHandler(FileNotFoundException.class)
    public Result<String> exceptionHandler(FileNotFoundException ex) {
                log.error(ex.getMessage());
                return Result.error("图片加载错误");
       }
}
