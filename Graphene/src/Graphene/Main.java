package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.file.*;
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

    private static final String CMD_REFRESH_NODES = "refresh";

    private static final String PARAM_RESET = "-r";

    private static final String HELP_TEXT =
            CMD_HELP_HELP + System.lineSeparator() +
            CMD_QUIT_HELP + System.lineSeparator() +
            CMD_DECRYPT_HELP + System.lineSeparator();
    
    public static final String RSA_PRIVATE_KEY_FILE = ".private.key";
    public static final String RSA_PUBLIC_KEY_FILE = ".public.key";

    public static void main(String[] args) throws IOException {
        if(args.length > 0 && args[0].equals(PARAM_RESET)) {
            NodeIPSync.reset();
        }

        // Init node network data
        NetworkInfo.MyIp = InetAddress.getLocalHost().getHostAddress();
        NodeIPSync.StoreIp(NetworkInfo.MyIp);

        refreshNodes();

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

        // Start up folder change watcher
        DataStore.init();
        FileWatcher fileWatcher = new FileWatcher(DataStore.FILE_PATH);
        fileWatcher.start();

        // Open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = "";

        while(!line.equals(CMD_QUIT)) {
            line = br.readLine();

            Scanner sc = new Scanner(line);
            if(!sc.hasNext()) continue;

            String command = sc.next();

            if(command.equals(CMD_HELP)) {
                System.out.println(HELP_TEXT);
            }
            else if(command.equals(CMD_DECRYPT)) {
                String fileName = sc.next();

                DecryptionRequest rq = new DecryptionRequest(fileName);
            }
            else if(command.equals(CMD_ADD_CLIENT)) {
                String ip = sc.next();
                NetworkInfo.NodeIps.add(ip);
            }
            else if(command.equals(CMD_REFRESH_NODES)) {
                refreshNodes();
            }
        }

        NodeIPSync.RemoveIp();

        fileWatcher.isRunning = false;
        incomingServer.isRunning = false;
    }

    private static void refreshNodes() {
        NetworkInfo.ClearNodeIps();

        for(String ip : NodeIPSync.GetIps()) {
            System.out.println("Found node " + ip);
            NetworkInfo.AddNodeIp(ip);
        }

        NetworkInfo.RemoveNodeIp(NetworkInfo.MyIp);
    }
}
