package com.tingcream.ftpClientPool.core;
 

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * FTP 客户端连接池
 * @author jelly
 *
 */
public class FTPClientPool {
	
	/**
	 * ftp客户端连接池
	 */
    private GenericObjectPool<FTPClient> pool;
 
    /**
     * ftp客户端工厂
     */
    private       FTPClientFactory clientFactory;
    
    
     /**
      * 构造函数中 注入一个bean
      * 
      * @param clientFactory
      */
    public   FTPClientPool(  FTPClientFactory clientFactory){
	    this.clientFactory=clientFactory;
 	    pool = new GenericObjectPool<FTPClient>(clientFactory, clientFactory.getFtpPoolConfig());
	   
     }
     
    
	public FTPClientFactory getClientFactory() {
		return clientFactory;
	}


	public GenericObjectPool<FTPClient> getPool() {
		return pool;
	}


	/**
	 * 借  获取一个连接对象
	 * @return
	 * @throws Exception
	 */
    public FTPClient borrowObject() throws Exception {
    	
    	FTPClient  client =    pool.borrowObject();
    	
//    	if(!client.sendNoOp()){
//    		//使池中的对象无效 
//    		 client.logout();
//    		 client.disconnect();
//    		 pool.invalidateObject(client);
//    		 client =clientFactory.create();
//    		 pool.addObject();
//    	 }
//        
    	return client ;
    
    }
    /**
     * 还   归还一个连接对象
     * @param ftpClient
     */
    public void returnObject(FTPClient ftpClient) {
    	
    	if(ftpClient!=null){
    		pool.returnObject(ftpClient); 
    	}
    }
}