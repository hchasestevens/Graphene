package Graphene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.UUID;

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

    public static void main(String[] args) throws IOException {
        String line = "";

        String myIp = args[0];

        NetworkInfo.NodeIps.add("172.20.128.33");

        // Start up incoming request server
        IncomingRequestServer incomingServer = new IncomingRequestServer();
        incomingServer.start();

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

                CreateRequest request = new CreateRequest(fileName, data);
                request.run();
            }
            else if(command.equals(CMD_ADD_CLIENT)) {
                String ip = sc.next();


            }
        }

        incomingServer.isRunning = false;
    }
    
    
}
