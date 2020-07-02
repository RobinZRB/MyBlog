package com.robinz.service.impl;

import com.robinz.NotFoundException;
import com.robinz.dao.BlogDao;
import com.robinz.dao.TagDao;
import com.robinz.pojo.Blog;
import com.robinz.pojo.Tag;
import com.robinz.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogDao blogDao;

    //事务注解，所有的CRUD都放在事务里
    @Transactional
    @Override
    public int saveTag(Tag tag) {
        return tagDao.saveTag(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagDao.getTag(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagDao.getTagByName(name);
    }

    @Transactional
    @Override
    public List<Tag> getAllTag() {
        return tagDao.getAllTag();
    }

    @Transactional
    @Override
    public List<Tag> getBlogTag() {
        return tagDao.getBlogTag();
    }

    @Override
    public List<Tag> getTagByString(String text) {    //从tagIds字符串中获取id，根据id获取tag集合
        List<Tag> tags = new ArrayList<>();
        List<Long> longs = convertToList(text);
        for (Long long1 : longs) {
            tags.add(tagDao.getTag(long1));
        }
        return tags;
    }

    private List<Long> convertToList(String ids) {  //把前端的tagIds字符串转换为list集合
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public int updateTag(Tag tag) {
        return tagDao.updateTag(tag);
    }

    @Transactional
    @Override
    public int deleteTag(Long id) {
        List<Blog> blogFromThisTag = blogDao.getByTagId(id);
        //先检查这些blog是否只有这一个标签，是的话不允许删除
        for(Blog blog : blogFromThisTag) {
            List<Tag> oldTags = blogDao.findSelfTags(blog);
            if(oldTags.size() == 1)
                throw new NotFoundException("还有博客使用此标签，不允许删除");
        }
        //先根据TagId删除对应的BlogAndTag
        tagDao.deleteBlogAndTagFromTag(id);
        //将blog中对应的tags属性更新
        for(Blog blog : blogFromThisTag) {
            List<Tag> newTags = blogDao.findSelfTags(blog);
            blog.setTags(newTags);
            blog.init();
        }

        return tagDao.deleteTag(id);
    }
}
