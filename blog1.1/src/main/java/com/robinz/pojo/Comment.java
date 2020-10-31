package com.robinz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;    //have
    private String nickname;    //have
    private String email;   //have
    private String content; //have
    private boolean adminComment;  //是否为管理员评论   //have

    //头像
    private String avatar;  //have
    private Date createTime;    //have

    private Long blogId;    //have
    private Long parentCommentId;  //父评论id  //have

    private String parentNickname;

    //父评论
    private Comment parentComment;

    private Blog blog;

    private List<Comment> replyComments;
}
