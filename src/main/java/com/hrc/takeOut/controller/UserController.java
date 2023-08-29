package com.hrc.takeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hrc.takeOut.commom.Result;
import com.hrc.takeOut.entity.User;
import com.hrc.takeOut.service.UserService;
import com.hrc.takeOut.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    /* 发送手机短信验证**/
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号码
        String phone = user.getPhone();
        //判断前端传来的值是否为空
        if (StringUtils.isNotEmpty(phone)) {
            //生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("验证码：{}",code);
            //确保用户已经获得验证码，便于登录验证
            session.setAttribute(phone, code);
            return Result.success("手机验证码短信发送成功");
        }
        return  Result.error("手机号码不能为空");
    }
    /** 移动端用户登录
     * 参数：phone号   code验证码*/
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String,String> map, HttpSession session) {
        log.info("开始调用移动端用户登录接口");
        //获取手机号
        String phone = map.get("phone");
        //获取验证码
        String code = map.get("code");
        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        if (codeInSession != null && codeInSession.equals(code)) {
            //判断是否为新用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            log.info("开始查询---user表");
            User user = userService.getOne(userLambdaQueryWrapper);
            log.info("结束查询---user表");
            //新用户注册
            if (Objects.isNull(user)) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                log.info("开始注册新用户---user表");
                userService.save(user);
                log.info("结束注册新用户---user表");
            }
            session.setAttribute("user",user.getId());
            return Result.success(user);
        }
        return Result.error("登录失败");
    }
}
