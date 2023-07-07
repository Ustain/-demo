package com.example.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("shareparking")
@Data
public class Shareparking {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private String sid;
    private String 可用时间段;
    private Integer price;
    private String img;
    private String tel;
    private String 小区;
    @TableField (exist = false)
    private String state;
}

