package Graphene;

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
        BufferedWriter out = null;
        BufferedReader br = null;

        try {
            servSocket = new ServerSocket(PORT);

            System.out.println("Setting up incoming request server on port " + PORT);

            while(isRunning) {
                fromClientSocket = servSocket.accept();

                out = new BufferedWriter(new OutputStreamWriter(fromClientSocket.getOutputStream()));
                br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));

                while ((str = br.readLine()) != null) {
                    Scanner sc = new Scanner(str);
                    String command = sc.next();
                    String clientIp = fromClientSocket.getInetAddress().toString().replace("/", "");

                    if(command.equals("decrypt"))
                    {
                        String fileName = sc.next();

                        System.out.println("Sending decryption share for file " + fileName);

                        //byte[] payload = RSA.encrypt_outgoing(clientIp, data);
                        out.write(DataStore.Shares.get(fileName));
                    }
                    else if (command.equals(("create")))
                    {
                        String fileName = sc.next();

                        String data = sc.next();
                        String share = sc.next();
                        //byte[] actualdata = RSA.decrypt_incoming(clientIp, data);

                        //DataStore.create(fileName, data);
                    }
                    else if (command.equals(("distrust")))
                    {

                    }

                    System.out.println("The message: " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
			// TODO Auto-generated catch block //RSA key stuff
			e.printStackTrace();
		} finally {
            out.close();
            try {
                br.close();
                fromClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}
