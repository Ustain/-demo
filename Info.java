package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@TableName("info")
@Data
public class Info {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private Integer sid;
    private String neighbourhood;
    private Integer price;
    private String tel;
    private String state;
    private String img;
    private String astartTime;
    private String aendTime;
    private String rate;
}
