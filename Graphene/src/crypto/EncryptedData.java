/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import com.tiemens.secretshare.engine.SecretShare;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author alesis
 */
public class EncryptedData {
    public List<SecretShare.ShareInfo> secretShare;
    public byte[] encryptedData;
}

class SerializableShare implements Serializable {
    SecretShare.ShareInfo share;
    public SerializableShare(SecretShare.ShareInfo share) {
        this.share = share;
    }
    
    SecretShare.ShareInfo getShareInfo() {
        return share;
    }
}