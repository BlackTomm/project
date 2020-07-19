package com.coding.controller.mall;

import com.coding.common.Constants;
import com.coding.common.ServiceResultEnum;
import com.coding.controller.vo.MallShoppingCartItemVO;
import com.coding.controller.vo.MallUserVO;
import com.coding.entity.MallShoppingCartItem;
import com.coding.service.MallShoppingCartService;
import com.coding.util.Result;
import com.coding.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-16 19:28
 **/
@Controller
public class ShoppingCartController {
	@Resource
	private MallShoppingCartService mallShoppingCartService;

	@GetMapping("/shop-cart")
	public String cartListPage(HttpServletRequest request, HttpSession session) {
		MallUserVO user = (MallUserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
		int itemsTotal = 0;
		int priceTotal = 0;
		List<MallShoppingCartItemVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
		if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
			itemsTotal = myShoppingCartItems.stream().mapToInt(MallShoppingCartItemVO::getGoodsCount).sum();
			if (itemsTotal < 1) {
				return "error/error_5xx";
			}
			//总价
			for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
				priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
			}
			if (priceTotal < 1) {
				return "error/error_5xx";
			}
		}
		request.setAttribute("itemsTotal", itemsTotal);
		request.setAttribute("priceTotal", priceTotal);
		request.setAttribute("myShoppingCartItems", myShoppingCartItems);
		return "mall/cart";
	}

	@PostMapping("/shop-cart")
	@ResponseBody
	public Result saveNewBeeShoppingCartItem(@RequestBody MallShoppingCartItem shoppingCartItem,
											 HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		shoppingCartItem.setUserId(user.getUserId());
		String saveResult = mallShoppingCartService.saveMallCartItem(shoppingCartItem);

		//添加成功
		if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
			return ResultGenerator.genSuccessResult();
		}
		//添加失败
		return ResultGenerator.genFailResult(saveResult);
	}

	/**
	 * @Description: 接口的映射地址为 /shop-cart，请求方法为 PUT，该 Controller 中的几个方法路径都是 /shop-cart，
	 * 根据请求方法的不同作为接口的区分方式，POST 方法是新增接口，GET 方法是购物车列表页面显示，PUT 方法是修改接口
	 * @Param: [mallShoppingCartItem, httpSession]
	 * @return: com.coding.util.Result
	 */
	@PutMapping("/shop-cart")
	@ResponseBody
	public Result updateNewBeeMallShoppingCartItem(@RequestBody MallShoppingCartItem mallShoppingCartItem,
												   HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		mallShoppingCartItem.setUserId(user.getUserId());
		String updateResult = mallShoppingCartService.updateMallCartItem(mallShoppingCartItem);

		//修改成功
		if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
			return ResultGenerator.genSuccessResult();
		}
		//修改失败
		return ResultGenerator.genFailResult(updateResult);
	}

	/**
	 * @Description: 如果购物车中一些商品，你不想进行后续的结算购买操作，可以选择将其删除。后端的编辑接口负责接收前端
	 * 的 DELETE 请求并进行处理，接收的参数为 cartItemId 字段，之后调用删除方法即可完成删除操作。
	 * @Param: [mallShoppingCartItemId, httpSession]
	 * @return: com.coding.util.Result
	 */
	@DeleteMapping("/shop-cart/{mallShoppingCartItemId}")
	@ResponseBody
	public Result updateNewBeeMallShoppingCartItem(@PathVariable("mallShoppingCartItemId") Long mallShoppingCartItemId,
												   HttpSession httpSession) {
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		Boolean deleteResult = mallShoppingCartService.deleteById(mallShoppingCartItemId);
		//删除成功
		if (deleteResult) {
			return ResultGenerator.genSuccessResult();
		}
		//删除失败
		return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
	}

	@GetMapping("/shop-cart/settle")
	public String settlePage(HttpServletRequest request,
							 HttpSession httpSession) {
		int priceTotal = 0;
		MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
		List<MallShoppingCartItemVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
		if (CollectionUtils.isEmpty(myShoppingCartItems)) {
			//无数据则不跳转至结算页
			return "/shop-cart";
		} else {
			//总价
			for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
				priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
			}
			if (priceTotal < 1) {
				return "error/error_5xx";
			}
		}
		request.setAttribute("priceTotal", priceTotal);
		request.setAttribute("myShoppingCartItems", myShoppingCartItems);
		return "mall/order-settle";
	}
}
