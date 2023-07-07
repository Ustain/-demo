package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "parkinginfo")
@Data
public class Parkinginfo2 {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private String 小区;
    private String state;
    private String 可用时间段;
    private Integer price;
    private String sid;
    private String 评分;
    private String tel;
    private String img;
}
