package com.skjanyou.util;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public final class DESUtil {

    //加密算是是des
    private static final String ALGORITHM = "DES";
    //转换格式
    private static final String TRANSFORMATION = "DES/ECB/PKCS5Padding";

    //利用8个字节64位的key给src加密
    public static byte[] encrypt(byte[] src,byte[]key)
    {
        try {
            //加密
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec keySpec = new DESKeySpec(key);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new SecureRandom());
            byte[] enMsgBytes = cipher.doFinal(src);    
            return enMsgBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //利用8个字节64位的key给src解密
    public static byte[] decrypt(byte[] encryptBytes,byte[]key){
        try {
            //解密
            //Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            Cipher deCipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeyFactory deDecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec deKeySpec = new DESKeySpec(key);
            SecretKey deSecretKey = deDecretKeyFactory.generateSecret(deKeySpec);
            deCipher.init(Cipher.DECRYPT_MODE, deSecretKey,new SecureRandom());
            byte[] deMsgBytes = deCipher.doFinal(encryptBytes);
            return deMsgBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) throws Exception{
    	String key = "skjanyou";
        String msg = "hello world. 你好，DES";
        System.out.println("加密前："+msg);
        byte[] encryptBytes = encrypt(msg.getBytes(),key.getBytes());
        System.out.println("加密后："+new String(encryptBytes));
        byte[] deMsgBytes = decrypt(encryptBytes,key.getBytes());
        System.out.println("解密后："+new String(deMsgBytes));
    }
}