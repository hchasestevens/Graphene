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

        String str = null;

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in_buffer = null;
		
		try {
			socket = new Socket(ip,
					RSAPubKeyServer.PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
			in_buffer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

            System.out.println("Requesting pubkey from " + ip);

			str = in_buffer.readLine();
            
            cache.put(ip, str);
		} catch (UnknownHostException e) {
			throw e;
		} catch (IOException e) {
            throw e;
		} finally {
            out.close();
            in_buffer.close();
            socket.close();
        }

        return str;
	}

}
