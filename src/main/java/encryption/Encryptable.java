package encryption;

import java.io.IOException;

public interface Encryptable<E> {

    public void encrypt();

    public void decrypt();
}
