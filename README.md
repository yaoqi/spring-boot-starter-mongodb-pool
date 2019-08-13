# spring-boot-starter-mongodb-pool

在日常工作中，我们通过Spring Data Mongodb来操作Mongodb数据库，在Spring Boot中只需要引入spring-boot-starter-data-mongodb即可。

很多时候我们往往需要操作多个数据库（微服务架构下一个服务一个独立的库），最简单的方式就是在项目中为每个数据库配置下，比如：

```
@Bean 
@Primary
public MongoDbFactory mongoDbFactory() throws Exception {
    // 省略...
   return new SimpleMongoDbFactory(mongoClient, "db1");
}

@Bean
@Primary
public MongoTemplate mongoTemplate() throws Exception {
	  //省略...
	  MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
	  return mongoTemplate;
}

@Bean 
@Qualifier("mongoDbFactory2")
public MongoDbFactory mongoDbFactory2()hrows Exception {
    // 省略...
   return new SimpleMongoDbFactory(mongoClient, "db2);
}

@Bean
@Qualifier("mongoTemplate2")
public MongoTemplate mongoTemplate2() hrows Exception {
    //省略...
    MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
    return mongoTemplate;
}
```

配置完成后注入使用的地方使用Qualifier注入即可使用对应的数据源，比如：

```
@Autowired
@Qualifier("mongoTemplate2")
private MongoTemplate seaweedMongoTemplate;
```

上面是直接在项目中配置多数据源的方式，接下来我们讲下一个问题，数据库连接池的问题，Mongodb的驱动中自带了连接池，但是Spring Boot Starter中却没有对连接池的配置，往往我们也需要通过代码的方式去配置，比如：

```
 @Bean
 public MongoClientOptions mongoClientOptions() {
     return new MongoClientOptions.Builder()
          //省略...
          .minConnectionsPerHost(...).build();
 }
```

虽然上面这些方式也都比较简单，但是在多个项目使用的时候都要去配置，重复代码比较严重，既然作为Starter按理来说是越简单越好，直接通过在属性文件中
配置就可以搞定，但是却不行，为此我封装了这个Pool Starter。

## 主要功能

- 目前最新的jar包只支持Spring Boot 2.0, Spring Boot 1.0版本的可以下载releases中的1.0.0版本的源码进行修改
- 可以配置多个数据源
- 支持连接池参数配置
- 支持去掉_class的配置
- **支持uri配置连接池信息

## 不支持的功能

- 多数据源配置后不支持Repository接口方式的使用

## 编译
git clone 后, mvn clean install到本地仓库，或upload到nexus私服。

## Maven依赖

```
<dependency>
	<groupId>com.github.yinjihuan</groupId>
	<artifactId>spring-boot-starter-mongodb-pool</artifactId>
	<version>2.0.2</version>
</dependency>
```

## 配置方式

配置采用spring.data.mongodb.mongoTemplate名称.属性名=值

```
spring.data.mongodb.testMongoTemplate.host=localhost
spring.data.mongodb.testMongoTemplate.port=27017
spring.data.mongodb.testMongoTemplate.database=backup
spring.data.mongodb.testMongoTemplate.showClass=false
spring.data.mongodb.testMongoTemplate.gridFsTemplateName=gridFsTemplate

spring.data.mongodb.logsMongoTemplate.host=localhost
spring.data.mongodb.logsMongoTemplate.port=27017
spring.data.mongodb.logsMongoTemplate.database=logs
spring.data.mongodb.logsMongoTemplate.gridFsTemplateName=logsGridFsTemplate
```

带认证的集群配置方式：

```
spring.data.mongodb.testMongoTemplate.uri=localhost:27017,localhost:27018
spring.data.mongodb.testMongoTemplate.username=yinjihuan
spring.data.mongodb.testMongoTemplate.password=123456
spring.data.mongodb.testMongoTemplate.database=backup
spring.data.mongodb.testMongoTemplate.showClass=false
```
Uri配置方式:
spring.data.mongodb.testMongoTemplate.uri=mongodb://dean:dean001@localhost:3717,localhost:3718/admin?replicaSet=rs001
spring.data.mongodb.testMongoTemplate.repositories.type=auto
```

testMongoTemplate和logsMongoTemplate就是每个数据源对应的MongoTemplate

同样的如果我们要操作GridFS的话也可以通过gridFsTemplateName属性来配置gridFsTemplate的名称

配置完之后就可以使用了，使用代码如下：

```
@Autowired
@Qualifier("testMongoTemplate")
private MongoTemplate testMongoTemplate;
	
@Autowired
@Qualifier("logsMongoTemplate")
private MongoTemplate logsMongoTemplate;
```

完整的使用案列请参考：https://github.com/yinjihuan/spring-boot-starter-mongodb-pool/tree/master/spring-boot-starter-mongodb-pool-example

完整的属性配置请参考：https://github.com/yinjihuan/spring-boot-starter-mongodb-pool/blob/master/spring-boot-starter-mongodb-pool/src/main/java/com/cxytiandi/mongodb/MongoPoolProperties.java

```
@Data
public class MongoPoolProperties {
    private String mongoTemplateName = "mongoTemplate";
	
    private String gridFsTemplateName = "gridFsTemplate";
	
    /**
     * 存储时是否保存_class
     */
    private boolean showClass = true;
    private String host;
    private Integer port = 27017;
    private String database;
    private String authenticationDatabase;
    private String gridFsDatabase;
    private String username;
    private char[] password;

    private int minConnectionsPerHost;
    private int maxConnectionsPerHost = 100;
    private int threadsAllowedToBlockForConnectionMultiplier = 5;
    private int serverSelectionTimeout = 1000 * 30;
    private int maxWaitTime = 1000 * 60 * 2;
    private int maxConnectionIdleTime;
    private int maxConnectionLifeTime;
    private int connectTimeout = 1000 * 10;
    private int socketTimeout = 0;
    private boolean socketKeepAlive = false;
    private boolean sslEnabled = false;
    private boolean sslInvalidHostNameAllowed = false;
    private boolean alwaysUseMBeans = false;

    private int heartbeatFrequency = 10000;
    private int minHeartbeatFrequency = 500;
    private int heartbeatConnectTimeout = 20000;
    private int heartbeatSocketTimeout = 20000;
    private int localThreshold = 15;
}
```

# 去掉_class
 Spring Data Mongodb在保存数据的时候会自动增加一个_class字段用来关联这条数据的Document实体类，大数据量下会浪费存储空间，我们可以通过下面的配置禁用掉：
 
 ```
 spring.data.mongodb.testMongoTemplate.showClass=false
 ```

# 作者
- 尹吉欢 1304489315@qq.com

# Uri配置支持by Dean
