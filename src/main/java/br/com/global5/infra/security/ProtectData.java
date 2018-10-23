package br.com.global5.infra.security;

import br.com.global5.infra.model.Criteria;
import br.com.global5.infra.util.AppUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ProtectData {

    private static byte[] compress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return str.getBytes();
        }
        ByteArrayOutputStream obj=new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = obj.toString();
        return obj.toByteArray();
    }

    private static String decompress(byte[] str) throws Exception {
        if (str == null || new String(str).length() == 0) {
            return new String(str);
        }
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
            outStr += line;
        }
        return outStr;
    }

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Criteria crit = new Criteria(5,30,0,0,0,
                                         0,0,0,0,
                                         0,0,0,0,null);
            String strKey = "LPidnkDSUMA8CFNm";
            String resultado = encrypt(mapper.writeValueAsString(crit),strKey);
            System.out.println("Codificado   : "+ resultado);


            System.out.println("Decodificado : "+ decrypt(resultado,strKey));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strClearText,String strKey) throws Exception{
        String strData="";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(strClearText.getBytes());
            strData= AppUtils.bytesToHex(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return DatatypeConverter.printBase64Binary(compress(strData));
    }

    public static String decrypt(String strEncrypted,String strKey) throws Exception{
        String strData="";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            // AppUtils.HexToBytes(new String(DatatypeConverter.parseBase64Binary(decompress(strEncrypted.getBytes()))))
            byte[] decrypted=cipher.doFinal(AppUtils.HexToBytes(decompress(DatatypeConverter.parseBase64Binary(strEncrypted))));
            strData=new String(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return strData;
    }


}