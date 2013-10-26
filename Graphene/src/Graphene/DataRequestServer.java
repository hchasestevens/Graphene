package Graphene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataRequestServer extends Thread {
	private final String server_ip;
	private final String resource_id;
	private final String my_ip;
	private IDecryptionCallback callback;
	//private BufferedReader in_buffer;
	public Boolean finished = false;
	public Boolean failed = false;
	public String error;
	
	
	public DataRequestServer (String server_ip, String resource_id, 
			IDecryptionCallback callback){
		this.server_ip = server_ip;
		this.resource_id = resource_id;
		this.my_ip = NetworkInfo.myIp;
		this.callback = callback;
		
		
	}
	
	@Override
	public void run()
	{
		this.listenSocket();
	}
	
	
	public Boolean isFinished(){
		return this.finished||this.failed;
	}
	
	
	public Integer getStatus(){
		if (!(this.finished || this.failed)) {return 0;}
		if (this.finished && !this.failed) {return 1;}
		if (!this.finished && this.failed) {return -1;}
		return -1;
	}
	
	
	private void listenSocket(){
		//Create socket connection
		   try{
		     Socket socket = new Socket(this.server_ip, IncomingRequestServer.PORT);
		     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		     BufferedReader in_buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		     
		     out.println("decrypt " + this.my_ip + " " + this.resource_id);
		   } catch (UnknownHostException e) {
		     this.failed = true;
		     this.error = "Unknown host: " + this.server_ip;
		   } catch  (IOException e) {
		     this.failed = true;
		     this.error = "No I/O";
		   }
		   
		}
}
