package com.coding.service;

import com.coding.controller.vo.MallUserVO;
import com.coding.entity.MallUser;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;

import javax.servlet.http.HttpSession;

/**
* @Description: 商城普通用户接口
* @Param:
* @return:
*/
public interface MallUserService {

	/**
	 * 后台分页
	 *
	 * @param pageUtil
	 * @return
	 */
	PageResult getNewBeeMallUsersPage(PageUtil pageUtil);

	/**
	 * 用户注册
	 *
	 * @param loginName
	 * @param password
	 * @return
	 */
	String register(String loginName, String password);

	/**
	 * 登录
	 *
	 * @param loginName
	 * @param passwordMD5
	 * @param httpSession
	 * @return
	 */
	String login(String loginName, String passwordMD5, HttpSession httpSession);

	/**
	 * 用户信息修改并返回最新的用户信息
	 *
	 * @param mallUser
	 * @return
	 */
	MallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession);

	/**
	 * 用户禁用与解除禁用(0-未锁定 1-已锁定)
	 *
	 * @param ids
	 * @param lockStatus
	 * @return
	 */
	Boolean lockUsers(Integer[] ids, int lockStatus);
}
