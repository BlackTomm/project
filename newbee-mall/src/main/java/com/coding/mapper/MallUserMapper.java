package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.MallUser;
import com.coding.util.PageUtil;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 01:05
 **/
public interface MallUserMapper extends BaseMapper<MallUser> {

	@Select("select * from tb_newbee_mall_user where login_name = #{loginName} and is_deleted = 0")
	MallUser selectByLoginName(String loginName);

	@Select("select * from tb_newbee_mall_user where login_name = #{loginName} and password_md5 = #{passwordMD5} and is_deleted = 0")
	MallUser selectByLoginNameAndPasswd(String loginName, String passwordMD5);


	/*@Select("select * from tb_newbee_mall_user where login_name = #{loginName} and password_md5 = #{passwordMD5} " +
			"and is_deleted = 0 limit #{start},#{limit}")*/
	List<MallUser> findMallUserList(PageUtil pageUtil);


	/*@Select("select count(*) from tb_newbee_mall_user where login_name = #{loginName}")*/
	int getTotalMallUsers(PageUtil pageUtil);
}
