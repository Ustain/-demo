package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@TableName("book")
@Data
public class Book {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private Integer sid;
    private String 小区;
    private String customerName;
    private String tel;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String date;
    private String begintime;
    private String endtime;
    private String state;
}
