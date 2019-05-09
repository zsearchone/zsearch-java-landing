# zsearch-java-landing

ZSearch Java客户端的简单示例，包括创建索引、批量写数据、索引查询、删除索引。

## 环境依赖
* JDK : > 1.8+
* Maven : > 3.5.0

## 库依赖
* SpringBoot
* Java High Level REST Client

## 开始

1 修改配置

`vi src/main/java/com/alipay/zsearch/landing/Landing.java`

> 从ZSearch控制台获取服务器地址、用户名及密码。

```java
    final int port = 9999;
    final String server = "zsearch.cloud.alipay.com";
    final String username = "your_username";
    final String password = "your_password";
```


2 运行示例
`mvn clean package`