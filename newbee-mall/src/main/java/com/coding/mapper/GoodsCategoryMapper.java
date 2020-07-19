package com.coding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coding.entity.GoodsCategory;
import com.coding.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-12 15:54
 **/
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

	@Select("select * from tb_newbee_mall_goods_category where category_level = #{categoryLevel} " +
			"and parent_id= #{parentId} and is_deleted = 0 order by category_rank desc limit #{start}, #{limit}")
	List<GoodsCategory> findGoodsCategoryList(PageUtil pageUtil);


	@Select("select count(*) from tb_newbee_mall_goods_category where category_level = #{categoryLevel} " +
			"and parent_id= #{parentId} and is_deleted = 0")
	int getTotalGoodsCategories(PageUtil pageUtil);


	@Select("select * from tb_newbee_mall_goods_category where category_level = #{categoryLevel, jdbcType=TINYINT} " +
			"and category_name = #{categoryName, jdbcType=VARCHAR} and is_deleted = 0 limit 1")
	GoodsCategory selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel, @Param("categoryName") String categoryName);

	/**
	* @Description: 根据categoryId查询未被删除分类
	* @Param: [categoryId]
	* @return: com.coding.entity.GoodsCategory
	*/
	@Select("select * from tb_newbee_mall_goods_category where category_id = #{categoryId, jdbcType=TINYINT} " +
			"and is_deleted = 0")
	GoodsCategory selectByPrimaryKey(Long categoryId);

	/**
	 * 根据parentId和level获取分类列表
	 *
	 * @param parentIds
	 * @param categoryLevel
	 * @return
	 */
	@Select({
		"<script>",
			"select",
			"*",
			"from tb_newbee_mall_goods_category",
			"where parent_id in",
				"<foreach collection ='parentIds' item='parentId' open='(' separator=',' close=')'>",
					"#{parentId,jdbcType=BIGINT}",
				"</foreach>",
			"and category_level = #{categoryLevel,jdbcType=TINYINT}",
			"and is_deleted = 0",
			"order by category_rank desc",
			"<if test='number>0'>",
				"limit #{number}",
			"</if>",
		"</script>"
	})
	List<GoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds,
														   @Param("categoryLevel") int categoryLevel,
														   @Param("number") int number);
}
