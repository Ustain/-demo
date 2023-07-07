package com.example.springboot.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Order2;
import com.example.springboot.mapper.Order2Mapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order2")
public class Order2Controller {

    @Resource
    Order2Mapper order2Mapper;

    @PostMapping
    public Result<?> save(@RequestBody Order2 order2){
        order2Mapper.insert(order2);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Order2 order2){
        order2Mapper.updateById(order2);
        return Result.success();
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        order2Mapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Order2> wrapper = Wrappers.<Order2>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Order2::getSid,search);
        }

        Page<Order2> order2Page = order2Mapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(order2Page);
    }
}
