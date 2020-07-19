package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.AdminUser;
import org.apache.ibatis.annotations.Select;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-08 23:03
 **/
public interface AdminUserMapper extends BaseMapper<AdminUser> {

	/**
	 * @Description: 根据管理员账号与密码进行查询
	 * @Param: [loginUserName, loginPassword]
	 * @return: com.coding.entity.AdminUser
	 * login_user_name = #{loginUserName} and
	 */
	@Select("select * from newbeemall_admin_user where login_user_name = #{loginUserName} and login_user_password = #{loginUserPassword}")
	AdminUser selectByAdminUser(String loginUserName, String loginUserPassword);
}
