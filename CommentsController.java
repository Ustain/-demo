package com.example.springboot.controller;

import com.example.springboot.commen.Result;
import com.example.springboot.entity.Comments;
import com.example.springboot.mapper.CommentsMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Resource
    CommentsMapper commentsMapper;

    @GetMapping
    public Result<?> getAllCommentsData() {
        List<Comments> rateList = commentsMapper.selectList(null);
        return Result.success(rateList);
    }

    @PostMapping
    public Result<?> saveComments(@RequestBody Comments comments) {
        System.out.println("Received comments: " + comments.toString());

        comments.setNeighbourhood(comments.getNeighbourhood());
        comments.setSid(comments.getSid());
        comments.setComments(comments.getComments());
        // 保存到数据库
        commentsMapper.insert(comments);
        return Result.success("评论保存成功");
    }

}

