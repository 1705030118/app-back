package com.ldm.entity;

import lombok.Data;

import java.util.List;
/**
 * @title 动态主页
 * @description
 * @author lidongming
 * @updateTime 2020/4/17 19:09
 */
@Data
public class DynamicIndex {
    private int dynamicId;// 动态ID
    private String avatar;// 头像
    private int userId;// 用户ID
    private String userNickname;// 用户昵称
    private String publishTime;// 发表时间
    private String content;// 动态内容
    private String images;// 图片列表，数据库中以逗号分开
    private List<String> imageList;// 返回给前端的图片列表是一个list
    private String publishLocation;// 发表地点
    private Boolean isLike;// 是否已经点赞
    private Long likeCount;// 点赞量
    private int commentCount;// 评论量
}
