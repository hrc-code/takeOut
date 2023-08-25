package com.hrc.takeOut.utils;

/**
 *  封装ThreadLocal类
 *  */
public class ThreadLocals {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal();
    public static Long getCurrentId() {
        return  threadLocal.get();
    }
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
}
