package com.example.springboot.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Info2;
import com.example.springboot.mapper.Info2Mapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info2")
public class Info2Controller {
    //本来是在Service类中调用，再调用Service类的
    @Resource
    Info2Mapper info2Mapper;

    @PostMapping
    public Result<?> save(@RequestBody Info2 info2){
        info2Mapper.insert(info2);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Info2 info2){
        info2Mapper.updateById(info2);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Info2> wrapper = Wrappers.<Info2>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if (NumberUtil.isNumber(search)) {
                // 将其中一个条件作为主要搜索条件
                wrapper.eq(Info2::getSid, search);
                // 将另一个条件作为附加搜索条件
                wrapper.like(Info2::getTel, "%" + search + "%");
            } else {
                wrapper.like(Info2::getNeighbourhood, search);
            }
        }

        Page<Info2> infoPage = info2Mapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(infoPage);
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        info2Mapper.deleteById(id);
        return Result.success();
    }


    //    根据id查询
    @GetMapping ("/{id}")
    public Result<?> getById(@PathVariable Long id){
        Info2 info2 = info2Mapper.selectById(id);
        return Result.success(info2);
    }
}
