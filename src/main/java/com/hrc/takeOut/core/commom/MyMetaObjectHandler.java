package com.hrc.takeOut.core.commom;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hrc.takeOut.core.utils.ThreadLocals;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 * 处理公共字段*/
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始公共字段自动填充 insert");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", ThreadLocals.getCurrentId());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser", ThreadLocals.getCurrentId());
        log.info("结束公共字段自动填充：insert");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始公共字段自动填充 update");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser", ThreadLocals.getCurrentId());
        log.info("借宿公共字段自动填充：update");
    }
}
