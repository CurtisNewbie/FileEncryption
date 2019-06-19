package encryption;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.PBEParameterSpec;
import javax.imageio.ImageIO;

import cryptoSpec.ImageCryptoSpec;

public class ImgEncryp extends ImageCryptoSpec implements Encryptable {

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
    public ImgEncryp(String filePath, char[] password, String fileNameExtension) throws IOException {
	setupKey(password);
	salt = new byte[8];
	this.fileNameExtension = fileNameExtension;

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
