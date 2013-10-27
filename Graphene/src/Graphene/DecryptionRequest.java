package Graphene;

import com.tiemens.secretshare.engine.SecretShare;
import crypto.EncryptUtil;
import crypto.EncryptedData;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DecryptionRequest {



    public DecryptionRequest(String fileName)
    {
        // Choose k random ip's
        int k = NetworkInfo.NodeIps.size();

        final EncryptedData data = new EncryptedData();
        data.encryptedData = DataStore.getFileContents(fileName);
        data.secretShare = new ArrayList<SecretShare.ShareInfo>();

        DataRequestServer[] requests = new DataRequestServer[k];



        for(int i = 0; i < k; i++) {
            requests[i] = new DataRequestServer(NetworkInfo.NodeIps.get(i), fileName, new IDecryptionCallback() {
                public void DataReceived(SecretShare.ShareInfo share)
                {
                    data.secretShare.add(share);
                }

                public void OnError(String error)
                {
                    System.out.println("Request error" + error);
                }
            });

            requests[i].start();
        }

        //while(data.secretShare.size() != k) { }

        for(int i = 0; i < k; i++) {
            try {
                requests[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        data.secretShare.add(DataStore.Shares.get(fileName));

        try {
            String res = EncryptUtil.decrypt(data);

            System.out.println("Decrypted file: " + res);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
