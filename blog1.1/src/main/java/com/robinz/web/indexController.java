package com.robinz.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.robinz.pojo.Blog;
import com.robinz.pojo.Comment;
import com.robinz.pojo.Tag;
import com.robinz.pojo.Type;
import com.robinz.service.BlogService;
import com.robinz.service.CommentService;
import com.robinz.service.TagService;
import com.robinz.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 这里是Spring MVC控制层，主要用户接受用户请求并调用 Service 层返回数据给前端页面。
 * 首页控制器，可以用来捕捉错误代码并返回方法
 * 用来跳转到主页面
 */
@Controller
public class indexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String toIndex(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 8);
        List<Blog> allBlog = blogService.getIndexBlog();    //获取主页博客
        List<Type> allType = typeService.getBlogType();  //获取博客的类型(联表查询)(首页右侧类型栏，每个类型多少博客blogs.size())
        List<Tag> allTag = tagService.getBlogTag();  //获取博客的标签(联表查询)（同理）

        List<Blog> recommendBlog = blogService.getAllRecommendBlog();  //获取推荐博客
        if(recommendBlog.size() > 10)
            recommendBlog = blogService.getPortRecommendBlog();

        //得到分页结果对象
        PageInfo pageInfo = new PageInfo(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("tags", allTag);
        model.addAttribute("types", allType);
        model.addAttribute("recommendBlogs", recommendBlog);
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                         @RequestParam String query, Model model){

        PageHelper.startPage(pagenum, 5);
        List<Blog> searchBlog = blogService.getSearchBlog(query);
        PageInfo pageInfo = new PageInfo(searchBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String toLogin(@PathVariable Long id, Model model){
        Blog blog = blogService.getDetailedBlog(id);
        List<Comment> comments = commentService.getCommentByBlogId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("blog", blog);
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs",blogService.get3RecommendBlog());
        return "_fragments :: newblogList";
    }
}
