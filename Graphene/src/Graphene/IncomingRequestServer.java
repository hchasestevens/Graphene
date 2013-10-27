package Graphene;

import com.tiemens.secretshare.engine.SecretShare;
import crypto.EncryptUtil;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class IncomingRequestServer extends Thread {
    public static final int PORT = 8050;

    public boolean isRunning = true;

    public void run() {
        String str;

        ServerSocket servSocket = null;
        Socket fromClientSocket = null;
        DataOutputStream out = null;
        DataInputStream in = null;

        try {
            servSocket = new ServerSocket(PORT);

            System.out.println("Setting up incoming request server on port " + PORT);

            while(isRunning) {
                fromClientSocket = servSocket.accept();

                out = new DataOutputStream(fromClientSocket.getOutputStream());
                in = new DataInputStream(fromClientSocket.getInputStream());

                String command = in.readUTF();
                String clientIp = fromClientSocket.getInetAddress().toString().replace("/", "");

                if(command.equals("decrypt"))
                {
                    String fileName = in.readUTF();

                    System.out.println("Sending decryption share for file " + fileName);

                    //byte[] payload = RSA.encrypt_outgoing(clientIp, data);
                    SecretShare.ShareInfo info = DataStore.Shares.get(fileName);
                    byte[] share = EncryptUtil.shareToByteArray(info);
                    out.writeInt(share.length);
                    out.write(share);
                }
                else if (command.equals(("create")))
                {
                    String fileName = in.readUTF();

                    int dataLen = in.readInt();
                    byte[] data = new byte[dataLen];
                    in.read(data, 0, dataLen);

                    int shareLen = in.readInt();
                    byte[] share = new byte[shareLen];
                    in.read(share, 0, shareLen);
                    //byte[] actualdata = RSA.decrypt_incoming(clientIp, data);

                    DataStore.create(fileName, data);
                }
                else if (command.equals(("distrust")))
                {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
			// TODO Auto-generated catch block //RSA key stuff
			e.printStackTrace();
		}
    }

}
