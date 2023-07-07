package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Nums;
import com.example.springboot.mapper.NumsMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
//@RequestMapping("/nums")
public class NumsController {

    @Resource
    NumsMapper numsMapper;

    @GetMapping("/nums")
    public Result<?> getNumsByNeighbourhoodAndSid(@RequestParam String neighbourhood, @RequestParam Integer sid) {
        QueryWrapper<Nums> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("neighbourhood", neighbourhood).eq("sid", sid);
        Nums nums = numsMapper.selectOne(queryWrapper);
        return Result.success(nums);
    }

    @GetMapping("/nums3")
    public Result<?> getAllNums() {
        List<Nums> numsList = numsMapper.selectList(null);
        return Result.success(numsList);
    }


    @PostMapping("/nums")
    public Result<?> saveNums(@RequestBody Nums nums) {
        System.out.println("Received nums: " + nums.toString());

        numsMapper.insert(nums);
        return Result.success("保存成功");
    }
}


