package Graphene;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static final Path FILE_PATH = Paths.get("files");

    public static void init() {
        if(!Files.exists(FILE_PATH)) try {
            Files.createDirectory(FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void create(String fileName, String file)
    {
        System.out.println("Creating file " + fileName + " with data " + file);

        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                     new FileOutputStream(FILE_PATH.toString() + "/" + fileName + ".txt"), "utf-8"));
            writer.write(file);
        } catch (IOException ex){
            // report
        } finally {
            try { writer.close(); } catch (Exception ex) {}
        }
    }

    public static void delete(String fileName)
    {
        Path file = Paths.get(getFileContents(fileName));

        try {
            Files.delete(file);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static String getFileContents(String fileName)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(getFilePath(fileName));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
    }

    private static Path getFilePath(String fileName) {
        return Paths.get(FILE_PATH.toString(), fileName + ".txt");
    }
}
