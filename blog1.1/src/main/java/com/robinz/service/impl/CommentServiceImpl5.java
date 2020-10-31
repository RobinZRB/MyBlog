package com.robinz.service.impl;

import com.robinz.dao.CommentDao;
import com.robinz.pojo.Comment;
import com.robinz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 *
 * 无任何优化
 *
 * **/
public class CommentServiceImpl5 implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public List<Comment> getCommentByBlogId(Long blogId) {  //查询父评论
        List<Comment> comments = commentDao.findByBlogId(blogId);   //获得这个blog下的所有评论
        Map<Comment, Long> m = new HashMap<>(); //(评论-父亲id)
        Map<Long, Comment> mForComment = new HashMap<>();   //(评论ID-评论)
        Map<Long, List<Comment>> mForId = new HashMap<>();  //(顶级评论-所所有子评论)
        List<Comment> TopComment = new ArrayList<>();   //顶级评论
        for(Comment comment : comments) {
            m.put(comment, comment.getParentCommentId());
            mForComment.put(comment.getId(), comment);
            if(comment.getParentCommentId() == -1) {
                TopComment.add(comment);    //得到顶级评论
                mForId.put(comment.getId(), new ArrayList<>());
            }
        }

        for(Comment comment : comments) {
            long topId = find(comment, m, mForComment);
            if(comment.getParentCommentId() != -1) {
                mForId.get(topId).add(comment);
            }
        }

        for(Comment i : TopComment) {
            i.setReplyComments(mForId.get(i.getId()));
        }

        return TopComment;
    }

    public long find(Comment comment, Map<Comment, Long> m, Map<Long, Comment> mForComment) {
        while(comment.getId() != m.get(comment)) {
            if(m.get(comment) == -1)
                break;
            Comment fatherComment = mForComment.get(m.get(comment));
            m.put(comment, fatherComment.getId());
            comment = fatherComment;
        }
        return comment.getId();
    }

    @Override
    public List<Comment> findSecondaryCommentBySelfId(Long id) {    //输入自己的Id查询自己的二级评论
        return commentDao.findSecondaryCommentBySelfId(id);
    }

    @Override
    public int saveComment(Comment comment) {   //接收回复的表单
        comment.setCreateTime(new Date());
        if(comment.getParentCommentId() != null) {
            comment.setParentComment(commentDao.findById(comment.getParentCommentId()));
        }
        Long curId = comment.getParentComment().getId();
        comment.setParentCommentId(curId);
        if(curId == -1)
            comment.setParentComment(null);
        else {
            comment.setParentComment(commentDao.findById(curId));
            comment.setParentNickname(commentDao.findById(curId).getNickname());
        }
        return commentDao.saveComment(comment);
    }

    @Override
    public int deleteComment(Comment comment) {
        //如果是顶级评论，先删除其子评论，再删除自己
        List<Comment> childComments = commentDao.findSecondaryCommentBySelfId(comment.getId());
        for(Comment childComment : childComments) {
            commentDao.deleteComment(childComment);
        }
        return commentDao.deleteComment(comment);
    }
}
