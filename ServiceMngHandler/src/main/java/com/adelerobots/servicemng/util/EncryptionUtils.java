package com.adelerobots.servicemng.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.encoders.Base64;



public class EncryptionUtils {

	public static PublicKey loadPublicKey(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Read Public Key.
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis = new FileInputStream(path + "/public.key");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		return publicKey;
	}

	public static byte[] encryptPkcs12(String text, Key key) {
		// ------------------------------------------------------------------------------------------------------------------------
		// Instanciando Cipher
		// ------------------------------------------------------------------------------------------------------------------------
		/*
		 * cipher - proceso de ecriptacion de datos para encriptar y
		 * desencriptar en este caso utilizando el algoritmo DSA
		 */
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DSA");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		}
		byte[] cipherText = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	public static String encryptBC(String texto, Key key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
		//Cipher cifrador = Cipher.getInstance("RSA", "BC"); // Hace uso del provider BC
		// Creates an RSA Cipher object (specifying the algorithm, mode, and padding).
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        String encodedText = null;

		  
		// Cifrar con clave publica
		byte[] bufferCifrado = null;
		try {
			// Poner cifrador en modo CIFRADO
			cipher.init(Cipher.ENCRYPT_MODE, key);	

			bufferCifrado = cipher.doFinal(texto.getBytes("UTF-8"));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(bufferCifrado.length>0){
			try {
				//encodedText = new String(bufferCifrado,"UTF-8");
				encodedText = new String(Base64.encode(bufferCifrado),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return encodedText;

	}

}
