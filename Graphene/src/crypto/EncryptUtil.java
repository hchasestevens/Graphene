/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import com.tiemens.secretshare.engine.SecretShare;
import com.tiemens.secretshare.engine.SecretShare.ShareInfo;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author alesis
 */
public class EncryptUtil {

    public static EncryptedData encrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] dataArray = data.getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey key = kgen.generateKey();
        
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = c.getIV();
        if (iv[0] < 0) {
            byte[] zero = new byte[1];
            zero[0] = 0;
            iv = ArrayUtils.addAll(zero, iv);
        }
        byte[] encrypted = c.doFinal(dataArray);
        BigInteger keyAndIv = new BigInteger(ArrayUtils.addAll(iv, key.getEncoded()));
        
        SecretShare.PublicInfo publicInfo = new SecretShare.PublicInfo(3, 2, SecretShare.createAppropriateModulusForSecret(keyAndIv), "");
        
        SecretShare secretShare = new SecretShare(publicInfo);
        SecretShare.SplitSecretOutput sso = secretShare.split(keyAndIv);
        List<ShareInfo> lsi = sso.getShareInfos();
        
        EncryptedData fd = new EncryptedData();
        fd.encryptedData = encrypted;
        fd.secretShare = lsi;
        
        return fd;
        
    }
    
    public static String decrypt(EncryptedData encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretShare secretShare = new SecretShare(encryptedData.secretShare.get(0).getPublicInfo());
        BigInteger combined = secretShare.combineParanoid(encryptedData.secretShare, null, null);
        
        byte[] combinedBytes = combined.toByteArray();
        if (combinedBytes.length > 32) {
            combinedBytes = Arrays.copyOfRange(combinedBytes, 1, 33);
        }
        SecretKey key = new SecretKeySpec(Arrays.copyOfRange(combinedBytes, 16, 32), 0, 16, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Arrays.copyOfRange(combinedBytes, 0, 16)));
        byte[] decrypted = c.doFinal(encryptedData.encryptedData);
        
        return new String(decrypted);
    }
}
