package com.coding.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @description: 验证码配置
 * @author: Black Tom
 * @create: 2020-07-07 17:57
 **/
@Component
public class KaptchaConfig {
	@Bean
	public DefaultKaptcha getDefaultKaptcha() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		//图片边框
		properties.put("kaptcha.border", "no");

		//字体颜色、大小
		properties.put("kaptcha.textproducer.font.color", "black");
		properties.put("kaptcha.textproducer.font.size", "30");
		properties.put("kaptcha.textproducer.font.names", "宋体,微软雅黑,楷体");

		//图片宽高,更改此项发现update classes无法及时显示更改后的变化，需要重启应用
		properties.put("kaptcha.image.width", "150");
		properties.put("kaptcha.image.height", "40");
//		properties.put("kaptcha.textproducer.img.width", "150");
//		properties.put("kaptcha.textproducer.img.height", "40");

		//验证码长度
		properties.put("kaptcha.textproducer.char.space", "5");

		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}
}
