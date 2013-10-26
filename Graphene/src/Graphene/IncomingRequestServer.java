package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
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
        try {
            servSocket = new ServerSocket(PORT);

            System.out.println("Setting up incoming request server on port " + PORT);

            while(isRunning) {
                Socket fromClientSocket = servSocket.accept();

                PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));

                while ((str = br.readLine()) != null) {
                    Scanner sc = new Scanner(str);
                    String command = sc.next();

                    if(command.equals("decrypt"))
                    {
                        //String returnIp = sc.next();
                        String fileName = sc.next();

                        System.out.println("Decrypting file " + fileName);

                        EncryptedFile file = DataStore.getFile(fileName);
                        BigInteger partialDecryption = file.partialDecryption();
                        pw.println(partialDecryption.toString());
                    }
                    else if (command.equals(("create")))
                    {
                        String fileName = sc.next();
                        String data = sc.next();
                        //BigInteger sig = sc.nextBigInteger();

                        DataStore.create(fileName, data);
                    }
                    else if (command.equals(("distrust")))
                    {

                    }

                    System.out.println("The message: " + str);
                }

                pw.close();
                br.close();

                fromClientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
