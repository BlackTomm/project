package com.coding.service.testBeforeStart.impl;

import com.coding.entity.testBeforeStart.User;
import com.coding.mapper.testBeforeStart.UserMapper;
import com.coding.service.testBeforeStart.UserService;
import com.coding.util.PageResult;
import com.coding.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-09 16:38
 **/
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public PageResult getAdminUserPage(PageUtil pageUtil) {
		List<User> users = userMapper.findUsers(pageUtil);
		int total = userMapper.getTotalUser(pageUtil);
		PageResult pageResult = new PageResult(users, total, pageUtil.getLimit(), pageUtil.getPage());
		return pageResult;
	}
}
