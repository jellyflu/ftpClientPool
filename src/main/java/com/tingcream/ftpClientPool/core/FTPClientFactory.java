package com.tingcream.ftpClientPool.core;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

import com.tingcream.ftpClientPool.config.FtpPoolConfig;
 
/**
 * ftpclient 工厂
 * @author jelly
 *
 */
public class FTPClientFactory extends BasePooledObjectFactory<FTPClient> {
	
    private static Logger logger =Logger.getLogger(FTPClientFactory.class);
    
    private FtpPoolConfig ftpPoolConfig;
    
     
   
	public FtpPoolConfig getFtpPoolConfig() {
		return ftpPoolConfig;
	}

	public void setFtpPoolConfig(FtpPoolConfig ftpPoolConfig) {
		this.ftpPoolConfig = ftpPoolConfig;
	}

	/**
     * 新建对象
     */
    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(ftpPoolConfig.getConnectTimeOut());
        try {
        	
        	logger.info("连接ftp服务器:" +ftpPoolConfig.getHost()+":"+ftpPoolConfig.getPort());
        	ftpClient.connect(ftpPoolConfig.getHost(), ftpPoolConfig.getPort());
            
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.error("FTPServer 拒绝连接");
                return null;
            }
            boolean result = ftpClient.login(ftpPoolConfig.getUsername(),ftpPoolConfig.getPassword());
            if (!result) {
                logger.error("ftpClient登录失败!");
                throw new Exception("ftpClient登录失败! userName:"+ ftpPoolConfig.getUsername() + ", password:"
                        + ftpPoolConfig.getPassword());
            }
         
			ftpClient.setControlEncoding(ftpPoolConfig.getControlEncoding());
			ftpClient.setBufferSize(ftpPoolConfig.getBufferSize());
			ftpClient.setFileType(ftpPoolConfig.getFileType());
			ftpClient.setDataTimeout(ftpPoolConfig.getDataTimeout());
			ftpClient.setUseEPSVwithIPv4(ftpPoolConfig.isUseEPSVwithIPv4());
			if(ftpPoolConfig.isPassiveMode()){
				logger.info("进入ftp被动模式");
				ftpClient.enterLocalPassiveMode();//进入被动模式
			}
        } catch (IOException e) {
            logger.error("FTP连接失败：", e);
        }
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<FTPClient>(ftpClient);
    }

    /**
     * 销毁对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        FTPClient ftpClient = p.getObject();
        ftpClient.logout();
        super.destroyObject(p);
    }

    /**
     * 验证对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        boolean connect = false;
        try {
            connect = ftpClient.sendNoOp();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }
    

    /**
     *  No-op.
     *
     *  @param p ignored
     */
    @Override
    public void activateObject(PooledObject<FTPClient> p) throws Exception {
        // The default implementation is a no-op.
    }

    /**
     *  No-op.
     *
     * @param p ignored
     */
    @Override
    public void passivateObject(PooledObject<FTPClient> p)
        throws Exception {
        // The default implementation is a no-op.
    }
}