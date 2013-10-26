package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;

public class Main {
    private static final String CMD_HELP = "help";
    private static final String CMD_HELP_HELP = CMD_HELP + " - show this message";

    private static final String CMD_QUIT = "quit";
    private static final String CMD_QUIT_HELP = CMD_QUIT + " - terminate the application";

    private static final String CMD_DECRYPT = "decrypt";
    private static final String CMD_DECRYPT_HELP = CMD_DECRYPT + " - decrypt a file, format: decrypt <fileName>";

    private static final String CMD_CREATE = "create";
    private static final String CMD_CREATE_HELP = CMD_CREATE + " - create a file, format: create <fileName> <data>";

    private static final String CMD_ADD_CLIENT = "client";
    private static final String CMD_ADD_CLIENT_HELP = CMD_ADD_CLIENT + " - add a client, format: client <ip>";

    private static final String HELP_TEXT =
            CMD_HELP_HELP + System.lineSeparator() +
            CMD_QUIT_HELP + System.lineSeparator() +
            CMD_DECRYPT_HELP + System.lineSeparator();
    
    public static final String RSA_PRIVATE_KEY_FILE = ".private.key";
    public static final String RSA_PUBLIC_KEY_FILE = ".public.key";

    public static void main(String[] args) throws IOException {
        String line = "";

        NetworkInfo.MyIp = InetAddress.getLocalHost().getHostAddress();

        NetworkInfo.NodeIps.add("172.20.128.33");

        // Start up incoming request server
        IncomingRequestServer incomingServer = new IncomingRequestServer();
        incomingServer.start();
        // Start up incoming RSA pubkey request server
        RSAPubKeyServer pubkey_server;
		try {
			pubkey_server = new RSAPubKeyServer(RSA.keyToString(RSA.getPublicKey()));
	        pubkey_server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to start public key server!!!");
		}

        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(!line.equals(CMD_QUIT)) {
            line = br.readLine();
            Scanner sc = new Scanner(line);

            String command = sc.next();

            if(command.equals(CMD_HELP)) {
                System.out.println(HELP_TEXT);
            }
            else if(command.equals(CMD_DECRYPT)) {
                String fileName = sc.next();

                DecryptionRequest rq = new DecryptionRequest(fileName);
                //rq.Wait();


            }
            else if(command.equals(CMD_CREATE)) {
                String fileName = sc.next();
                String data = sc.next();

                // Create file in local node
                DataStore.create(fileName, data);

                // Let other nodes know of the change
                CreateRequest request = new CreateRequest(fileName, data);
                request.run();
            }
            else if(command.equals(CMD_ADD_CLIENT)) {
                String ip = sc.next();
                NetworkInfo.NodeIps.add(ip);
            }
        }

        incomingServer.isRunning = false;
    }
}
