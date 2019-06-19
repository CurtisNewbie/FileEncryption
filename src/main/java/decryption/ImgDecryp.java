package decryption;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.PBEParameterSpec;
import javax.imageio.ImageIO;

import cryptoSpec.ImageCryptoSpec;

public class ImgDecryp extends ImageCryptoSpec implements Decryptable {

    private byte[] imageByte;

    public ImgDecryp(String filePath, char[] password, String fileNameExtension) throws IOException {
	setupKey(password);
	salt = new byte[8];
	this.fileNameExtension = fileNameExtension;
	
	// get the encrypted data/bytes
	File file = new File(filePath);
	BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

	// read the salt
	inputStream.read(salt);

	// read encrypted data
	int sizeOfData = (int) (file.length() - salt.length);
	imageByte = new byte[sizeOfData];
	inputStream.read(imageByte);
	inputStream.close();

	// Create PBE parameter specification
	pbeParam = new PBEParameterSpec(salt, PARAM_ITERATION_COUNT);

	// Create cipher for decryption
	try {
	    cipher = Cipher.getInstance(ALGORITHM);
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	}
    }

    /**
     * Decrypt the file, using predefined algorithm. Once the decryption is
     * finished, it will generate a file called "output.[givenFileNameExtension]"
     * which is the one that is previously encrypted.
     * 
     * @throws IOException it throws IOEexception when this class fails to create
     *                     the "output.[fileNameExtension]" file.
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
}
