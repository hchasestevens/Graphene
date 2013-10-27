package Graphene;

import com.tiemens.secretshare.engine.SecretShare;
import crypto.EncryptUtil;
import crypto.EncryptedData;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: jsu
 * Date: 10/26/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataStore {
    public static final Path FILE_PATH = Paths.get("files");

    public static HashMap<String, SecretShare.ShareInfo> Shares = new HashMap<String, SecretShare.ShareInfo>();

    public static void init() {
        if(!Files.exists(FILE_PATH)) try {
            Files.createDirectory(FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void create(String fileName, byte[] data) {
        System.out.println("Getting encrypted file: " + fileName);

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(FILE_PATH.toString() + "/" + fileName + ".txt");
            out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public static EncryptedData encrypt(String fileName)
    {
        System.out.println("Encrypting file " + fileName);

        String contents = getFileAsString(fileName);

        FileOutputStream out = null;
        EncryptedData data = null;

        try {
            data = EncryptUtil.encrypt(contents);

            Shares.put(fileName, data.secretShare.get(0));

            out = new FileOutputStream(FILE_PATH.toString() + "/" + fileName + ".txt");
            out.write(data.encryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return data;
    }

    public static void delete(String fileName)
    {
        Path file = getFilePath(fileName);

        try {
            Files.delete(file);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static byte[] getFileContents(String fileName)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(getFilePath(fileName));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return encoded;
    }

    public static String getFileAsString(String fileName) {
        byte[] file = getFileContents(fileName);
        ByteBuffer buffer = ByteBuffer.wrap(file);
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    private static Path getFilePath(String fileName) {
        return Paths.get(FILE_PATH.toString(), fileName + ".txt");
    }
}
