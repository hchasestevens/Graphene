package Graphene;

import java.math.BigInteger;

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
        // Chase's ip is 129.215.59.42 BULLSHIT
        (new DataRequestServer(NetworkInfo.NodeIps.get(0), fileName, new IDecryptionCallback() {
            public void DataReceived(BigInteger data)
            {

            }
        })).run();
    }


}
