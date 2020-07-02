package com.robinz.web.admin;

import com.robinz.pojo.User;
import com.robinz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class loginController {

    @Autowired  //Bean的注入，不然不创建实例无法调用方法，创建实例往往很麻烦且浪费
    private UserService userService;

    @GetMapping
    public String loginPage() {
        System.out.println(1);
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes attributes) {

        User user = userService.checkUser(username, password);

        if(user != null) {
            user.setPassword(null); //密码不保存
            session.setAttribute("user", user); //保持状态
            return "admin/index";
        }
        attributes.addFlashAttribute("message", "用户名或密码错误");
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
