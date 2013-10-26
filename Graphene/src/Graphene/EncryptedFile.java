package Graphene;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class EncryptedFile {
    public String fileName;
    public String data;

    private BigInteger sig;

    public EncryptedFile(String fileName, String data, BigInteger sig) {
        this.fileName = fileName;
        this.data = data;
        this.sig = sig;
    }

    public BigInteger partialDecryption() {
        return new BigInteger("123123123132");
    }
}
