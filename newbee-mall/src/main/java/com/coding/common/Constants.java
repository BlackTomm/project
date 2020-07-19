package com.coding.common;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-09 16:25
 **/
public class Constants {
	//上传到目标文件夹所在路径，也是图片请求获取的路径
//	public final static String FILE_UPLOAD_DIC = "C:\\Users\\onedy\\Pictures\\upload\\";
	public final static String FILE_UPLOAD_DIC = "E:\\IDE下载文件\\chrome\\newbee-ltd-newbee-mall-master\\newbee-mall\\src\\main\\resources\\upload\\upload\\";

	public static final int RESULT_CODE_SUCCESS = 200;  // 成功处理请求
	public static final int RESULT_CODE_BAD_REQUEST = 412;  // 请求错误
	public static final int RESULT_CODE_PARAM_ERROR = 406;  // 传参错误
	public static final int RESULT_CODE_SERVER_ERROR = 500;  // 服务器错误

	//商品状态：上架为0，下架为1
	public static final int SELL_STATUS_UP = 0;
	public static final int SELL_STATUS_DOWN = 1;

	public static final int INDEX_CAROUSEL_NUMBER = 5;//首页轮播图数量(可根据自身需求修改)
	public static final int INDEX_CATEGORY_NUMBER = 10;//首页一级分类的最大数量

	public static final int INDEX_GOODS_HOT_NUMBER = 4;//首页热卖商品数量
	public static final int INDEX_GOODS_NEW_NUMBER = 5;//首页新品数量
	public static final int INDEX_GOODS_RECOMMEND_NUMBER = 10;//首页推荐商品数量

	public final static int SHOPPING_CART_ITEM_LIMIT_NUMBER = 5;//购物车中单个商品的最大购买数量(可根据自身需求修改)
	public final static int SHOPPING_CART_ITEM_TOTAL_NUMBER = 13;//购物车中商品的最大数量(可根据自身需求修改)

	public final static String MALL_VERIFY_CODE_KEY = "mallVerifyCode";//验证码key
	public final static String MALL_USER_SESSION_KEY = "mallUser";//session中user的key,普通用户登录首页对应的session

	public static final int GOODS_SEARCH_PAGE_LIMIT = 10;//搜索分页的默认条数(每页10条)
	public final static int ORDER_SEARCH_PAGE_LIMIT = 8;//我的订单列表分页的默认条数(每页8条)

	public static final int SEARCH_CATEGORY_NUMMBER = 8;//搜索页一级分类的最大数量

}
