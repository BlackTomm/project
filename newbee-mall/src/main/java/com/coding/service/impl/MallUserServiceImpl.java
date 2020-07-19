package com.coding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coding.common.Constants;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.MallUserVO;
import com.coding.entity.Goods;
import com.coding.entity.MallUser;
import com.coding.mapper.MallUserMapper;
import com.coding.service.MallUserService;
import com.coding.util.BeanUtil;
import com.coding.util.MD5Util;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 01:00
 **/
@Service
public class MallUserServiceImpl implements MallUserService {

	@Resource
	private MallUserMapper mallUserMapper;

	@Override
	public PageResult getNewBeeMallUsersPage(PageUtil pageUtil) {
		List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
		int total = mallUserMapper.getTotalMallUsers(pageUtil);
		PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}

	@Override
	public String register(String loginName, String password) {
		if (mallUserMapper.selectByLoginName(loginName) != null) {
			return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
		}
		MallUser registerUser = MallUser.builder().loginName(loginName)
				.nickName(loginName)
				.passwordMd5(MD5Util.MD5Encode(password, "UTF-8")).build();
		if (mallUserMapper.insert(registerUser) > 0) {
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.DB_ERROR.getResult();
	}

	@Override
	public String login(String loginName, String passwordMD5, HttpSession httpSession) {
		MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
		if (user != null && httpSession != null) {
			if (user.getLockedFlag() == 1) {
				return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
			}
			//昵称太长 影响页面展示
			if (user.getNickName() != null && user.getNickName().length() > 7) {
				String tempNickName = user.getNickName().substring(0, 7) + "..";
				user.setNickName(tempNickName);
			}
			MallUserVO mallUserVO = new MallUserVO();
			BeanUtil.copyProperties(user, mallUserVO);
			//设置session
			httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
			return ServiceResultEnum.SUCCESS.getResult();
		}
		return ServiceResultEnum.LOGIN_ERROR.getResult();
	}

	@Override
	public MallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession) {
		return null;
	}

	@Override
	public Boolean lockUsers(Integer[] ids, int lockStatus) {
		if (ids.length < 1) {
			return false;
		}
		List<Integer> lockUserList = Arrays.asList(ids);
		int lockUserNum = lockUserList.stream().mapToInt(id -> mallUserMapper.update(
				new MallUser().setIsDeleted((byte) lockStatus),
				new QueryWrapper<MallUser>()
						.lambda().eq(MallUser::getUserId, id))).sum();
		return lockUserNum == ids.length;
	}

}
