package com.example.springboot.controller;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Parkinginfo;
import com.example.springboot.entity.Parkinginfo2;
import com.example.springboot.entity.Share;
import com.example.springboot.entity.Shareparking;
import com.example.springboot.mapper.ShareMapper;
import com.example.springboot.mapper.ShareMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/share")
public class ShareController {

    @Resource
    ShareMapper shareMapper;

    @PostMapping
    public Result<?> save(@RequestBody Share share){
        shareMapper.insert(share);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Share share){
        shareMapper.updateById(share);
        return Result.success();
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        shareMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Share> wrapper = Wrappers.<Share>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Share::getSid,search);
        }

        Page<Share> sharePage = shareMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(sharePage);
    }
}
