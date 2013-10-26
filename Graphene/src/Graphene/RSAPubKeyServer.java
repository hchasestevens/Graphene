package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class RSAPubKeyServer extends Thread {
	private final String RSAPubKey;
	public static final int PORT = 8051;
	public boolean isRunning;
	
	public RSAPubKeyServer( String RSAPubKey ){
		this.RSAPubKey = RSAPubKey;
		this.isRunning = true;
	}
	
	
	public void run(){
		try {
            ServerSocket servSocket = new ServerSocket(PORT);

            System.out.println("Setting up incoming RSA Pubkey server on port " + PORT);

            while(this.isRunning) {
                Socket fromClientSocket = servSocket.accept();

                PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(), true);
				
				pw.println(this.RSAPubKey);
                pw.close();

                fromClientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
	}

}
