package Graphene;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDecryptionCallback {
    public void DataReceived(BigInteger data);
    public void OnError(String message);
}
