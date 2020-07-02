package com.robinz.service.impl;

import com.robinz.dao.CommentDao;
import com.robinz.pojo.Comment;
import com.robinz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public List<Comment> getCommentByBlogId(Long blogId) {  //查询父评论
        //没有父节点的默认为-1
        List<Comment> comments = commentDao.findByBlogIdAndParentCommentNull(blogId, Long.parseLong("-1"));
        for(Comment comment : comments) {
            comment.setReplyComments(commentDao.findSecondaryCommentBySelfId(comment.getId()));
        }
        return comments;
    }

    @Override
    public List<Comment> findSecondaryCommentBySelfId(Long id) {    //输入自己的Id查询自己的二级评论
        return commentDao.findSecondaryCommentBySelfId(id);
    }

    @Override
    public int saveComment(Comment comment) {   //接收回复的表单
        //防止输入集合为null
        if(comment.getParentCommentId() != null) {
            comment.setParentComment(commentDao.findById(comment.getParentCommentId()));
        }

        //Union-Find算法（Union操作），若父级评论不是顶级，则向上迭代找到顶级评论作为父评论，只改Id，不改父亲name
        Long curId = comment.getParentComment().getId();
        if(curId != -1) {
            comment.setParentNickname(commentDao.findById(curId).getNickname());
            while (commentDao.findById(curId).getParentCommentId() != -1) {
                curId = commentDao.findById(curId).getParentCommentId();
            }
        }
        //Union更新
        comment.setParentCommentId(curId);
        if(curId == -1)
            comment.setParentComment(null);
        else
            comment.setParentComment(commentDao.findById(curId));

        //能走到这，说明ParentCommentId和ParentComment已经初始化好了
        comment.setCreateTime(new Date());

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
