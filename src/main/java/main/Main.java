package main;

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

	    // password for encryption or decryption
	    System.out.println("Enter password");
	    char[] password = console.readPassword();

	    switch (operation) {
	    case ENCRYPTION:
		ImgEncryp imgEncryp = new ImgEncryp(filePath, password, ImgEncryp.Command.ENCRYPT);
		imgEncryp.encrypt();
		break;

	    case DECRYPTION:
		ImgEncryp imgDecryp = new ImgEncryp(filePath, password, ImgEncryp.Command.DECRYPT);
		imgDecryp.decrypt();
		break;
	    default:
		System.out.println(
			"You selected the wrong command, you can only enter e or d for encryption and decryption.");
		System.out.println("Operation failed");
		System.exit(0);
		break;
	    }

	} catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("You did not enter the path of the file that you want to encrypt/decrypt.");
	    System.exit(0);
	} catch (IOException e) {
	    System.out.println("File not exists");
	    System.exit(0);
	}

	System.out.println("Done.");
	System.exit(0);

    }

}
