package com.coding.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 配置mapper扫描位置
 * @author: Black Tom
 * @create: 2020-07-09 14:03
 **/
@Configuration
@MapperScan("com.coding.mapper")
public class MybatisPlusConfig {

}