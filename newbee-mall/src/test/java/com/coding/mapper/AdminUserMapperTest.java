package com.coding.mapper;

import com.coding.entity.AdminUser;
import com.coding.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-09 08:46
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminUserMapperTest {
	@Autowired
	private AdminUserMapper adminUserMapper;

	@Test
	public void login() {
		String passwordMd5 = MD5Util.MD5Encode("123456", "UTF-8");
		AdminUser user = adminUserMapper.selectByAdminUser("admin", passwordMd5);
		//查询	返回密码为null。。。
		log.info("管理员账号查询：{}", user);

		user = adminUserMapper.selectById(1);
		log.info("根据管理员Id进行账号查询：{}", user);
	}

	@Test
	public void nameUpdate() {
		int loginUserId = 3;
		String loginUserName = "newbee-admin21";
		String nickName = "新丰21";

		AdminUser adminUser = adminUserMapper.selectById(loginUserId);
		adminUser.setLoginUserName(loginUserName);
		adminUser.setNickName(nickName);
		log.info("更新管理员账号信息： {}", adminUserMapper.updateById(adminUser));
	}

	@Test
	public void passwordUpdate() {
		int loginUserId = 3;
		String originalPassword = "123456";
		String newPassword = "1234";

		AdminUser adminUser = adminUserMapper.selectById(loginUserId);

		String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
		String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
		//比较原密码是否正确
		if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
			//设置新密码并修改
			adminUser.setLoginPassword(newPasswordMd5);
				/*adminUserMapper.update(adminUser, new QueryWrapper<AdminUser>()
						.lambda().eq(AdminUser::getAdminUserId,loginUserId))*/
			log.info("更新管理员账号信息： {}", adminUserMapper.updateById(adminUser));
		} else {
			log.info("更新密码失败");
		}
	}

}
