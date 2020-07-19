package com.coding;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:
 * @author: Black Tom
 * @create: 2020-07-06 19:16
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlConnectivityTest {
	@Autowired
	private DataSource defaultDataSource;

	@Test
	public void dataSourceTest() throws SQLException {
		Connection connection = defaultDataSource.getConnection();
		// 判断连接对象是否为空
		log.info("获取连接：{}",connection != null);
		// 获取数据源类型
		log.info("默认数据源为：{}", defaultDataSource.getClass());
		connection.close();
	}
}
