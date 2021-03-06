package com.ldm.dao;


import com.ldm.entity.Comment;
import com.ldm.request.PublishComment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper
public interface CommentDao {

    /**
     * 发布评论
     * @param request
     * @return
     */
    @Insert("INSERT INTO `t_comment`(`item_id`, `user_id`, " +
            "`publish_time`, `content`, `reply_count`, `flag`) VALUES" +
            "(#{itemId}, #{userId}, NOW(), #{content}, 0, #{flag})")
    int publishComment(PublishComment request);

    /**
     * @title 删除评论
     * @description 删除评论表和回复表中对应的数据,并更新对应活动/动态的评论量
     * @author lidongming
     * @updateTime 2020/4/9 23:58
     */
    @Delete({"DELETE FROM t_comment WHERE comment_id=#{commentId};",
    "DELETE FROM t_reply WHERE comment_id=#{commentId}"})
    int deleteComment(int commentId);

    /**
     * @title 活动评论量
     * @description 删除评论时,活动的评论量-1
     * @author lidongming
     * @updateTime 2020/4/10 0:05
     */
    @Update("UPDATE t_activity SET comment_count=comment_count-1 WHERE activity_id=#{itemId}")
    int reduceActivityCommentCount(int itemId);

    /**
     * @title 更新动态评论量
     * @description 删除评论时,动态的评论量-1
     * @author lidongming
     * @updateTime 2020/4/10 0:05
     */
    @Update("UPDATE t_dynamic SET comment_count=comment_count-1 WHERE dynamic_id=#{itemId}")
    int reduceDynamicCommentCount(int itemId);

    /**
     * @title 活动评论量
     * @description 删除评论时,活动的评论量+1
     * @author lidongming
     * @updateTime 2020/4/10 0:05
     */
    @Update("UPDATE t_activity SET comment_count=comment_count+1 WHERE activity_id=#{itemId}")
    int addActivityCommentCount(int itemId);

    /**
     * @title 更新动态评论量
     * @description 发表评论时,动态的评论量+1
     * @author lidongming
     * @updateTime 2020/4/10 0:05
     */
    @Update("UPDATE t_dynamic SET comment_count=comment_count+1 WHERE dynamic_id=#{itemId}")
    int addDynamicCommentCount(int itemId);

    /**
     * @title 获取评论列表,活动/动态详情页中展示评论列表，flag为0则活动，flag为1则动态
     * @description redis获取avatar.userNickname
     * @author ggh
     * @updateTime 2020/4/14 19:40
     */
    @Select("SELECT * FROM t_comment WHERE item_id=#{itemId} AND flag=#{flag} " +
            "ORDER BY publish_time DESC LIMIT #{pageNum},#{pageSize}")
    List<Comment> selectCommentList(int itemId,int flag, int pageNum,int pageSize);

}
