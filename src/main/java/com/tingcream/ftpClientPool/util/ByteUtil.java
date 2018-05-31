package com.tingcream.ftpClientPool.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 字节工具类
 * @author jelly
 *
 */
public class ByteUtil {
	
	 public static byte[] inputStreamToByteArray(InputStream in)  {
		 try {
			 ByteArrayOutputStream out=new ByteArrayOutputStream();
		        byte[] buffer=new byte[1024*4];
		        int n=0;
		        while ( (n=in.read(buffer)) >0) {
		            out.write(buffer,0,n);
		        }
		      return  out.toByteArray();
		       
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	 }

}
