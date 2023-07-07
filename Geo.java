package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@TableName("geo")
@Data
public class Geo {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private String neighbourhood;
    private Integer sid;
    private Double x;//纬度
    private Double y;//经度
}
