package com.coding.controller.common;

import com.coding.common.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @description: 前端验证码交互
 * @author: Black Tom
 * @create: 2020-07-07 18:08
 **/
@Controller
public class KaptchaController {
	@Autowired
	private DefaultKaptcha kaptchaProducer;

	/**
	 * @Description: 该方法所拦截处理的路径为 /kaptcha，在前端访问该路径后就可以接收到一个图片流并显示在
	 * 浏览器页面的对应位置上
	 * @Param: [request, response]
	 * @return: void
	 */
	@GetMapping("/common/kaptcha")
	public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] kaptchaOutputStream = null;
		ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
		try {
			//生成验证码并保存在session中
			String verifyCode = kaptchaProducer.createText();
			request.getSession().setAttribute("verifyCode", verifyCode);
			//根据验证码生成图像
			BufferedImage kaptchaProducerImage = kaptchaProducer.createImage(verifyCode);
			//生成输出流
			ImageIO.write(kaptchaProducerImage, "jpg", imgOutputStream);
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		kaptchaOutputStream = imgOutputStream.toByteArray();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pargme", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		//写入二进制文件
		responseOutputStream.write(kaptchaOutputStream);
		//强制刷新，将缓冲中的数据写入
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	@GetMapping("/verify")
	@ResponseBody
	public String verify(@RequestParam("code") String code, HttpSession session) {
		if (StringUtils.isEmpty(code)) {
			return "验证码不能为空";
		}
		String kaptchaCode = session.getAttribute("verifyCode") + "";
		if (StringUtils.isEmpty(kaptchaCode) || !code.equals(kaptchaCode)) {
			return "验证码错误";
		}
		return "验证成功";
	}

	@GetMapping("/common/mall/kaptcha")
	public void mallKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		com.google.code.kaptcha.impl.DefaultKaptcha newBeeMallLoginKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
		Properties properties = new Properties();
		properties.put("kaptcha.border", "no");
		properties.put("kaptcha.textproducer.font.color", "27,174,171");
		properties.put("kaptcha.noise.color", "20,33,42");
		properties.put("kaptcha.textproducer.font.size", "30");
		properties.put("kaptcha.image.width", "110");
		properties.put("kaptcha.image.height", "40");
		properties.put("kaptcha.session.key", Constants.MALL_VERIFY_CODE_KEY);
		properties.put("kaptcha.textproducer.char.space", "2");
		properties.put("kaptcha.textproducer.char.length", "6");
		Config config = new Config(properties);
		newBeeMallLoginKaptcha.setConfig(config);
		byte[] captchaOutputStream = null;
		ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
		try {
			//生产验证码字符串并保存到session中
			String verifyCode = newBeeMallLoginKaptcha.createText();
			httpServletRequest.getSession().setAttribute(Constants.MALL_VERIFY_CODE_KEY, verifyCode);
			BufferedImage challenge = newBeeMallLoginKaptcha.createImage(verifyCode);
			ImageIO.write(challenge, "jpg", imgOutputStream);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		captchaOutputStream = imgOutputStream.toByteArray();
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaOutputStream);
		responseOutputStream.flush();
		responseOutputStream.close();
	}
}
