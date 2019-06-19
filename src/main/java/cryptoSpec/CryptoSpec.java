package cryptoSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;

public abstract class CryptoSpec {
    
    protected final String ALGORITHM = "PBEWithMD5AndTripleDES";
    protected final int PARAM_ITERATION_COUNT = 100;

    protected SecretKey key;
    protected byte[] salt;
    protected Cipher cipher;
    protected PBEParameterSpec pbeParam;
    protected String fileNameExtension;

}
