package Graphene;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkInfo {
    public static String MyIp;

    public static ArrayList<String> NodeIps = new ArrayList<String>();
    
    public static HashMap<String, String> pubkeyCache = new HashMap<String, String>();

    public static void ClearNodeIps() {
        synchronized (NodeIps) {
            NodeIps.clear();
        }
    }

    public static void AddNodeIp(String ip) {
        synchronized (NodeIps) {
            NodeIps.add(ip);
        }
    }

    public static void RemoveNodeIp(String ip) {
        synchronized (NodeIps) {
            NodeIps.remove(ip);
        }
    }
}
