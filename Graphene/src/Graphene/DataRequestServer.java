package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

public class DataRequestServer extends Thread {
	private final String server_ip;
	private final String resource_id;
	private IDecryptionCallback callback;

	public DataRequestServer(String server_ip, String resource_id,
			IDecryptionCallback callback) {
		this.server_ip = server_ip;
		this.resource_id = resource_id;
		this.callback = callback;
	}

	@Override
	public void run() {
		Socket socket = null;
        PrintWriter out = null;
        BufferedReader in_buffer = null;

		try {
			socket = new Socket(this.server_ip,
					IncomingRequestServer.PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
			in_buffer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

            System.out.println("Requesting decryption of " + this.resource_id + " from " + this.server_ip);

			out.println("decrypt " + this.resource_id);

			String str;
			while ((str = in_buffer.readLine()) != null) {
				if (!str.isEmpty()) {
					str = RSA.decrypt_incoming(this.server_ip, str);
					callback.DataReceived(new BigInteger(str)); //TODO: Change this from BigInteger
					break;
				}
			}

            out.close();
            socket.close();
            in_buffer.close();
		} catch (UnknownHostException e) {
			this.callback.OnError(e.getMessage());
		} catch (IOException e) {
            this.callback.OnError(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block //RSA bullshit
			e.printStackTrace();
		} finally {
            out.close();
            try {
                in_buffer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
	}
}
