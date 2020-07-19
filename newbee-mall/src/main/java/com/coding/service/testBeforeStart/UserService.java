package com.coding.service.testBeforeStart;

import com.coding.util.PageResult;
import com.coding.util.PageUtil;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-09 16:38
 **/
public interface UserService {

	/**
	 * 分页功能
	 *
	 * @param pageUtil
	 * @return
	 */
	PageResult getAdminUserPage(PageUtil pageUtil);
}
