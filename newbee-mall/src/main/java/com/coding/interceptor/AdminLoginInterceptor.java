package com.coding.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 后台管理员登录拦截
 * @author: Black Tom
 * @create: 2020-07-09 11:22
 **/
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

	/**
	 * @Description: 在请求的预处理过程中读取当前 session 中是否存在 loginUser 对象，如果不存在则返回 false 并跳转至登录页面，
	 * 如果已经存在则返回 true，继续做后续处理流程
	 * @Param: [request, response, handler]
	 * @return: boolean
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestServletPath = request.getServletPath();
		if (requestServletPath.startsWith("/admin") && null == request.getSession().getAttribute("loginUser")) {
			request.getSession().setAttribute("errorMsg", "请登录");
			response.sendRedirect(request.getContextPath() + "/admin/login");
			return false;
		} else {
			request.getSession().removeAttribute("errorMsg");
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
