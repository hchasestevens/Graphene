/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import com.tiemens.secretshare.engine.SecretShare;
import java.util.List;

/**
 *
 * @author alesis
 */
public class EncryptedData {
    List<SecretShare.ShareInfo> secretShare;
    byte[] encryptedData;
}
