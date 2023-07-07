package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

@TableName("rate")
@Data
public class Rate {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private String neighbourhood;
    private Integer sid;
    private BigDecimal rate;
}
