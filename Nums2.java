package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("nums2")
@Data
public class Nums2 {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private Integer sid;
    private String neighbourhood;
    private Integer nums2;
}
