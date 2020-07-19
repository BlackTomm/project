package com.coding.service.impl;

import com.coding.entity.AdminUser;
import com.coding.mapper.AdminUserMapper;
import com.coding.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class AdminUserServiceImplTest {
	@Autowired
	private AdminUserService adminUserService;

	@Test
	void login() {
	}

	@Test
	void getUserDetailById() {
	}

	@Test
	void updateName() {
		int loginUserId = 3;
		String loginUserName = "newbee-admin22";
		String nickName = "新丰22";
//		AdminUser adminUser = adminUserMapper.selectById(loginUserId);

		adminUserService.updateName(loginUserId, loginUserName, nickName);
	}

	@Test
	void updatePassword() {
		int loginUserId = 3;
		String originalPassword = "123456";
		String newPassword = "1234";
		adminUserService.updatePassword(loginUserId, originalPassword, newPassword);
	}
}