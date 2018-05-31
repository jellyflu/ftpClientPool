package com.tingcream.ftpClientPool.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.tingcream.ftpClientPool.core.FTPClientPool;
import com.tingcream.ftpClientPool.util.ByteUtil;

/**
 * ftp客户端辅助bean  
 * 
 * @author jelly
 *
 */
public class FTPClientHelper {
	

	private FTPClientPool  ftpClientPool;

    public void setFtpClientPool(FTPClientPool ftpClientPool) {
		this.ftpClientPool = ftpClientPool;
	}
	
    /**
     * 下载 remote文件流  
     * @param remote 远程文件
     * @return 字节数据
     * @throws Exception
     */
	public   byte[] retrieveFileStream(String remote) throws Exception {
		FTPClient client=null;
		InputStream in =null;
	    try {
	    	//  long start =System.currentTimeMillis();
	    	   client=	ftpClientPool.borrowObject();
	    	   in=client.retrieveFileStream(remote);
	    	//  long end =System.currentTimeMillis();
	    	 // System.out.println("ftp下载耗时(毫秒):"+(end-start));
	    	return   ByteUtil.inputStreamToByteArray(in);
		 }finally{
			  if (in != null) {  
	                in.close();  
	            } 
            if (!client.completePendingCommand()) {  
            	client.logout();  
            	client.disconnect();  
            	ftpClientPool.getPool().invalidateObject(client);
            } 
			ftpClientPool.returnObject(client);
			 
		}
	}
	
	/**
	 * 创建目录    单个不可递归
	 * @param pathname 目录名称
	 * @return
	 * @throws Exception
	 */
	public   boolean makeDirectory(String pathname) throws Exception {
		
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.makeDirectory(pathname);
		}  finally{
			ftpClientPool.returnObject(client);
		}
	}
	
	/**
	 * 删除目录，单个不可递归
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	public   boolean removeDirectory(String pathname) throws Exception {
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.removeDirectory(pathname);
		} finally{
			ftpClientPool.returnObject(client);
		}
	}
	
	/**
	 * 删除文件 单个 ，不可递归 
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public   boolean deleteFile(String pathname) throws Exception {
		
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.deleteFile(pathname);
		}finally{
			ftpClientPool.returnObject(client);
		}
	}
	
	/**
	 * 上传文件 
	 * @param remote
	 * @param local
	 * @return
	 * @throws Exception
	 */
	public   boolean storeFile(String remote, InputStream local) throws Exception {
		FTPClient client=null;
	    try {
	    	client=	ftpClientPool.borrowObject();
			return  client.storeFile(remote, local);
		} finally{
			ftpClientPool.returnObject(client);
		}
	}
   
}
