package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Parkinginfo;
import com.example.springboot.mapper.ParkinginfoMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkinginfo")
public class ParkinginfoController {
    //本来是在Service类中调用，再调用Service类的
    @Resource
    ParkinginfoMapper parkinginfoMapper;

    @PostMapping
    public Result<?> save(@RequestBody Parkinginfo parkinginfo){
        parkinginfoMapper.insert(parkinginfo);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Parkinginfo parkinginfo){
        parkinginfoMapper.updateById(parkinginfo);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Parkinginfo> wrapper = Wrappers.<Parkinginfo>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Parkinginfo::getSid,search);
        }

        Page<Parkinginfo> parkinginfoPage = parkinginfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(parkinginfoPage);
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        parkinginfoMapper.deleteById(id);
        return Result.success();
    }


    //    根据id查询
    @GetMapping ("/{id}")
    public Result<?> getById(@PathVariable Long id){
        Parkinginfo parkinginfo = parkinginfoMapper.selectById(id);
        return Result.success(parkinginfo);
    }
}
