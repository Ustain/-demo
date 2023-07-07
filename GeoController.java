package com.example.springboot.controller;

import com.example.springboot.commen.Result;
import com.example.springboot.entity.Geo;
import com.example.springboot.mapper.GeoMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/geo")
public class GeoController {

    @Resource
    GeoMapper geoMapper;

    @GetMapping
    public Result<?> getAllGeoData() {
        List<Geo> geoList = geoMapper.selectList(null);
        return Result.success(geoList);
    }

}

