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

`vi src/main/resources/application.properties`

> 服务器地址、用户名及密码从ZSearch控制台获取.

```
server = http://zsearch.cloud.alipay.com:9999
username = youre_username
password = youre_password
```

2 运行示例
`mvn clean package`