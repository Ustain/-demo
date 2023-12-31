package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;

import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
@RestController
@RequestMapping("/user")
public class UserController {
    //本来是在Service类中调用，再调用Service类的
    @Resource
    UserMapper userMapper;

    @PostMapping
    public Result<?> save(@RequestBody User user){
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(User::getUsername,search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        userMapper.deleteById(id);
        return Result.success();
    }


    //    登录
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){//这只是传进来的user，并没有在数据库中进行查找
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if (res==null){
            return Result.error("-1","用户名或密码错误");
        }
        return Result.success(res);
    }

    // 注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        if (user.getNickName() == null || user.getNickName().isEmpty()) {
            user.setNickName("用户");
        }
        userMapper.insert(user);
        return Result.success();
    }
    // 密码找回
    @PostMapping("/forgetpwd")
    public Result<?> forgetpwd(@RequestBody User user) {
        User existingUser = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (existingUser == null) {
            return Result.error("-1", "用户不存在");
        }

        // 返回用户的密码
        return Result.success(existingUser.getPassword());
    }


    //    根据id查询
    @GetMapping ("/{id}")
    public Result<?> getById(@PathVariable Long id){
        User user = userMapper.selectById(id);
        return Result.success(user);
    }
}
