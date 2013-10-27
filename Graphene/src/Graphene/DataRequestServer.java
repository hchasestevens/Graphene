package Graphene;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import com.tiemens.secretshare.engine.SecretShare;

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
        DataOutputStream out = null;
        DataInputStream in_buffer = null;

		try {
			socket = new Socket(this.server_ip,
					IncomingRequestServer.PORT);
			out = new DataOutputStream(socket.getOutputStream());
			in_buffer = new DataInputStream(socket.getInputStream());

            System.out.println("Requesting decryption of " + this.resource_id + " from " + this.server_ip);

            out.writeUTF("decrypt");
            out.writeUTF(this.resource_id);
            
			Integer bytelen = in_buffer.readInt();
			byte[] data = new byte[bytelen];
			byte incoming;
			for (int i = 0; i < bytelen; i++){
				data[i] = in_buffer.readByte();
			}

			callback.DataReceived(new SecretShare.ShareInfo(data));
			
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
