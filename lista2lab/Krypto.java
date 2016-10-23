package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class Krypto {

	private static final Charset CHARSET = Charset.forName("US-ASCII");
	//private static final Charset CHARSET = Charset.forName("UTF-8");
	//private static final String PADDING = "PKCS5Padding";
	private static final String PADDING = "NoPadding";
	String key, str;
	//byte[] cipherBytes;

	SecretKey aesKey;
	byte[] keyBytes;

	byte[] iv;
	Cipher cipher;
	IvParameterSpec ivParam;

	String ciph;
	String plainText;

	byte[][] ciphersBytes;
	IvParameterSpec[] ivParams;

	Krypto() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
		cipher = Cipher.getInstance("AES/CBC/" + PADDING);
		ciphersBytes = new byte[2][];
		ivParams = new IvParameterSpec[2];

		

	}

	void setIv(String iv) {
		this.iv = DatatypeConverter.parseHexBinary(iv);
		ivParam = new IvParameterSpec(this.iv);
	}

	String decrypt(String key, int nr) {
		plainText = "";
		try {
			keyBytes = DatatypeConverter.parseHexBinary(key);
			aesKey = new SecretKeySpec(keyBytes, "AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParams[nr]);
			plainText = new String(cipher.doFinal(ciphersBytes[nr]), CHARSET);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return plainText;
	}

	void zad1() throws FileNotFoundException, UnsupportedEncodingException {
		String keyPart = "8f8c7857c81383ee8d16973174e8a6177fcf3c8c139b6c2e36ced1b0";
		this.setIv("6f532eaf928919adea8ee30b4350f581");
		ivParams[0] = ivParam;
		this.setIv("c5facd4fa1349c18f12975b2cdd23920");
		ivParams[1] = ivParam;

		ciph = "CammDhHOcDou3T+jCB+wkDwZWCPmj0R9h6TIWyd22NoO1sz9T+XNgXcXdO6vyNKIm2B/h8En3YYxskNMgHIwu+dQMj8tNPouAAuMdXs/KwaHVUB4tbqA1r+CCS6rrT/V";
		ciphersBytes[0] = DatatypeConverter.parseBase64Binary(ciph);
		ciphersBytes[0] = Arrays.copyOfRange(ciphersBytes[0], 0, 16);
		ciph = "ZCmgKP6lti8zedStoEd24Zm0RfMBqPVzQWDJ4qnnHx4pIPYr+XpulOiwUDTwmybQaCRPtQxNtoOXOEViuZBdaEcppLGnuRx1ARtzrdMyO4jQ/hEowiiqQJybrOAWcyhS";
		ciphersBytes[1] = DatatypeConverter.parseBase64Binary(ciph);
		ciphersBytes[1] = Arrays.copyOfRange(ciphersBytes[1], 0, 16);
		
		String plain = null;
		String plain1 = null;
		long tStart = System.currentTimeMillis();
		String name = "zad1";
		PrintWriter writer = new PrintWriter(name + ".txt", CHARSET.toString());
		for (int n0 = 0; n0 < 16; n0++) {
			for (int n1 = 0; n1 < 16; n1++) {
				for (int n2 = 0; n2 < 16; n2++) {
					for (int n3 = 0; n3 < 16; n3++) {
						for (int n4 = 0; n4 < 16; n4++) {
							for (int n5 = 0; n5 < 16; n5++) {
								for (int n6 = 0; n6 < 16; n6++) {
									for (int n7 = 0; n7 < 16; n7++) {
										String testKey =  
												  Integer.toHexString(n0) + Integer.toHexString(n1)
												+ Integer.toHexString(n2) + Integer.toHexString(n3)
												+ Integer.toHexString(n4) + Integer.toHexString(n5)
												+ Integer.toHexString(n6) + Integer.toHexString(n7) 
												+ keyPart;
										plain = this.decrypt(testKey, 0);
										plain1 = this.decrypt(testKey, 1);
										// sprawdzanie wyniku
										if (plain.equals(plain1)) {
											System.out.println(testKey + "\t" + plain);
											writer.println(testKey + "\t" + plain);
											writer.flush();
										}
									}
								}
							}
						}
					}
				}
				System.out.print((100.0 / 16 * (n0)) + (100.0 / 256 * (n1 + 1)) + "%\t---\t");
				System.out.println(((System.currentTimeMillis() - tStart) / 1000.0) + " sek");
			}
		}
		writer.close();
	}

	void zad2() throws FileNotFoundException, UnsupportedEncodingException {
		String keyPart = "ae794a7ccc807a01e7267c7c719f89305cf9ea408507f5a988d41f5";
		this.setIv("c3114d0f980d70ef1172169b22dbf663");
		ivParams[0] = ivParam;
		this.setIv("6880010817f784b0eb7a925288fc95dc");
		ivParams[1] = ivParam;

		ciph = "hXSxAEjz/I76R3/YykDTTmmcuNvIwY5zTLNUshTgDDjzUmFN4dpSZpWO8LA7ePsqEk/d6I21V1kI1SnC19uedXVSxJfRhIFz1bLC3scDvGn+HejeplkJA3ODZMUgI2lUvMq+hQCTeInLSYvhPhx3UQ==";
		ciphersBytes[0] = DatatypeConverter.parseBase64Binary(ciph);
		ciphersBytes[0] = Arrays.copyOfRange(ciphersBytes[0], 0, 16);
		ciph = "kmeLYEyPHMj3Ke4nsk7m62jngMy2wDhCFxOR/ZVnjM3ayTl4aa7gS6CT4//M5wAwX6vr6i6/mNWOJnr/r6Euyebt5rCPSRBwrtDUNgdjrj5NgQFUHy6LirGIhepuH0iIRI/aJ3IWN6GeZroFGxfJag==";
		ciphersBytes[1] = DatatypeConverter.parseBase64Binary(ciph);
		ciphersBytes[1] = Arrays.copyOfRange(ciphersBytes[1], 0, 16);
		
		String plain = null;
		String plain1 = null;
		long tStart = System.currentTimeMillis();
		String name = "zad2";
		PrintWriter writer = new PrintWriter(name + ".txt", CHARSET.toString());
		for (int n0 = 0; n0 < 4; n0++) {
			for (int n1 = 0; n1 < 16; n1++) {
				for (int n2 = 0; n2 < 16; n2++) {
					for (int n3 = 0; n3 < 16; n3++) {
						for (int n4 = 0; n4 < 16; n4++) {
							for (int n5 = 0; n5 < 16; n5++) {
								for (int n6 = 0; n6 < 16; n6++) {
									for (int n7 = 0; n7 < 16; n7++) {
										for (int n8 = 0; n8 < 16; n8++) {
											String testKey =  
													  Integer.toHexString(n0) + Integer.toHexString(n1)
													+ Integer.toHexString(n2) + Integer.toHexString(n3)
													+ Integer.toHexString(n4) + Integer.toHexString(n5)
													+ Integer.toHexString(n6) + Integer.toHexString(n7) 
													+ Integer.toHexString(n8) + keyPart;
											plain = this.decrypt(testKey, 0);
											plain1 = this.decrypt(testKey, 1);
											// sprawdzanie wyniku
											if (plain.equals(plain1)) {
												System.out.println(testKey + "\t" + plain);
												System.out.println("time: "+((System.currentTimeMillis() - tStart) / 1000.0) + " sek");
												writer.println(testKey + "\t" + plain);
												writer.println("time: "+((System.currentTimeMillis() - tStart) / 1000.0) + " sek");
												writer.flush();
											}
										}
									}
								}
							}
						}
					}
				}
				System.out.print((100.0 / 16 * (n0)) + (100.0 / 256 * (n1 + 1)) + "%\t---\t");
				System.out.println(((System.currentTimeMillis() - tStart) / 1000.0) + " sek");
			}
		}
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		Krypto kryp = new Krypto();
		//kryp.zad1();
		//kryp.zad2();
		test();
	}

	static void test() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		String iv = "c3114d0f980d70ef1172169b22dbf663";
		String key = "5bf00ce02ae794a7ccc807a01e7267c7c719f89305cf9ea408507f5a988d41f5";
		String cipherText = "hXSxAEjz/I76R3/YykDTTmmcuNvIwY5zTLNUshTgDDjzUmFN4dpSZpWO8LA7ePsqEk/d6I21V1kI1SnC19uedXVSxJfRhIFz1bLC3scDvGn+HejeplkJA3ODZMUgI2lUvMq+hQCTeInLSYvhPhx3UQ==";
		
		SecretKey aesKey = new SecretKeySpec(DatatypeConverter.parseHexBinary(key), "AES");
		byte [] ivBytes = DatatypeConverter.parseHexBinary(iv);
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(ivBytes));
		byte[] result = cipher.doFinal(DatatypeConverter.parseBase64Binary(cipherText));
		System.out.println(new String(result, "UTF-8"));
	}
}