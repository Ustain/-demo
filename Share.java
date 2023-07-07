package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@TableName("share")
@Data
public class Share {
    @TableId(value = "id" ,type = IdType.AUTO) //id是主键名
    private Integer id;
    private Integer sid;
    private Integer price;
    private String tel;
    private String img;
    private String neighbourhood;
    private String astartTime;
    private String aendTime;
}
