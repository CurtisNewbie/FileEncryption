package cryptoSpec;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public abstract class ImageCryptoSpec extends CryptoSpec {

    protected byte[] imageByte;
    
    /**
     * Generate SecretKey based on the given password and predefined algorithm.
     * 
     * @param password char[] of password
     */
    protected void setupKey(char[] password) {
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
}
