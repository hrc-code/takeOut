package com.hrc.takeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrc.takeOut.entity.Category;

public interface CategoryService extends IService<Category> {
      void  remove(Long id);
}
