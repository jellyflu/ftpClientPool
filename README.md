# ftpClientPool
此ftp客户端连接池底层依赖apache的commons-net和commons-pool2，基于这二者做的个薄层封装，让我们在spring项目中以更简洁、高效的方式使用ftpClient

# 如何使用
1、下载jar包 ftpClientPool-xx.jar(我用maven编译好的jar包)到你的工程lib中，或者下载此项目源码后在本地编译亦可。
     
2、在你的spring主配置文件中引入spring-ftpClientPool.xml配置文件
	    示例如下：   
      
	 <import resource="classpath:spring-ftpClientPool.xml"/>
   
   spring-ftpClientPool.xml 配置如下：
   
   ```
   <?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
    xmlns:cache="http://www.springframework.org/schema/cache"
	xsi:schemaLocation="
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context-4.3.xsd
          http://www.springframework.org/schema/aop
          http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
          http://www.springframework.org/schema/tx
          http://www.springframework.org/schema/tx/spring-tx-4.3.xsd
          http://www.springframework.org/schema/cache
          http://www.springframework.org/schema/cache/spring-cache-4.3.xsd">
    
    <beans>
      <!-- ftp连接池配置参数 -->
     <bean id="ftpPoolConfig" class="com.tingcream.ftpClientPool.config.FtpPoolConfig">
           <property name="host" value="${ftp.host}"/>
           <property name="port" value="${ftp.port}"/>
           <property name="username" value="${ftp.username}"/>
           <property name="password" value="${ftp.password}"/>
           
           <property name="connectTimeOut" value="${ftp.connectTimeOut}"/>
           <property name="controlEncoding" value="${ftp.controlEncoding}"/>
           <property name="bufferSize" value="${ftp.bufferSize}"/>
           <property name="fileType" value="${ftp.fileType}"/>
           <property name="dataTimeout" value="${ftp.dataTimeout}"/>
           <property name="useEPSVwithIPv4" value="${ftp.useEPSVwithIPv4}"/>
           <property name="passiveMode" value="${ftp.passiveMode}"/>
           
           <property name="blockWhenExhausted" value="${ftp.blockWhenExhausted}"/>
           <property name="maxWaitMillis" value="${ftp.maxWaitMillis}"/>
           <property name="maxTotal" value="${ftp.maxTotal}"/>
           <property name="maxIdle" value="${ftp.maxIdle}"/>
           <property name="testOnBorrow" value="${ftp.testOnBorrow}"/>
           <property name="testOnReturn" value="${ftp.testOnReturn}"/>
           <property name="testOnCreate" value="${ftp.testOnCreate}"/>
           <property name="testWhileIdle" value="${ftp.testWhileIdle}"/>
     </bean>
      <!-- ftp客户端工厂 -->
     <bean id="ftpClientFactory" class="com.tingcream.ftpClientPool.core.FTPClientFactory">
          <property name="ftpPoolConfig" ref="ftpPoolConfig"></property>
     </bean>
     
     <!-- ftp客户端连接池对象 -->
     <bean id="ftpClientPool" class="com.tingcream.ftpClientPool.core.FTPClientPool">
         <constructor-arg index="0" ref="ftpClientFactory"></constructor-arg>
     </bean> 
    
     <!-- ftp客户端辅助bean-->
      <bean id="ftpClientHelper" class="com.tingcream.ftpClientPool.client.FTPClientHelper">
           <property name="ftpClientPool"  ref="ftpClientPool"/>
      </bean>
     
</beans>
 ```
  
  
 
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