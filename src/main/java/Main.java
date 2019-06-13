
import java.io.Console;
import java.io.IOException;

import encryption.ImgEncryp;

/**
 * Program runs from here.
 * 
 * @author Curtis
 *
 */
public class Main {

    public static final String DECRYPTION = "d";
    public static final String ENCRYPTION = "e";

    public static void main(String[] args) {

	Console console = System.console();

	try {
	    String operation = args[0];
	    String filePath = args[1];
	    String fileNameExtention = args[2];

	    // password for encryption or decryption
	    System.out.println("Enter password");
	    char[] password = console.readPassword();

	    switch (operation) {
	    case ENCRYPTION:
		ImgEncryp imgEncryp = new ImgEncryp(filePath, password, fileNameExtention, ImgEncryp.Command.ENCRYPT);
		try {
		    imgEncryp.encrypt();
		} catch (IOException e1) {
		    System.out.println("Fail to writed encrypted data.");
		    System.exit(0);
		}
		break;

	    case DECRYPTION:
		ImgEncryp imgDecryp = new ImgEncryp(filePath, password, fileNameExtention, ImgEncryp.Command.DECRYPT);
		try {
		    imgDecryp.decrypt();
		} catch (IOException e2) {
		    System.out.println("Fail to writed decrypted data.");
		    System.exit(0);
		}

		break;
	    default:
		System.out.println(
			"You selected the wrong command, you can only enter e or d for encryption and decryption.");
		System.out.println("Operation failed");
		System.exit(0);
		break;
	    }

	} catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Incomplete Command");
	    System.exit(0);
	} catch (IOException e) {
	    System.out.println("File not exists");
	    System.exit(0);
	}
	System.out.println("Done.");
	System.exit(0);
    }

}
