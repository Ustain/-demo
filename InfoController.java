package com.example.springboot.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Info;
import com.example.springboot.mapper.InfoMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info")
public class InfoController {
    //本来是在Service类中调用，再调用Service类的
    @Resource
    InfoMapper infoMapper;

    @PostMapping
    public Result<?> save(@RequestBody Info info){
        infoMapper.insert(info);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Info info){
        infoMapper.updateById(info);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") Integer minPrice,
                              @RequestParam(defaultValue = "0") Integer maxPrice,
                              @RequestParam(defaultValue = "0") Integer minScore,
                              @RequestParam(defaultValue = "0") Integer maxScore,
                              @RequestParam(defaultValue = "0") Integer minStartTime,
                              @RequestParam(defaultValue = "0") Integer maxEndTime,
                              @RequestParam(defaultValue = "") String timeRange) {
        LambdaQueryWrapper<Info> wrapper = Wrappers.<Info>lambdaQuery();

        if (StrUtil.isNotBlank(search)) {
            if (NumberUtil.isNumber(search)) {
                // 将其中一个条件作为主要搜索条件
                wrapper.eq(Info::getSid, search);
                // 将另一个条件作为附加搜索条件
                wrapper.like(Info::getTel, "%" + search + "%");
            } else {
                wrapper.like(Info::getNeighbourhood, search);
            }
        }

        // 添加价格范围条件
        if (minPrice != 0 && maxPrice != 0) {
            wrapper.between(Info::getPrice, minPrice, maxPrice);
        } else if (minPrice != 0) {
            wrapper.ge(Info::getPrice, minPrice);
        } else if (maxPrice != 0) {
            wrapper.le(Info::getPrice, maxPrice);
        }

        // 添加评分范围条件
        if (minScore != 0 && maxScore != 0) {
            wrapper.between(Info::getRate, minScore, maxScore);
        } else if (minScore != 0) {
            wrapper.ge(Info::getRate, minScore);
        } else if (maxScore != 0) {
            wrapper.le(Info::getRate, maxScore);
        }

        // 添加时间段范围条件
        if ("0-8".equals(timeRange)) {
            wrapper.between(Info::getAstartTime, 0, 8);
        } else if ("8-16".equals(timeRange)) {
            wrapper.between(Info::getAstartTime, 8, 16);
        } else if ("16-24".equals(timeRange)) {
            wrapper.between(Info::getAstartTime, 16, 24);
        }

        Page<Info> infoPage = infoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(infoPage);
    }




    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        infoMapper.deleteById(id);
        return Result.success();
    }


    //    根据id查询
    @GetMapping ("/{id}")
    public Result<?> getById(@PathVariable Long id){
        Info info = infoMapper.selectById(id);
        return Result.success(info);
    }
}
