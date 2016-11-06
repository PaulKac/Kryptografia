package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyStore;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FilenameUtils;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Zad2 {
	static File keystore;
	private static String keyId, keyPass, PIN;
	final static Key configKey = new SecretKeySpec(DatatypeConverter.parseHexBinary("5bf00ce02ae794a7ccc807a01e7267c7c719f89305cf9ea408507f5a988d41f5"), "AES");
	static BasicPlayer player;
	static long shift, limit;
	static int point;
	static double gain;
	
	/*
	 * key1, keystorepass, keypass
	 * 
	 */
	static byte[] encrypt(byte[] plainBytes) throws Exception {
		
		// wyciaganie klucza z keystora
		InputStream keystoreStream = new FileInputStream(keystore);
		KeyStore keystore = KeyStore.getInstance("JCEKS");
		keystore.load(keystoreStream, "keystorepass".toCharArray());
		if (!keystore.containsAlias(keyId)) {
		    throw new RuntimeException("Alias for key not found");
		}
		Key key = keystore.getKey(keyId, keyPass.toCharArray());
		
		Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		
		byte [] cipherBytes = cipher.doFinal(plainBytes);
		byte[] ciphfinal = new byte[iv.length + cipherBytes.length];
		System.arraycopy(iv, 0, ciphfinal, 0, iv.length);
		System.arraycopy(cipherBytes, 0, ciphfinal, iv.length, cipherBytes.length);
				
		return ciphfinal;
	}
	
	static byte[] decrypt(byte [] fileBytes) throws Exception {
				
		// wyciaganie klucza z keystora
		InputStream keystoreStream = new FileInputStream(keystore);
		KeyStore keystore = KeyStore.getInstance("JCEKS");
		keystore.load(keystoreStream, "keystorepass".toCharArray());
		if (!keystore.containsAlias(keyId)) {
		    throw new RuntimeException("Alias for key not found");
		}
		Key key = keystore.getKey(keyId, keyPass.toCharArray());
		
		byte[] ivBytes = new byte[16];
		System.arraycopy(fileBytes, 0, ivBytes, 0, 16);
		byte[] cipherBytes = new byte[fileBytes.length - 16];
		System.arraycopy(fileBytes, 16, cipherBytes, 0, fileBytes.length-16);
		
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] result = cipher.doFinal(cipherBytes);
		return result;
	}
	
	static byte[] encryptConfig(byte[] plainBytes) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, configKey);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		
		byte [] cipherBytes = cipher.doFinal(plainBytes);
		byte[] ciphfinal = new byte[iv.length + cipherBytes.length];
		System.arraycopy(iv, 0, ciphfinal, 0, iv.length);
		System.arraycopy(cipherBytes, 0, ciphfinal, iv.length, cipherBytes.length);
				
		return ciphfinal;
	}
	
	static byte[] decryptConfig(byte[] fileBytes) throws Exception {
		byte[] ivBytes = new byte[16];
		System.arraycopy(fileBytes, 0, ivBytes, 0, 16);
		byte[] cipherBytes = new byte[fileBytes.length - 16];
		System.arraycopy(fileBytes, 16, cipherBytes, 0, fileBytes.length-16);
		
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, configKey, new IvParameterSpec(ivBytes));
		return cipher.doFinal(cipherBytes);
	}
	
	private static long getDurationWithMp3Spi(File file) throws UnsupportedAudioFileException, IOException {
	    AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
	    if (fileFormat instanceof TAudioFileFormat) {
	        Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
	        String key = "duration";
	        Long microseconds = (Long) properties.get(key);
	        int mili = (int) (microseconds / 1000);
	        int sec = (mili / 1000) % 60;
	        int min = (mili / 1000) / 60;
	        //System.out.println(min+"min " + sec+ "sec");
	        return (long) mili/1000;
	    } else {
	        throw new UnsupportedAudioFileException();
	    }
	}
	
	static void play(File file) throws Exception{
		shift = (long) (file.length()/getDurationWithMp3Spi(file)*10);
		//shift = (long) (file.length()/10);
		limit = file.length();
		point = 0;
		gain = 1.0;
		player = new BasicPlayer();
		player.open(file);
		player.play();
	}
	
	static File chooseFile(String choose){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int retVal = chooser.showDialog(null, choose);
		File file;
		if (retVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
            System.out.println("Wybrany plik: " + file.getAbsolutePath());
        } else {
        	System.out.println("Wybieranie anulowane. Zamkniecie programu.\n");
        	return null;
        }
		return file;
	}

	public static void main(String[] args) throws Exception {
		File configFile = new File("config");
		if(!configFile.exists()) { // plik config nie istnieje
			// "instalacja" - tworzenie pliku config
			keystore = chooseFile("Wybierz plik keystore");
			System.out.print("Podaj identyfikator klucza: ");
			Scanner sc = new Scanner(System.in);
			keyId = sc.next();
			//keyId = "key1";
			System.out.print("Podaj haslo klucza: ");
			//keyPass = System.console().readPassword().toString();
			keyPass = "keypass"; //TODO remove later, uncomment line above
			System.out.print("Podaj PIN: ");
			//PIN = System.console().readPassword().toString();
		    PIN = "1234"; //TODO remove later, uncomment line above
		    
		    String configString = 
		    		keystore.getAbsolutePath() + "\n"
		    		+ keyId + "\n"
		    		+ keyPass + "\n"
		    		+PIN;
		    
		   
		    FileOutputStream fos = new FileOutputStream(configFile.getAbsolutePath());
			fos.write(encryptConfig(configString.getBytes()));
			fos.close();
			//System.out.println(  );
		} else {
			String configString = new String(decryptConfig(Files.readAllBytes(configFile.toPath())));
			String[] sp = configString.split("\n");
			keystore = new File(sp[0]);
			keyId = sp[1];
			keyPass = sp[2];
			PIN = sp[3];
			System.out.print("Podaj PIN: ");
			//PIN = System.console().readPassword().toString();
			PIN = "1234"; //TODO remove later, uncomment line above
			Scanner sc = new Scanner(System.in);
			String tPIN = "1234";
			//String tPIN = sc.next();
			if(tPIN.matches(PIN)) {
				System.out.println("PIN zgodny.");
			} else {
				System.out.println("Nieprawidlowy PIN.");
				return;
			}
			
		    File trackenc = chooseFile("Wybierz enc mp3");
		    
			//File track = new File("C:\\Users\\Paul\\Documents\\sem5workspace\\track.mp3");
		    System.err.close();
			play(decryptFile(trackenc));
			
			GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
	        System.out.println("Strzalki gora/dol - glosnosc, lewo/prawo - przewijanie, spacja - pauza/start, esc - wyjscie");
	        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
	            @Override public void keyPressed(GlobalKeyEvent event) {
	                //System.out.println(event);
	                if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_SPACE){
	                	try {
		                	if(player.getStatus() == 0)
								player.pause();
							else
								player.resume();
	                	} catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_ESCAPE){
						try {
							player.stop();
							keyboardHook.shutdownHook();
						} catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_LEFT){
						try { 
							if(point > 0) {
								point--;
								player.seek(point*shift);
							} else if(point == 0)
								player.seek(0);
						} catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_RIGHT){
						try { 
							if((point+1)*shift < limit) {
								point++;
								player.seek(point*shift);
							}
						} catch (BasicPlayerException e) {e.printStackTrace();}
	                } /*else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_0){
						try { player.seek(0*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_1){
						try { player.seek(1*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_2){
						try { player.seek(2*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_3){
						try { player.seek(3*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_4){
						try { player.seek(4*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_5){
						try { player.seek(5*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_6){
						try { player.seek(6*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_7){
						try { player.seek(7*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_8){
						try { player.seek(8*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_9){
						try { player.seek(9*shift); } catch (BasicPlayerException e) {e.printStackTrace();}
	                }*/ else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_UP){
						try {
							if(gain < 1.0) {
								gain = gain+0.1;
								player.setGain(gain);
							}
						} catch (BasicPlayerException e) {e.printStackTrace();}
	                } else if(event.getVirtualKeyCode()==GlobalKeyEvent.VK_DOWN){
						try {
							if(gain > 0.0) {
								gain = gain-0.1;
								player.setGain(gain);
							}
						} catch (BasicPlayerException e) {e.printStackTrace();}
	                }
	            }
	            @Override public void keyReleased(GlobalKeyEvent event) {}
	        });
		}
		//System.out.println(encrypt("C:\\Users\\Paul\\Documents\\sem5workspace\\keystore.jks", "key1", "keypass", chooseFile()));
		//System.out.println(decrypt("C:\\Users\\Paul\\Documents\\sem5workspace\\keystore.jks", "key1", "keypass", chooseFile("Wybierz plik do dec")));
		//play(chooseFile());
	}

	static String encryptFile(String path, String keyId, String keyPass, File file) throws Exception {
		
		// wyciaganie klucza z keystora
		InputStream keystoreStream = new FileInputStream(path);
		KeyStore keystore = KeyStore.getInstance("JCEKS");
		keystore.load(keystoreStream, "keystorepass".toCharArray());
		if (!keystore.containsAlias(keyId)) {
		    throw new RuntimeException("Alias for key not found");
		}
		Key key = keystore.getKey(keyId, keyPass.toCharArray());
		
		Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		
		Path path1 = Paths.get(file.getAbsolutePath());
		byte[] ciphertext = cipher.doFinal(Files.readAllBytes(path1));
		byte[] ciphfinal = new byte[iv.length + ciphertext.length];
		System.arraycopy(iv, 0, ciphfinal, 0, iv.length);
		System.arraycopy(ciphertext, 0, ciphfinal, iv.length, ciphertext.length);
		//System.out.println("iv.length: "+iv.length);
		
		//System.out.println(ciphfinal.length);
		FileOutputStream fos = new FileOutputStream(
				file.getParentFile()+"\\"+file.getName() // sciezka\plik.txt
				+".AES_CTR_encrypted");			
		fos.write(ciphfinal);
		fos.close();
		return "File encrypted";
	}
	
	static File decryptFile(File file) throws Exception {		
		// wyciaganie klucza z keystora
		InputStream keystoreStream = new FileInputStream(keystore);
		KeyStore keystore = KeyStore.getInstance("JCEKS");
		keystore.load(keystoreStream, "keystorepass".toCharArray());
		if (!keystore.containsAlias(keyId)) {
		    throw new RuntimeException("Alias for key not found");
		}
		Key key = keystore.getKey(keyId, keyPass.toCharArray());
		
		Path path1 = Paths.get(file.getAbsolutePath());
		byte[] fileBytes = Files.readAllBytes(path1);
		
		byte[] ivBytes = new byte[16];
		System.arraycopy(fileBytes, 0, ivBytes, 0, 16);
		byte[] cipherBytes = new byte[fileBytes.length - 16];
		System.arraycopy(fileBytes, 16, cipherBytes, 0, fileBytes.length-16);
		
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
		byte[] result = cipher.doFinal(cipherBytes);
		String name = 
				file.getParentFile()+"\\"+FilenameUtils.removeExtension(FilenameUtils.removeExtension(file.getName()))
				+"_AES_CTR_decrypted"
				+"."+FilenameUtils.getExtension(FilenameUtils.removeExtension(file.getName()));
		FileOutputStream fos = new FileOutputStream(name);
		fos.write(result);
		fos.close();
		File file1 = new File(name);
		//Files.write(Paths.get("filename"), bytes);
		return file1;
	}
	
}
