package com.robinz.web.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.robinz.pojo.Type;
import com.robinz.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 从空表开始，我们首先进入types-input页面去初始化第一个id；
 * 此时id=null，我们无论输入name为什么都会添加成功；
 * 有了第一个值后，我们可以继续添加，也可进入types页面可以进行编辑
 * 假设我们进行新增处理，这里开始id!=null了，所以判断t是否为null，若为null，添加操作，否则视为重复添加
 * 假设我们进行编辑，此时传进来个type实体包含id和name（id是已知的）
 * 如果此时t不为空（说明在数据库找到一样的name，表示此时重复修改了），返回失败：否则成功，并进行更新操作
 * 删除同理，不再赘述
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")           //Model是thymeleaf的内容，是前端的页面展示模型，并用来传值。这一段是分页处理。
    public String types(@RequestParam(required = false, defaultValue = "1", value = "pagenum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 5);
        List<Type> allType = typeService.getAllType();
        //得到分页结果对象
        PageInfo<Type> pageInfo = new PageInfo<>(allType);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String toAddType(Model model){
        model.addAttribute("type", new Type());   //返回一个type对象给前端th:object，这里这个Type类里面的id和name是null
        return "admin/types-input";
    }

    //这里是必须要带id的，没id没法查询啊。
    @GetMapping("/types/{id}/input")
    public String toEditType(@PathVariable Long id, Model model){
         model.addAttribute("type", typeService.getType(id));
         return "admin/types-input";
    }


    @PostMapping("/types")      //没带id的是要添加的
    public String addType(Type type, RedirectAttributes attributes){    //新增
        //如果我传进来时判定id为空，那么我这里t得到的肯定是空的，直接添加成功
        Type t = typeService.getTypeByName(type.getName());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的分类");
            return "redirect:/admin/types/input";
        }
        else
            attributes.addFlashAttribute("msg", "添加成功");
        typeService.saveType(type);
        return "redirect:/admin/types";   //不能直接跳转到types页面，否则不会显示type数据(没经过types方法)
    }


    @PostMapping("/types/{id}")     //带id的是要更新的
    public String editType(@PathVariable Long id, Type type, RedirectAttributes attributes){  //修改
        Type t = typeService.getTypeByName(type.getName());
        if(t != null){
            attributes.addFlashAttribute("msg", "不能添加重复的分类");
            return "redirect:/admin/types/input";
        }
        else
            attributes.addFlashAttribute("msg", "修改成功");
        typeService.updateType(type);
        return "redirect:/admin/types";   //不能直接跳转到types页面，否则不会显示type数据(没经过types方法)
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/admin/types";
    }
}
