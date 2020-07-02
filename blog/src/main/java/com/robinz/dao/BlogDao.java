package com.robinz.dao;

import com.robinz.pojo.Blog;
import com.robinz.pojo.BlogAndTag;
import com.robinz.pojo.Comment;
import com.robinz.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogDao {

    Blog getBlog(Long id);  //后台展示博客down

    Blog getDetailedBlog(@Param("id") Long id);  //博客详情down，虽然查询语句中得到的是重复的blog+多个tag行，但是返回的只有一个Blog类

    List<Blog> getAllBlog();    //down

    List<Blog> getByTypeId(Long typeId);  //根据类型id获取博客down

    List<Blog> getByTagId(Long tagId);  //根据标签id获取博客down

    List<Blog> getIndexBlog();  //主页博客展示down

    List<Blog> getAllRecommendBlog();  //推荐博客展示down

    List<Blog> getSearchBlog(String query);  //全局搜索博客down

    List<Blog> searchAllBlog(Blog blog);  //后台根据标题、分类、推荐搜索博客down

    List<String> findGroupYear();  //查询所有年份，返回一个集合down

    List<Blog> findByYear(@Param("year") String year);  //按年份查询博客down

    int saveBlog(Blog blog);    //down

    int saveBlogAndTag(BlogAndTag blogAndTag);  //down

    int updateBlog(Blog blog);  //down

    int deleteBlog(Long id);    //down

    int deleteBlogAndTag(Long id);

    int updateViews(Blog blog);

    List<Tag> findSelfTags(Blog blog);
}
