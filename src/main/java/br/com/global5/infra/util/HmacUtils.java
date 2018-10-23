package br.com.global5.infra.util;

/**
 * Copyright (c) 2014, Diversity Arrays Technology, All rights reserved. 
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Class the can do HMAC_SHA1 that is compatible with DAL.
 * The method that is useful outside this class is hmacSha1Hex
 * @author puthick
 */
public class HmacUtils {

    /**
     * @param args the command line arguments
     */
    public static String hex(byte[] data) {
      
        StringBuilder sb = new StringBuilder();

        for (byte b : data) {
            sb.append(Character.forDigit((b & 240) >> 4, 16));
            sb.append(Character.forDigit((b & 15), 16));
        }    

        return sb.toString();
    }
    
    /*
     * cryptValue : message that you want to hash.
     * cryptKey   : secret key
     * 
     * return     : digest hexadecimal string like '39e30fb2ddfcf32b9dd663039ca6e847b698aa6f'
     */
    
    public static String hmacSha1Hex(String cryptValue, String cryptKey) {
    	    	
        String digesthex = "";
        //String digestB64 = "";
        try {

        Mac sha = Mac.getInstance("HmacSHA1");
        sha.init(new SecretKeySpec(cryptKey.getBytes("UTF8"), "HmacSHA1"));
        
        
        byte[] utf8 = cryptValue.getBytes("UTF8");
        byte[] digest = sha.doFinal(utf8);
        

        digesthex = hex(digest);

        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
    	return digesthex;
    }
    
}   