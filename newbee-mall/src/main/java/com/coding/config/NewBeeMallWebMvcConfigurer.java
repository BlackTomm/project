package com.coding.config;

import com.coding.common.Constants;
import com.coding.interceptor.AdminLoginInterceptor;
import com.coding.interceptor.MallLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 配置拦截路径
 * @author: Black Tom
 * @create: 2020-07-09 11:32
 **/
@Configuration
public class NewBeeMallWebMvcConfigurer implements WebMvcConfigurer {

	@Autowired
	private AdminLoginInterceptor adminLoginInterceptor;

	@Autowired
	private MallLoginInterceptor mallLoginInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 添加一个拦截器，拦截以/admin为前缀的url路径（后台登陆拦截）
		/*registry.addInterceptor(adminLoginInterceptor)
				.addPathPatterns("/admin/**")
				.excludePathPatterns("/admin/login")
				.excludePathPatterns("/admin/dist/**")
				.excludePathPatterns("/admin/plugins/**");*/

		// 商城页面登陆拦截
		registry.addInterceptor(mallLoginInterceptor)
				.excludePathPatterns("/admin/**")
				.excludePathPatterns("/register")
				.excludePathPatterns("/login")
				.excludePathPatterns("/logout")
				.addPathPatterns("/goods/detail/**")
				.addPathPatterns("/shop-cart")
				.addPathPatterns("/shop-cart/**")
				.addPathPatterns("/saveOrder")
				.addPathPatterns("/orders")
				.addPathPatterns("/orders/**")
				.addPathPatterns("/personal")
				.addPathPatterns("/personal/updateInfo")
				.addPathPatterns("/selectPayType")
				.addPathPatterns("/payPage");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
		registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
	}
}
