package com.hrc.takeOut.core.commom;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
/**
 * 将处理的数据用统一的格式返回给前端*/
@Data
public class Result<T> {
    private Integer code;//1为成功 ,0为失败
    private String msg;//错误信息
    private T data;//数据
    private Map map = new HashMap();//动态数据
    public static <T> Result<T> success(T object) {
            Result<T> result = new Result<>();
            result.data = object;
            result.code = 1;
            return result;
    }
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }
    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
