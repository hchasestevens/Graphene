package Graphene;

import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NodeIPSync {
    private static final String NODES_KEY = "graphenedatanetworknodes";
    private static final String IP_KEY = "graphenedatanetworknode";

    private static UUID ourId = UUID.randomUUID();

    public static void reset() {
        storeValue(NODES_KEY, "");
    }

    public static void StoreIp(String ip)
    {
        String nodes = getValue(NODES_KEY);
        nodes += " " + ourId.toString();
        storeValue(ourId.toString(), ip);

        storeValue(NODES_KEY, nodes);
    }

    public static void RemoveIp()
    {
        String nodeRes = getValue(NODES_KEY);
        String[] nodes = nodeRes.split(" ");

        List<String> nodeList = Arrays.asList(nodes);
        nodeList.remove(ourId.toString());

        StringBuilder builder = new StringBuilder();
        for(String s : nodeList) {
            builder.append(s);
            builder.append(" ");
        }

        storeValue(NODES_KEY, builder.toString());
    }

    public static String[] GetIps()
    {
        String[] nodes = getValue(NODES_KEY).split(" ");

        ArrayList<String> ipList = new ArrayList<String>();

        for(int i = 0; i < nodes.length; i++) {
            if(!nodes[i].isEmpty()) ipList.add(nodes[i]);
        }

        String[] ips = new String[ipList.size()];
        ipList.toArray(ips);

        return ips;
    }

    private static String getValue(String key) {
        String result = getResult("http://api.openkeyval.org/" + key);
        return result;
    }

    private static void storeValue(String key, String value) {
        try {
            String data = URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            // Send data
            URL url = new URL("http://api.openkeyval.org/" + key);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            String returned = "";
            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = rd.readLine()) != null)
                returned += inputLine;
            wr.close();
            rd.close();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        catch(ProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getResult(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";

        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            int status = conn.getResponseCode();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }

            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
