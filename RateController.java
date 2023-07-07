package com.example.springboot.controller;

import com.example.springboot.commen.Result;
import com.example.springboot.entity.Rate;
import com.example.springboot.mapper.RateMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rate")
public class RateController {

    @Resource
    RateMapper rateMapper;

    @GetMapping
    public Result<?> getAllRateData() {
        List<Rate> rateList = rateMapper.selectList(null);
        return Result.success(rateList);
    }

    @PostMapping
    public Result<?> saveRate(@RequestBody Rate rate) {
        rate.setNeighbourhood(rate.getNeighbourhood());
        rate.setSid(rate.getSid());
        rate.setRate(rate.getRate());
        // 保存到数据库
        rateMapper.insert(rate);
        return Result.success("评分保存成功");
    }



}

