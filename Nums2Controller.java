package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Nums2;
import com.example.springboot.mapper.Nums2Mapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/nums2")
public class Nums2Controller {

    @Resource
    Nums2Mapper nums2Mapper;

    @GetMapping
    public Result<?> getNums2ByNeighbourhoodAndSid(@RequestParam String neighbourhood, @RequestParam Integer sid) {
        QueryWrapper<Nums2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("neighbourhood", neighbourhood).eq("sid", sid);
        Nums2 nums2 = nums2Mapper.selectOne(queryWrapper);
        return Result.success(nums2);
    }

    @PostMapping
    public Result<?> saveNums2(@RequestBody Nums2 nums2) {
        System.out.println("Received nums2: " + nums2.toString());

        nums2Mapper.insert(nums2);
        return Result.success("保存成功");
    }
}


