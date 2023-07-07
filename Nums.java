package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("nums")
@Data
public class Nums {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private Integer sid;
    private String neighbourhood;
    private Integer nums;
}
