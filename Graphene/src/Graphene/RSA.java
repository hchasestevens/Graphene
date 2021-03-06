package Graphene;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSA {
	private static PrivateKey privateKey;
	private static PublicKey publicKey;
	public static enum KeyType {PUBLIC, PRIVATE};
	private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";
	private final static boolean RSA_ENABLED = false;
	
	private static void generateKeys() throws Exception{
		final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		final KeyPair key = keyGen.generateKeyPair();
		File privateKeyFile = new File(Main.RSA_PRIVATE_KEY_FILE);
		File publicKeyFile = new File(Main.RSA_PUBLIC_KEY_FILE);
		
		if (publicKeyFile.getParentFile() != null)
			publicKeyFile.getParentFile().mkdirs();
		publicKeyFile.createNewFile();
		if (privateKeyFile.getParentFile() != null)
			privateKeyFile.getParentFile().mkdirs();
		privateKeyFile.createNewFile();
		
		ObjectOutputStream publicKeyOutput = new ObjectOutputStream(
				new FileOutputStream(publicKeyFile));
		publicKeyOutput.writeObject(key.getPublic());
		publicKeyOutput.close();
		
		ObjectOutputStream privateKeyOutput = new ObjectOutputStream(
				new FileOutputStream(privateKeyFile));
		privateKeyOutput.writeObject(key.getPrivate());
		privateKeyOutput.close();
		
		privateKey = key.getPrivate();
		publicKey = key.getPublic();
	}
	
	
	private static boolean keysGenerated() {
		File privateKey = new File(Main.RSA_PRIVATE_KEY_FILE);
		File publicKey = new File(Main.RSA_PUBLIC_KEY_FILE);
		
		return (privateKey.exists() && publicKey.exists());
	}
	
	
	public static byte[] encrypt(String text, Key key) throws Exception{
		byte[] cipherText;
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		cipherText = cipher.doFinal(text.getBytes());
		return cipherText;
	}
	
	
	public static byte[] encrypt(byte[] text, Key key) throws Exception{
		byte[] cipherText;
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		cipherText = cipher.doFinal(text);
		return cipherText;
	}
	
	
	public static String decrypt(byte[] text, Key key) throws Exception{
		byte[] decrypted;
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		decrypted = cipher.doFinal(text);
		return new String(decrypted);
	}
	
	
	public static String decrypt(String text, Key key) throws Exception{
		byte[] decrypted;
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		decrypted = cipher.doFinal(text.getBytes());
		return new String(decrypted);
	}
	
	
	public static PrivateKey getPrivateKey() throws Exception{
		if (privateKey != null)
			return privateKey;
		if (!keysGenerated()){
			generateKeys();
			return privateKey;
		}
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Main.RSA_PRIVATE_KEY_FILE));
		privateKey = (PrivateKey) inputStream.readObject();
		return privateKey;
	}
	
	
	public static PublicKey getPublicKey() throws Exception{
		if (publicKey != null)
			return publicKey;
		if (!keysGenerated()){
			generateKeys();
			return publicKey;
		}
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Main.RSA_PUBLIC_KEY_FILE));
		publicKey = (PublicKey) inputStream.readObject();
		return publicKey;
	}
	
	
	public static String keyToString(Key key){
		Base64 base64 = new Base64();
		return base64.encodeToString(key.getEncoded());
	}
	
	
	public static Key stringToKey(String str, KeyType keyType) throws Exception{
		Base64 base64 = new Base64();
		byte[] encodedKey = base64.decode(str);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		if (keyType == keyType.PRIVATE)
			return kf.generatePrivate(spec);
		if (keyType == keyType.PUBLIC)
			return kf.generatePublic(spec);
		throw new Exception("keyType must be PUBLIC or PRIVATE.");
	}
	
	
	public static byte[] encrypt_outgoing(String clientIp, String data) throws Exception{
		// returns Client.Pubkey.Encrypt(data)
		// We are Server
		
		//testing working:
		if (!RSA_ENABLED)
			return data.getBytes();
		
		String clientPubkeyStr = RSAPubKeyClient.RSAPubKeyClient(clientIp);
        PublicKey clientPubkey = (PublicKey) stringToKey(clientPubkeyStr, KeyType.PUBLIC);
        byte[] encrypted_data = encrypt(data, clientPubkey);
		return encrypted_data;
	}
	
	
	public static String decrypt_incoming(String serverIP, byte[] data) throws Exception{
		// returns Client.Privkey.Decrypt(data)
		// We are Client
		
		if (!RSA_ENABLED){
		System.out.println("Using UTF-8");
		return new String (data, "UTF-8");}
		
		PrivateKey clientPrivKey = getPrivateKey();
		String decrypted_data = decrypt(data, clientPrivKey);
		return decrypted_data;
	}
}
