package com.coding.service;

import com.coding.entity.AdminUser;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-08 22:24
 **/
public interface AdminUserService {
	/**
	 * @Description: 获取用户信息
	 * @Param: [loginUserName, loginUserPassword]
	 * @return: com.coding.entity.AdminUser
	 */
	AdminUser login(String loginUserName, String loginUserPassword);

	/**
	 * @Description: 根据Id获取用户信息
	 * @Param: [loginUserId]
	 * @return: com.coding.entity.AdminUser
	 */
	AdminUser getUserDetailById(Integer loginUserId);

	/**
	 * @Description: 修改管理员账号名及昵称
	 * @Param: [loginUserId, loginUserName, nickName]
	 * @return: java.lang.Boolean
	 */
	Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

	/**
	 * @Description: 修改管理员账号密码
	 * @Param: [loginUserId, originalPassword, newPassword]
	 * @return: java.lang.Boolean
	 */
	Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

}
