## 基于SpringBoot实现的Java秒杀系统

### 技术栈

- 前端：Bootstrap + Jquery
- 后端：SpringBoot-2.3.1.RELEASE  +  Redis-5.0.9 +  MySQL 5.7.19

### 测试环境

- IDEA + Maven-3.6.2 + Tomcat 9.0.36 + JDK8

### 启动说明

- 启动前，请配置好 [application.yml](https://github.com/TyCoding/springboot-seckill/blob/master/src/main/resources/application.yml) 中连接数据库的用户名和密码，以及Redis服务器的地址和端口信息
- 启动前，请创建数据库`seckill`，建表SQL语句放在：`/sqlSchema/schema.sql`。具体的建表和建库语句请仔细看SQL文件。
- 配置完成后，运行位于 `src/main/com/seckill/`下的SpringbootSeckillApplication中的main方法，访问 `http://localhost:8080/seckill/` 进行API测试。
- 注意`/sqlSchema/schema.sql`中秒杀商品的日期可能要修改，自行修改为符合商品秒杀条件的时间即可,目前此表仅有最后一件商品可秒杀成功。

### 项目参考

#### 慕课视频

1. [Java高并发秒杀API之业务分析与DAO层](http://www.imooc.com/learn/587)
2. [Java高并发秒杀API之Service层](http://www.imooc.com/learn/631)
3. [Java高并发秒杀API之web层](http://www.imooc.com/learn/630)
4. [Java高并发秒杀API之高并发优化](http://www.imooc.com/learn/632)

#### 相关代码

1. 原始代码：https://github.com/miracle678067/seckill
2. 基于SpringBoot+thymeleaf 实现 https://github.com/TyCoding/springboot-seckill，此项目的SpringBoot版本主要参考了她的实现，该作者也写了相关的博客，可以作为参考。

### 项目设计

```
├─.idea
│  ├─dataSources
│  └─inspectionProfiles
├─.mvn
│  └─wrapper
├── pom.xml  -- 项目依赖
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─seckill
    │  │          ├─dto	-- 统一封装的一些结果属性，和entity类似
    │  │          ├─entity	-- 实体类
    │  │          ├─enums	-- 手动定义的字典枚举参数
    │  │          ├─exception	-- 统一的异常结果
    │  │          ├─mapper	-- Mybatis-Mapper层映射接口，或称为DAO层
    │  │          ├─redis	-- redis,jedis 相关配置
    │  │          ├─service	-- 业务层,利用mapper提供的接口实现业务逻辑
    │  │          └─web	-- MVC的web层
    │  └─resources
    │      ├─mapper	-- Mybatis-Mapper层XML映射文件
    │      ├─sqlSchema	--需要在项目执行前，在MySQL控制台插入的表，按顺序执行即可
    │      ├─static	-- 存放页面静态资源，可通过浏览器直接访问
    │      │  ├─css
    │      │  ├─fonts
    │      │  └─js
    │      └─templates
    │          └─thymeleaf	-- 存放Thymeleaf模板引擎所需的HTML，不能在浏览器直接访问
    └─test	-- 测试文件
```



### 基本框架

#### 后端

类似于以下架构，但缺少秒杀交易系统集群与支付体系，本项目可以对秒杀系统做一个简单地了解，关键点是使用Redis做了一个缓冲层，减少了对MySQL表的IO次数。

<img src="https://static001.geekbang.org/resource/image/ba/3d/ba65c2b4e2a2bae28192e1d456131f3d.jpg" style="zoom: 50%;" />

​											图片来源：[01 | 设计秒杀系统时应该注意的5个架构原则](https://time.geekbang.org/column/article/40726)

#### 前端交互

**主要页面：**

<div align=center>
<img src="https://blog-field-1258773891.cos.ap-beijing.myqcloud.com/my-blog/2020/06/seckill/front_end_design-flow.png" width="250px" />
</div>



**详情页：**

<div align=center>
<img src="https://blog-field-1258773891.cos.ap-beijing.myqcloud.com/my-blog/2020/06/seckill/front-end_design_detail_flow.png" width="600px" />
</div>





### 项目预览

#### 商品列表页

![](https://blog-field-1258773891.cos.ap-beijing.myqcloud.com/my-blog/2020/06/seckill/product-list.png)

#### 秒杀倒计时页面

![](https://blog-field-1258773891.cos.ap-beijing.myqcloud.com/my-blog/2020/06/seckill/seckill-countdown.png)

#### 开始秒杀页面

![](https://blog-field-1258773891.cos.ap-beijing.myqcloud.com/my-blog/2020/06/seckill/seckill-start.png)

### 项目搭建过程中遇到的问题

搭建过程中也遇到了一些问题，按照搭建模块顺序做了以下总结：

#### DAO层开发

1. 由于MySQL 5.7.19默认一张表只能有一个字段包含timestamp，导致无法成功创建秒杀seckill表

   ```mysql
   --创建秒杀商品库存表
   CREATE TABLE seckill(
   `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
   `name` varchar (120) NOT NULL COMMENT '商品名称',
   `number` int NOT NULL COMMENT '库存数量',
   `start_time` timestamp NOT NULL COMMENT '秒杀开启时间',
   `end_time` timestamp  NOT NULL COMMENT '秒杀结束时间',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   primary key (seckill_id),
   key idx_start_time(start_time),
   key idx_end_time(end_time),
     key idx_create_time(create_time)
   )ENGINE=InnoDB AUTO_INCREMENT= 1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
   ```

   解决办法： 设置sql_mode，sql_mode分为全局模式与会话模式，根据客户端差异，设置不同的sql_mode。

   ```mysql
   show variables like 'sql_mode';		//查看sql_mode
   set sql_mode = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';		//去除掉部分sql_mode值，可以再次查询确认
   ```

   **参考来源：**

   - 解决办法参考自：[ERROR 1067 (42000) : Invalid default value for 'LAST_UPDATED'](https://dba.stackexchange.com/questions/152513/error-1067-42000-at-line-7-invalid-default-value-for-last-updated)
   - MySQL文档：[5.1.10 Server SQL Modes](https://dev.mysql.com/doc/refman/5.7/en/sql-mode.html)

2. MySQL连接不上，建议使用5.xx版本，8.xx版本还未连接成功，并对数据库连接URL做时区相关配置

   ```mysql
   #基本属性,seckill可以替换成实际创建的数据库
   url: jdbc:mysql://127.0.0.1:3306/seckill?serverTimezone=Asia/Shanghai&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull
   ```



#### web层开发

1. 主要遇到了前端页面不显示倒计时与开始秒杀按钮，后改用SpringBoot+thymeleaf结合，显示正常，未查找到具体原因，如果遇到问题建议先查看慕课视频下的评论及别人写的github项目，有很多类似的。

   首先需要确保前端能够接收到保存在model内的attribute参数对应的seckill值，用于detail页面展示。目前看到了三种写法可以确保前端接收到了seckill

   - 针对JSP编写的detail.jsp页脚添加的JavaScript代码

     ```javascript
     <script type="text/javascript">
         $(function () {
             //使用EL表达式传入参数
             seckill.detail.init({
                 seckillId:${seckill.seckillId},
                 startTime:${seckill.startTime.time},//毫秒
                 endTime:${seckill.endTime.time}
             });
         })
     </script>
     ```

   - 针对thymeleaf编写的detail.html

     ```javascript
     <script>
         $(function () {
             seckill.detail.init({
                 /*html下这种写法可以获取到值 https://blog.csdn.net/qq_29072049/article/details/79865530
                 * seckillId: '${seckill.seckillId}'下发无效，
                 * jsp下为 seckillId:${seckill.seckillId} */
                 seckillId: '[[${seckill.seckillId}]]',
                 startTime: '[[${seckill.startTime.time}]]',
                 endTime: '[[${seckill.endTime.time}]]'     
             });
         })
     </script>
     ```

   - 针对thymeleaf编写的另一种detail.html版本，相比之下由于添加了th:inline="javascript"导致相比前一种 `seckillId: '[[${seckill.seckillId}]]'`少了引号，如果随意写会导致前端无法获取数据。

     ```
     <script th:inline="javascript">
        $(function () {
             seckill.detail.init({
                 seckillId: [[${seckill.seckillId}]],
                 startTime: [[${seckill.startTime.time}]],//毫秒
                 endTime: [[${seckill.endTime.time}]]
             });
         })
     </script>
     ```

     通过debug断点调试，可以确保前端获取到了数据。具体参考 [如何调试 JavaScript 脚本](https://wiki.jikexueyuan.com/project/chrome-devtools/debugging-javascript.html)，当刷新页面后看到Scope出获取到值可以确保前端得到了seckill对象值

     ![](https://blog-field-1258773891.cos.ap-beijing.myqcloud.com/my-blog/2020/06/seckill/chrome-debug.png)