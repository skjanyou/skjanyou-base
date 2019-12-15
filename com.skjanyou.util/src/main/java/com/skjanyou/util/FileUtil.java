package com.skjanyou.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    
    
    /**
     * 复制文件
     * @param src    源文件
     * @param dest   目的文件
     */
    public static void copy(File src,File dest)  {
    	FileInputStream fis = null;
    	FileOutputStream fos = null;
    	try {
			fis = new FileInputStream(src);
			fos = new FileOutputStream(dest);
			int len = -1;
			byte[] buff = new byte[ 1024 ];
			while( (len = fis.read(buff)) != -1 ){
				fos.write(buff, 0, len);
			}
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(fos);
			CommUtil.close(fis);
		}
    }
    
    /**
     * 用于复杂的路径层级创建文件 </br>
     * 例如: /opt/home, skjanyou, data ,结果为/opt/home/skjanyou/data </br> 
     * @param filePath 第一个元素为根目录
     * @return
     */
    public static File createNewFile( String... filePath ){
    	File file = new File( filePath[0] );
    	for( int i = 1; i < filePath.length;i++ ){
    		file = new File(file,filePath[i]);
    	}
    	return file;
    }
    
    public static byte[] getFileBytes( File file ){
    	byte[] result = null;
    	InputStream is = null;
    	ByteArrayOutputStream baos = null;
    	try {
			is = StreamUtil.getFileInputStream(file);
			baos = new ByteArrayOutputStream(is.available());
			int len = -1;byte[] buff = new byte[ 4 * 1024 ];
			while( (len = is.read(buff)) != -1 ){
				baos.write(buff, 0, len);
			}
			baos.flush();
			result = baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			CommUtil.close(baos);
			CommUtil.close(is);
		}
    	
    	return result;
    }
    
}