package com.coding.controller.testBeforeStart;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: http请求格式自动转化测试
 * @author: Black Tom
 * @create: 2020-07-06 18:04
 **/
@RestController
public class WebMvcAutoConfigurationControllerTest {
	/**
	 * @Description: SpringMVC 中的类型转换，Http 请求传递的数据都是字符串 String 类型的，
	 * 上面这个方法在 Controller 中定义，如果该方法对应的地址接收到到浏览器的请求的话，并且
	 * 请求中含有 goodsName(String 类型)、weight(float类型)、type(int类型)、onSale(Boolean类型)
	 * 参数且都已经被进行正确的类型转换了，如果参数无法通过 String 强转的话也会报错，这就是所提到的
	 * MessageCodesResolver
	 * @Param: [goodsName, weight, type, onSale]
	 * @return: void
	 */
	@RequestMapping("/test/type/conversion")
	public void typeConversionTest(String goodsName, float weight, int type, Boolean onSale) {
		System.out.println("goodsName:" + goodsName);
		System.out.println("weight:" + weight);
		System.out.println("type:" + type);
		System.out.println("onSale:" + onSale);
	}


}
