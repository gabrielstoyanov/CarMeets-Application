package gss130.carmeets;

import java.security.MessageDigest;

//this class is responsible for hashing the password
//using the md5 hashing method
public class Password_Hashing {
    //function that is hashing the passed password as parameter
    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }
}
