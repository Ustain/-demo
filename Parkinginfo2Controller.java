package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Parkinginfo2;
import com.example.springboot.mapper.Parkinginfo2Mapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkinginfo2")
public class Parkinginfo2Controller {
    //本来是在Service类中调用，再调用Service类的
    @Resource
    Parkinginfo2Mapper parkinginfo2Mapper;

    @PostMapping
    public Result<?> save(@RequestBody Parkinginfo2 parkinginfo2){
        parkinginfo2Mapper.insert(parkinginfo2);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Parkinginfo2 parkinginfo2){
        parkinginfo2Mapper.updateById(parkinginfo2);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Parkinginfo2> wrapper = Wrappers.<Parkinginfo2>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Parkinginfo2::getSid,search);
        }

        Page<Parkinginfo2> parkinginfoPage = parkinginfo2Mapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(parkinginfoPage);
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        parkinginfo2Mapper.deleteById(id);
        return Result.success();
    }


    //    根据id查询
    @GetMapping ("/{id}")
    public Result<?> getById(@PathVariable Long id){
        Parkinginfo2 parkinginfo = parkinginfo2Mapper.selectById(id);
        return Result.success(parkinginfo);
    }
}
