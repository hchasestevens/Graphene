package Graphene;

import java.math.BigInteger;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataStore {
    private static Dictionary<String, EncryptedFile> files = new Hashtable<String, EncryptedFile>();

    public static void create(String fileName, String file, BigInteger sig)
    {
        synchronized (files) {
            files.put(fileName, new EncryptedFile(fileName, file, sig));
        }
    }

    public static void delete(String fileName)
    {
        synchronized (files) {
            files.remove(fileName);
        }
    }

    public static EncryptedFile getFile(String fileName)
    {
        return files.get(fileName);
    }
}
