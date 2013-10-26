package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class RSAPubKeyClient {
	
	public static String RSAPubKeyClient( String ip ) throws IOException{
		HashMap<String, String> cache = NetworkInfo.pubkeyCache;
		
		if (cache.containsKey(ip)){
			return cache.get(ip);
		}
		
		try {
			Socket socket = new Socket(ip,
					RSAPubKeyServer.PORT);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in_buffer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

            System.out.println("Requesting pubkey from " + ip);

			String str = in_buffer.readLine();
            out.close();
            socket.close();
            in_buffer.close();
            
            cache.put(ip, str);
            
			return str;

		} catch (UnknownHostException e) {
			throw e;
		} catch (IOException e) {
            throw e;
		}
	}

}
