package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.IndexConfig;
import com.coding.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-15 19:26
 **/
public interface IndexConfigMapper extends BaseMapper<IndexConfig> {

	@Select("select * from tb_newbee_mall_index_config where config_type = #{configType} and is_deleted = 0 " +
			"order by config_rank desc limit #{start},#{limit}")
	List<IndexConfig> findIndexConfigList(PageUtil pageUtil);

	@Select("select count(*) from tb_newbee_mall_index_config where config_type = #{configType} and is_deleted = 0")
	int getTotalIndexConfigs(PageUtil pageUtil);

	@Select("select * from tb_newbee_mall_index_config where config_type = #{configType} and is_deleted = 0 " +
			"order by config_rank desc limit #{number}")
	List<IndexConfig> findIndexConfigsByTypeAndNum(@Param("configType") int configType, @Param("number") int number);
}
