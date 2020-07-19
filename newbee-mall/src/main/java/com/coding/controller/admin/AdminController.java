package com.coding.controller.admin;

import com.coding.common.ServiceResultEnum;
import com.coding.entity.AdminUser;
import com.coding.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @description: 后台面板管理
 * @author: Black Tom
 * @create: 2020-07-07 14:23
 **/
@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminUserService adminUserService;

	@GetMapping({"/index",""})
	public String index(HttpServletRequest request) {
		request.setAttribute("path", "index");
		return "admin/index";
	}

	@GetMapping("/category")
	public String category(HttpServletRequest request) {
		request.setAttribute("path", "category");
		return "admin/category";
	}

	/*------------------------------管理员登录------------------------------*/

	@GetMapping({"/login"})
	public String login() {
		return "admin/login";
	}

	@PostMapping(value = "/login")
	public String login(@RequestParam("userName") String loginUserName,
						@RequestParam("password") String loginUserPassword,
						@RequestParam("verifyCode") String verifyCode,
						HttpSession session) {
		if (StringUtils.isEmpty(verifyCode)) {
			session.setAttribute("errorMsg", "验证码不能为空");
			return "admin/login";
		}
		if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(loginUserPassword)) {
			session.setAttribute("errorMsg", "用户名或密码不能为空");
			return "admin/login";
		}
		String kaptchaCode = session.getAttribute("verifyCode") + "";
		if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
			session.setAttribute("errorMsg", "验证码错误");
			return "admin/login";
		}

		//查询正常，但密码返回为null
		AdminUser adminUser = adminUserService.login(loginUserName, loginUserPassword);
		if (adminUser != null) {
			session.setAttribute("loginUser", adminUser.getNickName());
			session.setAttribute("loginUserId", adminUser.getAdminUserId());
			//session过期时间设置为30天
			session.setMaxInactiveInterval(60 * 60 * 24 * 30);
			return "redirect:/admin/index";
		} else {
			session.setAttribute("errorMsg", "登陆失败，请联系主管获得测试账号");
			return "admin/login";
		}
	}

	/*------------------------------更改管理员信息------------------------------*/

	@GetMapping("/profile")
	public String profile(HttpServletRequest request) {
		Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
		AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
		if (adminUser == null) {
			return "admin/login";
		}
		request.setAttribute("path", "profile");
		request.setAttribute("loginUserName", adminUser.getLoginUserName());
		request.setAttribute("nickName", adminUser.getNickName());
		return "admin/profile";
	}

	@PostMapping("/profile/name")
	@ResponseBody
	public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
							 @RequestParam("nickName") String nickName) {
		if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
			return "参数不能为空";
		}
		Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
		if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
			return ServiceResultEnum.SUCCESS.getResult();
		} else {
			return "修改失败";
		}
	}

	@PostMapping("/profile/password")
	@ResponseBody
	public String passwordUpdate(HttpServletRequest request,
								 @RequestParam("originalPassword") String originalPassword,
								 @RequestParam("newPassword") String newPassword) {
		if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
			return "参数不能为空";
		}
		Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
		if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
			//修改成功后清空session中的数据，前端控制跳转至登录页
			request.getSession().removeAttribute("loginUserId");
			request.getSession().removeAttribute("loginUser");
			request.getSession().removeAttribute("errorMsg");
			return ServiceResultEnum.SUCCESS.getResult();
		} else {
			return "修改失败";
		}
	}

	/*------------------------------退出登录------------------------------*/

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("loginUserId");
		request.getSession().removeAttribute("loginUser");
		request.getSession().removeAttribute("errorMsg");
		return "admin/login";
	}
}
