package com.robinz.service;

import com.robinz.pojo.Tag;

import java.util.List;

public interface TagService {

    int saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    List<Tag> getAllTag();

    List<Tag> getBlogTag();  //首页右侧展示tag对应的博客数量

    List<Tag> getTagByString(String text);   //从字符串中获取tag集合

    int updateTag(Tag tag);

    int deleteTag(Long id);
}
