package encryption;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

public class ImgEncryp implements Encryptable, Decryptable {

    private static final String ALGORITHM = "PBEWithMD5AndTripleDES";
    private final int PARAM_ITERATION_COUNT = 100;

    private byte[] imageByte;
    private SecretKey key;
    private byte[] salt;
    private Cipher cipher;
    private PBEParameterSpec pbeParam;
    private String fileNameExtension;

    public enum Command {
	ENCRYPT, DECRYPT
    }

    /**
     * 
     * @param filePath the path of the image file
     * @param password the password that is used to encrypt/decrypt the image.
     * @param com      the command that determines whether the object is created for
     *                 encryption or decryption. <br>
     *                 An enum class is created for this value. The value includes:
     *                 { Command.ENCRYPT, Command.DECRYPT }
     * @throws IOException it throws IOException when this class fails to read the
     *                     file.
     */
    public ImgEncryp(String filePath, char[] password, String fileNameExtension, Command com) throws IOException {
	setupKey(password);
	salt = new byte[8];
	this.fileNameExtension = fileNameExtension;

	// For encryption
	if (com == Command.ENCRYPT) {

	    // get the image
	    BufferedImage image = ImageIO.read(new File(filePath));
	    this.imageByte = imageToByte(image, this.fileNameExtension);

	    // Randomly generate byte[8] (salt).
	    new Random().nextBytes(salt);

	    // Create PBE parameter specification
	    pbeParam = new PBEParameterSpec(salt, PARAM_ITERATION_COUNT);

	    // Create cipher for encryption
	    try {
		cipher = Cipher.getInstance(ALGORITHM);
	    } catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
	    }
	} else { // Command.DECRYPT

	    // get the encrypted data/bytes
	    File file = new File(filePath);
	    FileInputStream inputStream = new FileInputStream(file);

	    // read the salt
	    inputStream.read(salt);

	    // read encrypted data
	    int sizeOfData = (int) (file.length() - salt.length);
	    imageByte = new byte[sizeOfData];
	    inputStream.read(imageByte);

	    // Create PBE parameter specification
	    pbeParam = new PBEParameterSpec(salt, PARAM_ITERATION_COUNT);

	    // Create cipher for decryption
	    try {
		cipher = Cipher.getInstance(ALGORITHM);
	    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	    }
	}
    }

    /**
     * Encrypt the file, using predefined algorithm. Once the encryption is
     * finished, it will generate a file called "encrypted.txt" which contains the
     * encrypted data in forms of bytes.
     * 
     * @throws IOException it throws IOEexception when this class fails to create
     *                     the "encrypted.txt" file.
     */
    @Override
    public void encrypt() throws IOException {
	try {
	    cipher.init(Cipher.ENCRYPT_MODE, key, pbeParam);

	    // put the salt first into the outputs
	    FileOutputStream encryptedOutputStream = new FileOutputStream(new File("encrypted.txt"));
	    encryptedOutputStream.write(salt);

	    // encrypted the data and write the data.
	    try {
		byte[] encryptedImage = cipher.doFinal(imageByte);
		encryptedOutputStream.write(encryptedImage);

	    } catch (IllegalBlockSizeException e) {
		e.printStackTrace();
	    } catch (BadPaddingException e) {
		e.printStackTrace();
	    }
	    encryptedOutputStream.close();
	} catch (InvalidKeyException e) {
	    System.out.println("Invalid key, wrong data length and so on. Exiting...");
	    System.exit(0);
	} catch (InvalidAlgorithmParameterException e) {
	}
    }

    /**
     * Decrypt the file, using predefined algorithm. Once the decryption is
     * finished, it will generate a file called "output.txt" which contains the
     * encrypted data in forms of bytes.
     * 
     * @throws IOException it throws IOEexception when this class fails to create
     *                     the "encrypted.txt" file.
     */
    @Override
    public void decrypt() throws IOException {
	try {
	    cipher.init(Cipher.DECRYPT_MODE, key, pbeParam);

	    try {
		// convert the decrypted data back to image
		byte[] originalData = cipher.doFinal(imageByte);
		ByteArrayInputStream byteIn = new ByteArrayInputStream(originalData);
		BufferedImage originalImage = ImageIO.read(byteIn);
		ImageIO.write(originalImage, fileNameExtension, new File("output." + fileNameExtension));
	    } catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} catch (InvalidKeyException e) {
	    System.out.println("Invalid key, wrong data length and so on. Exiting...");
	    System.exit(0);
	} catch (InvalidAlgorithmParameterException e) {
	}
    }

    /**
     * Generate SecretKey based on the given password and predefined algorithm.
     * 
     * @param password char[] of password
     */
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

    /**
     * Covet buffered image into array of byte.
     * 
     * @param image             buffered image
     * @param fileNameExtension the file name extension
     * @return array of byte
     * @throws IOException throws IOException when the ImageIO.write method has
     *                     errors.
     */
    private static byte[] imageToByte(BufferedImage image, String fileNameExtension) throws IOException {
	ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	ImageIO.write(image, fileNameExtension, byteOut);
	byte[] imageByte = byteOut.toByteArray();
	return imageByte;
    }

}
