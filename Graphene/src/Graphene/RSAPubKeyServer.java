package Graphene;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;

public class RSAPubKeyServer extends Thread {
	private final String RSAPubKey;
	public static final int PORT = 8051;
	private boolean isRunning;
	
	public RSAPubKeyServer( String RSAPubKey ){
		Base64 base64 = new Base64();
		this.RSAPubKey = RSAPubKey;
		this.isRunning = true;
	}
	
	
	public void run(){
        Socket fromClientSocket = null;
        PrintWriter pw = null;

		try {
            ServerSocket servSocket = new ServerSocket(PORT);

            System.out.println("Setting up incoming RSA Pubkey server on port " + PORT);

            while(this.isRunning) {
                fromClientSocket = servSocket.accept();
                pw = new PrintWriter(fromClientSocket.getOutputStream(), true);

				pw.print(this.RSAPubKey);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            pw.close();
            try {
                fromClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
	}

}
