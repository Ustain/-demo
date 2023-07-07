package com.example.springboot.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.commen.Result;
import com.example.springboot.entity.Shareparking;
import com.example.springboot.mapper.ShareparkingMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shareparking")
public class ShareparkingController {
    //本来是在Service类中调用，再调用Service类的
    @Resource
    ShareparkingMapper shareparkingMapper;


    @PostMapping
    public Result<?> save(@RequestBody Shareparking shareparking){
        if (shareparking.getState()==null){
            shareparking.setState("空闲中");
        }
        shareparkingMapper.insert(shareparking);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Shareparking shareparking){
        shareparkingMapper.updateById(shareparking);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Shareparking> wrapper = Wrappers.<Shareparking>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(Shareparking::getSid,search);
        }

        Page<Shareparking> shareparkingPage = shareparkingMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(shareparkingPage);
    }

    //    删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        shareparkingMapper.deleteById(id);
        return Result.success();
    }


    //    根据id查询
    @GetMapping ("/{id}")
    public Result<?> getById(@PathVariable Long id){
        Shareparking shareparking = shareparkingMapper.selectById(id);
        return Result.success(shareparking);
    }


}
