## 做过的Demo

### 秒杀

最近看了Java并发相关的书，为了练手，却发现这个demo并未用到一些线程池及锁相关的东西，更像是让你对秒杀有一个基本的一个了解，如果对SpringBoot不熟悉，也可以试试该项目，具体介绍可以参见[项目文档](https://github.com/BlackTomm/project/blob/master/seckill-SpringBoot/README.md)。

#### 后端

类似于以下架构，但缺少秒杀交易系统集群与支付体系，本项目可以对秒杀系统做一个简单地了解，关键点是使用Redis做了一个缓冲层，减少了对MySQL表的IO次数。

<img src="https://static001.geekbang.org/resource/image/ba/3d/ba65c2b4e2a2bae28192e1d456131f3d.jpg" width="500px" />

​											图片来源：[01 | 设计秒杀系统时应该注意的5个架构原则](https://time.geekbang.org/column/article/40726)