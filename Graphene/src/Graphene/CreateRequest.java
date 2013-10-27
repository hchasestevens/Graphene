package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateRequest {
    private String fileName;
    private String data;

    public CreateRequest(String fileName, String data) {
        this.fileName = fileName;
        this.data = data;
    }

    public void run() {
        // Notify all servers of the new file
        for(String serverIp : NetworkInfo.NodeIps) {
            this.notifyServer(serverIp);
        }
    }

    private void notifyServer(String serverIp) {
        try {
            Socket socket = new Socket(serverIp, IncomingRequestServer.PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Requesting creation of file " + this.fileName + " to " + serverIp);
            
            this.data = RSA.encrypt_outgoing(serverIp, this.data);

            out.println("create " + this.fileName + " " + this.data);

            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("Creation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Creation: " + e.getMessage());
        } catch (Exception e) {
			// TODO Auto-generated catch block //RSA bullshit
			e.printStackTrace();
		}
    }
}
