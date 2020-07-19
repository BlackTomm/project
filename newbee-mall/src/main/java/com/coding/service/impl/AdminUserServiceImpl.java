package com.coding.service.impl;

import com.coding.entity.AdminUser;
import com.coding.mapper.AdminUserMapper;
import com.coding.service.AdminUserService;
import com.coding.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-08 23:06
 **/
@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Resource
	private AdminUserMapper adminUserMapper;

	@Override
	public AdminUser login(String loginUserName, String loginUserPassword) {
		String passwordMd5 = MD5Util.MD5Encode(loginUserPassword, "UTF-8");
		return adminUserMapper.selectByAdminUser(loginUserName, passwordMd5);
	}

	@Override
	public AdminUser getUserDetailById(Integer loginUserId) {
		return adminUserMapper.selectById(loginUserId);
	}

	@Override
	public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
		AdminUser adminUser = adminUserMapper.selectById(loginUserId);
		//当前用户非空才可以进行更改
		if (adminUser != null) {
			//设置新名称并修改
			adminUser.setLoginUserName(loginUserName);
			adminUser.setNickName(nickName);
			//修改成功则返回true
			return adminUserMapper.updateById(adminUser) > 0;
		}
		return false;
	}

	@Override
	public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
		AdminUser adminUser = adminUserMapper.selectById(loginUserId);
		//当前用户非空才可以进行更改
		if (adminUser != null) {
			//设置新密码
			String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
			String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
			//比较原密码是否正确
			if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
				//设置新密码并修改
				adminUser.setLoginPassword(newPasswordMd5);
				//修改成功则返回true
				return adminUserMapper.updateById(adminUser) > 0;
			}
		}
		return false;
	}
}
