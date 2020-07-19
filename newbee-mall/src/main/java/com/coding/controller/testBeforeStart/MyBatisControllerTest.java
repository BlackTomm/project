package com.coding.controller.testBeforeStart;

import com.coding.entity.testBeforeStart.User;
import com.coding.mapper.testBeforeStart.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: Mybatis测试类
 * @author: Black Tom
 * @create: 2020-07-06 23:18
 **/
@RestController
public class MyBatisControllerTest {
	@Autowired
	private UserMapper userMapper;

	// 查询所有记录
	@GetMapping("/users/mybatis/queryAll")
	public List<User> queryAll() {
		return userMapper.selectList(null);
	}

	// 新增一条记录
	@GetMapping("/users/mybatis/insert")
	public Boolean insert(String name, String password) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
			return false;
		}
		User user = User.builder().userName(name).password(password).build();
		return userMapper.insert(user) > 0;
	}

	// 修改一条记录
	@GetMapping("/users/mybatis/update")
	public Boolean insert(Integer id, String name, String password) {
		if (id == null || id < 1 || StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
			return false;
		}
		User user = User.builder().id(id).userName(name).password(password).build();
		return userMapper.updateById(user) > 0;
	}

	// 删除一条记录
	@GetMapping("/users/mybatis/delete")
	public Boolean insert(Integer id) {
		if (id == null || id < 1) {
			return false;
		}
		return userMapper.deleteById(id) > 0;
	}

}
