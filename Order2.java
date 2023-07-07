package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("order2")
@Data
public class Order2 {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private String neighbourhood;
    private Integer sid;
    private String tel;
    private String date;
    private String startTime;
    private String endTime;
}


