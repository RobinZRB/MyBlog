package com.robinz.config;

import com.robinz.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  //注解为配置类
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {   //配置拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")   //我们匹配过滤admin下所有路径
                .excludePathPatterns("/admin")  //但是要排除这两个，不然登录都提交不了
                .excludePathPatterns("/admin/login");
    }
}
