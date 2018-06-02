# ftpClientPool
此ftp客户端连接池底层依赖apache的commons-net和commons-pool2，基于这二者做的个薄层封装，让我们在spring项目中以更简洁、高效的方式使用ftpClient

# 如何使用
1、下载jar包 ftpClientPool-xx.jar(我用maven编译好的jar包)到你的工程lib中，或者下载此项目源码后在本地编译亦可。
     
2、在你的spring主配置文件中引入spring-ftpClientPool.xml配置文件
	    示例如下：   
      
	 <import resource="classpath:spring-ftpClientPool.xml"/>
	 
 
3、在你的 spring主配置文件中新载入一个ftpPoolConfig.properties配置
	    示例如下：
      
	   <bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"> 
		  <property name="locations"> 
		    <list> 
		      <value>classpath:jdbc.properties</value> 
		      <value>classpath:redis.properties</value> 
		       <!--  更多项  -->
		       
		       <!--注意新加入此项  -->
		      <value>classpath:ftpPoolConfig.properties</value> 
		      
		    </list> 
		  </property> 
		</bean>
4、在你的工程classpath中新加ftpPoolConfig.properties属性配置文件
示例： 

```
ftp.host=127.0.0.1
ftp.port=21
ftp.username=ftptest
ftp.password=123456

 
#ftp 连接超时时间 毫秒
ftp.connectTimeOut=5000
#控制连接  字符编码
ftp.controlEncoding=UTF-8
#缓冲区大小
ftp.bufferSize=1024
#传输文件类型   2表二进制
ftp.fileType=2
#数据传输超时 毫秒
ftp.dataTimeout=120000
#
ftp.useEPSVwithIPv4=false
#是否启用ftp被动模式
ftp.passiveMode=true


#连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
ftp.blockWhenExhausted=true
#最大空闲等待时间  毫秒
ftp.maxWaitMillis=3000
#最大连接数
ftp.maxTotal=10
#最大空闲连接数
ftp.maxIdle=10
#最小空闲连接数
ftp.minIdle=2
#申请连接时 检测是否有效
ftp.testOnBorrow=true
#返回连接时 检测是否有效
ftp.testOnReturn=true
#创建连接时 检测是否有效
ftp.testOnCreate=true
#空闲时检测连接是否有效  
ftp.testWhileIdle=true
```

5、在你的spring工程中如何使用此模块(ftpClientPool)

	    示例1:           
	    @Resource  
	    private  FTPClientHelper  ftpClientHelper ;// 注入ftp客户端helper对象使用
      
	  示例2:    
	    @Resource
	    private   FTPClientPool  ftpClientPool;// 注入 ftp客户端连接池对象使用
	  示例3：     你也可以同时注入上面二者