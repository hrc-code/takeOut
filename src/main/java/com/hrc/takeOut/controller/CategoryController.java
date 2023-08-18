package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrc.takeOut.core.commom.Result;
import com.hrc.takeOut.entity.Category;
import com.hrc.takeOut.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
   private CategoryService categoryService;
        /**新增分类*/
    @PostMapping
    public Result<String> save(@RequestBody Category category, HttpServletRequest request) {
        log.info("post /category  新增分类：{}",category);
        categoryService.save(category);
        return Result.success("新增分类成功");
    }
    /**分页查询*/
    @GetMapping("/page")
    public Result<Page<Category>> page(int page, int pageSize) {
        //构造分页对象
        Page<Category> categoryPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，按照sort排序
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);
        //进行具有条件构造器的分页查询
        categoryService.page(categoryPage,categoryLambdaQueryWrapper);
        return Result.success(categoryPage);
    }

    /**g根据id删除分类*/
    @DeleteMapping
    public Result<String> delete(Long id) {
        categoryService.remove(id);
        return Result.success("删除成功");
    }

    /**根据id更新分类 */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        log.info("完成一个分类的更新");
        return Result.success("分类更新成功");
    }
}
