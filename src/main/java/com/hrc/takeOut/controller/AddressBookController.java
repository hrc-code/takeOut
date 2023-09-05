package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.entity.AddressBook;
import com.hrc.takeOut.service.AddressBookService;
import com.hrc.takeOut.utils.ThreadLocals;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/** 地址簿管理*/
@Slf4j
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Resource
    private AddressBookService addressBookService;
    /** 新增用户收货地址*/
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(ThreadLocals.getCurrentId());
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }
    /** 设置用户默认收货地址
     * 若用户没有默认地址则直接存入，否则需要修改数据库中默认地址*/
    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        Result<AddressBook> aDefault = getDefault();
        String msg = aDefault.getMsg();
        AddressBook data = aDefault.getData();
        if (msg == null) {
             data.setIsDefault("0");
             // update address_book set * = ? where id = ?
            addressBookService.updateById(data);
        }
        Long addressBookId = addressBook.getId();
        //select * from address_book where id = ?
        AddressBook addressBook1 = addressBookService.getById(addressBookId);
        addressBook1.setIsDefault("1");
        //update address_book set * = ?
        addressBookService.updateById(addressBook1);
        return Result.success(addressBook1);
    }
    /** 修改用户收货地址*/
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook) {
        // update address_book set * = ? where id = ?
        addressBookService.updateById(addressBook);
        return Result.success("修改地址成功");
    }
    /** 获取用户默认的收货地址*/
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        // select * from  table  where user_id = ? and is_default = 1;
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, ThreadLocals.getCurrentId());
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("该用户未设置默认地址");
        }
    }
    /** 查看用户全部的收货地址*/
    @GetMapping("/list")
    public Result<List<AddressBook>>  list() {
        // select * from address_book where user_id = ?
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, ThreadLocals.getCurrentId());
        List<AddressBook> addressBookList = addressBookService.list(addressBookLambdaQueryWrapper);
        return Result.success(addressBookList);
    }
    /** 根据id查询用户收货地址
     * 用于修改用户收货地址回显相关信息*/
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        //select * from address_book where id = ?
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            return Result.error("没有这个收货地址");
        } else {
            return Result.success(addressBook);
        }
    }
}
