package encryption;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.imageio.ImageIO;

public class ImgEncryp implements Encryptable<byte[]> {

    private static final String ALGORITHM = "PBEWithMD5AndTripleDES";
    private final int PARAM_ITERATION_COUNT = 100;

    private byte[] imageByte;
//    private byte[] outputs;
    private SecretKey key;
    private byte[] salt;
    private Cipher cipher;
    private PBEParameterSpec pbeParam;

    public enum Command {
	ENCRYPT, DECRYPT
    }

    // For encryption
    public ImgEncryp(String filePath, char[] password, Command com) throws IOException {
	setupKey(password);
	salt = new byte[8];

	if (com == Command.ENCRYPT) {
	    // get the image
	    BufferedImage image = ImageIO.read(new File(filePath));
	    this.imageByte = imageToByte(image);

	    // Generate byte[8] or salt.
	    new Random().nextBytes(salt);

	    // Create PBE parameter specification
	    pbeParam = new PBEParameterSpec(salt, PARAM_ITERATION_COUNT);

	    // Create cipher for encryption
	    try {
		cipher = Cipher.getInstance(ALGORITHM);

	    } catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
	    } catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
	    }
	} else { // decrypt
	    // get the encrypted data/bytes
	    File file = new File(filePath);
	    FileInputStream in = new FileInputStream(file);

	    // read the salt
	    in.read(salt);

	    // read encrypted data
	    int sizeOfData = (int) (file.length() - salt.length);
	    imageByte = new byte[sizeOfData];
	    in.read(imageByte);

	    // Create PBE parameter specification
	    pbeParam = new PBEParameterSpec(salt, PARAM_ITERATION_COUNT);

	    // Create cipher for decryption
	    try {
		cipher = Cipher.getInstance(ALGORITHM);

	    } catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
	    } catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
	    }
	}

    }

    public void encrypt() {
	try {
	    cipher.init(Cipher.ENCRYPT_MODE, key, pbeParam);

	    // put the salt first into the outputs
	    FileOutputStream encryptedOut = new FileOutputStream(new File("encrypted.txt"));
	    encryptedOut.write(salt);

	    // encrypted the data and write the data.
	    try {
		byte[] encryptedImage = cipher.doFinal(imageByte);
		encryptedOut.write(encryptedImage);

	    } catch (IllegalBlockSizeException e) {
		e.printStackTrace();
	    } catch (BadPaddingException e) {
		e.printStackTrace();
	    }
	    encryptedOut.close();
	} catch (InvalidKeyException e) {
	    e.printStackTrace();
	} catch (InvalidAlgorithmParameterException e) {
	    e.printStackTrace();
	} catch (IOException e1) {
	    System.out.println("Fail to writed encrypted data.");
	    System.exit(0);
	}

    }

    @Override
    public void decrypt() {
	try {
	    cipher.init(Cipher.DECRYPT_MODE, key, pbeParam);
	    
	    try {
		// convert the decrypted data back to image
		byte[] originalData = cipher.doFinal(imageByte);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(originalData);
		BufferedImage originalImage = ImageIO.read(byteIn);
		ImageIO.write(originalImage, "png", new File("output.png"));
	    } catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	} catch (InvalidKeyException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidAlgorithmParameterException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void setupKey(char[] password) {
	// Generate PBE key object.
	PBEKeySpec pbeKeySpec = new PBEKeySpec(password);

	// Create secret Key factory based on the specified algorithm
	try {
	    SecretKeyFactory skFactory = SecretKeyFactory.getInstance(ALGORITHM);
	    // Generate the key based on the given password
	    this.key = skFactory.generateSecret(pbeKeySpec);
	} catch (NoSuchAlgorithmException e) {
	    System.out.println("Algorithm error");
	    System.exit(0);
	} catch (InvalidKeySpecException e) {
	}
    }

    private static byte[] imageToByte(BufferedImage image) {
	ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	try {
	    ImageIO.write(image, "png", byteOut);
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	byte[] imageByte = byteOut.toByteArray();
	return imageByte;
    }

}
