/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import com.tiemens.secretshare.engine.SecretShare;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author alesis
 */
public class EncryptedData {
    public List<SecretShare.ShareInfo> secretShare;
    public byte[] encryptedData;
}
