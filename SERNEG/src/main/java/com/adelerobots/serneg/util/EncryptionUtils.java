package com.adelerobots.serneg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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

	public static PrivateKey loadPrivateKey(String path, String algorithm)
			throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		// Read Private Key.
		File filePrivateKey = new File(path + "/private.key");
		FileInputStream fis = new FileInputStream(path + "/private.key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
				encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return privateKey;
	}

	public static String encryptBC(String texto, Key key)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException {
		// Cipher cifrador = Cipher.getInstance("RSA", "BC"); // Hace uso del
		// provider BC
		// Creates an RSA Cipher object (specifying the algorithm, mode, and
		// padding).
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

		if (bufferCifrado.length > 0) {
			try {
				encodedText = new String(Base64.encode(bufferCifrado),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return encodedText;

	}

	public static String decryptBC(String texto, Key key)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException {
		// Cipher cifrador = Cipher.getInstance("RSA", "BC"); // Hace uso del
		// provider BC
		// Creates an RSA Cipher object (specifying the algorithm, mode, and
		// padding).
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		String plainText = null;

		// Cifrar con clave publica
		byte[] bufferTextoPlano = null;
		try {
			// Poner cifrador en modo DESENCRIPTACIÓN
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte [] bytesText = Base64.decode(texto.getBytes("UTF-8"));
			bufferTextoPlano = cipher.doFinal(bytesText);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		if (bufferTextoPlano.length > 0) {
			try {
				plainText = new String(bufferTextoPlano,"UTF-8");
			} catch (UnsupportedEncodingException uee) {
				// TODO Auto-generated catch block
				uee.printStackTrace();
			}
		}

		return plainText;

	}

}
