package com.coding.mapper.testBeforeStart;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.testBeforeStart.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-06 23:07
 **/
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 根据参数查询用户列表
	 *
	 * @param param
	 * @return
	 */

	@Select("select * from tb_user order by id desc limit #{start}, #{limit}")
	List<User> findUsers(Map param);

	/**
	 * 查询用户总数
	 *
	 * @param param
	 * @return
	 */
	@Select("select count(*) from tb_user")
	int getTotalUser(Map param);
}
