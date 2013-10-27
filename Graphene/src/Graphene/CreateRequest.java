package Graphene;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import com.tiemens.secretshare.engine.SecretShare;
import crypto.EncryptUtil;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateRequest {
    private String fileName;
    private byte[] data;
    private List<SecretShare.ShareInfo> shares;

    public CreateRequest(String fileName, byte[] data, List<SecretShare.ShareInfo> shares) {
        this.fileName = fileName;
        this.data = data;
        this.shares = shares;
    }

    public void run() {
        // Notify all servers of the new file

        for(int i = 1; i < shares.size(); i++) {
            String serverIp = NetworkInfo.NodeIps.get(i - 1);
            this.notifyServer(serverIp, shares.get(i));
        }
    }

    private void notifyServer(String serverIp, SecretShare.ShareInfo share) {
        Socket socket = null;
        DataOutputStream out = null;

        try {
            socket = new Socket(serverIp, IncomingRequestServer.PORT);
            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("Requesting creation of file " + this.fileName + " to " + serverIp);
            
            //this.data = RSA.encrypt_outgoing(serverIp, this.data);

            out.writeUTF("create");
            out.writeUTF(this.fileName);

            out.writeInt(data.length);
            out.write(data);

            byte[] shareData = EncryptUtil.shareToByteArray(share);
            out.writeInt(shareData.length);
            out.write(shareData);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOProblem: " + e.getMessage());
        } catch (Exception e) {
			// TODO Auto-generated catch block //RSA bullshit
			e.printStackTrace();
		}
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
