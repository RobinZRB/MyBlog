package com.robinz.service.impl;

import com.robinz.dao.BlogDao;
import com.robinz.NotFoundException;
import com.robinz.dao.CommentDao;
import com.robinz.pojo.Blog;
import com.robinz.pojo.BlogAndTag;
import com.robinz.pojo.Comment;
import com.robinz.pojo.Tag;
import com.robinz.service.BlogService;
import com.robinz.util.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private CommentDao commentDao;

    @Override
    public Blog getBlog(Long id) {
        return blogDao.getBlog(id);
    }

    @Override
    public Blog getDetailedBlog(Long id) {
        System.out.println(id);
        Blog blog = blogDao.getDetailedBlog(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        String content = blog.getContent();
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));  //将Markdown格式转换成html
        blog.setViews(blog.getViews()+1);
        blogDao.updateViews(blog);
        return blog;
    }

    @Override
    public List<Blog> getAllBlog() {
        return blogDao.getAllBlog();
    }

    @Override
    public List<Blog> getByTypeId(Long typeId) {
        return blogDao.getByTypeId(typeId);
    }

    @Override
    public List<Blog> getByTagId(Long tagId) {
        return blogDao.getByTagId(tagId);
    }

    @Override
    public List<Blog> getIndexBlog() {
        return blogDao.getIndexBlog();
    }

    @Override
    public List<Blog> getAllRecommendBlog() {
        return blogDao.getAllRecommendBlog();
    }

    @Override
    public List<Blog> getPortRecommendBlog() {
        List<Blog> tmpRecommendBlog = new ArrayList();
        List<Blog> allRecommendBlog = blogDao.getAllRecommendBlog();
        for(int i = 0; i < 10; i++)
            tmpRecommendBlog.add(allRecommendBlog.get(i));
        return tmpRecommendBlog;
    }

    @Override
    public List<Blog> get3RecommendBlog() {
        List<Blog> tmpRecommendBlog = new ArrayList();
        List<Blog> allRecommendBlog = blogDao.getAllRecommendBlog();
        if(allRecommendBlog.size() > 3) {
            for (int i = 0; i < 3; i++)
                tmpRecommendBlog.add(allRecommendBlog.get(i));
            return tmpRecommendBlog;
        }
        else
            return allRecommendBlog;
    }

    @Override
    public List<Blog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findGroupYear();
        Set<String> set = new HashSet<>(years);  //set去掉重复的年份
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : set)
            map.put(year, blogDao.findByYear(year));
        return map;
    }

    @Override
    public int countBlog() {
        return blogDao.getAllBlog().size();
    }

    @Override
    public List<Blog> searchAllBlog(Blog blog) {
        return blogDao.searchAllBlog(blog);
    }

    @Override    //新增博客
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        //保存博客
        blogDao.saveBlog(blog);
        //保存博客后才能获取自增的id
        Long id = blog.getId();
        //将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        //维护blogAndTag
        for (Tag tag : tags) {
            //这里blog类中的指针相连，但是我们查tag是根据BlogAndTag查的，所以应该连接这个
            blogAndTag = new BlogAndTag(tag.getId(), id);
            blogDao.saveBlogAndTag(blogAndTag);
        }
        return 1;
    }

    @Override   //编辑博客
    public int updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        //将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        //这里应该先删，再更新
        blogDao.deleteBlogAndTag(blog.getId());
        //防止null值
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            if(tag == null)
                continue;
            blogAndTag = new BlogAndTag(tag.getId(), blog.getId());
            blogDao.saveBlogAndTag(blogAndTag);
        }
        return blogDao.updateBlog(blog);
    }

    @Override
    public int deleteBlog(Long id) {
        List<Comment> replyComments = commentDao.findByBlogId(id);
        for(Comment replyComment : replyComments) {
            commentDao.deleteComment(replyComment);
        }
        blogDao.deleteBlogAndTag(id);
        return blogDao.deleteBlog(id);
    }

    @Override
    public int deleteBlogAndTag(Long id) {
        return blogDao.deleteBlogAndTag(id);
    }
}
