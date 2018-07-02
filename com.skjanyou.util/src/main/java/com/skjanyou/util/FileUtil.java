package com.skjanyou.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * 编码计算工具
 * getHax可以计算文件md5，sha1
 * getCRC32可以计算文件的CRC值
 * **/
public class FileUtil {

    private FileUtil(){}
    
    private static char[] hexChar = {
        '0','1','2','3','4','5','6','7','8','9',
        'A','B','C','D','E','F'
    };
    
    public static String getMD5(File file){
        InputStream ins = null;
        MessageDigest md5 = null;
        try{
        	ins = new FileInputStream(file);
        	byte[] buffer = new byte[8192];
        	md5 = MessageDigest.getInstance("MD5");
        	
        	int len;
        	while((len = ins.read(buffer)) != -1){
        		md5.update(buffer, 0, len);
        	}
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}finally{
            CommUtil.close(ins);
        }
        
        return toHexString(md5.digest());    	
    }
    
    
    
    
    private static String toHexString(byte[] b){
        StringBuilder sb = new StringBuilder(b.length*2);
        for(int i=0;i<b.length;i++){
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    
    /*
     * 获取MessageDigest支持几种加密算法
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private static String[] getCryptolmpls(String serviceType){
        
        Set result = new HashSet();
//        all providers
        Provider[] providers = Security.getProviders();
        for(int i=0;i<providers.length;i++){
//            get services provided by each provider
            Set keys = providers[i].keySet();
            for(Iterator it = keys.iterator();it.hasNext();){
                String key = it.next().toString();
                key = key.split(" ")[0];
                
                if(key.startsWith(serviceType+".")){
                    result.add(key.substring(serviceType.length()+1));
                }else if(key.startsWith("Alg.Alias."+serviceType+".")){
                    result.add(key.substring(serviceType.length()+11));
                }
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
    
    public static String getCRC32(File file) {  
        CRC32 crc32 = new CRC32();  
        FileInputStream fileinputstream = null;  
        CheckedInputStream checkedinputstream = null;  
        String crc = null;  
        try {  
            fileinputstream = new FileInputStream(file);  
            checkedinputstream = new CheckedInputStream(fileinputstream, crc32);  
            while (checkedinputstream.read() != -1) {  
            }  
            crc = Long.toHexString(crc32.getValue()).toUpperCase();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
        	CommUtil.close(fileinputstream);
        	CommUtil.close(checkedinputstream);
        }  
        return crc;  
    }  
    
}