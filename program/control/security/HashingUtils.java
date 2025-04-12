package program.control.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashingUtils {

    public static String hash(String input){
        MessageDigest digest = null;
        try{
            // Get an instance of SHA-256
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Compute hash
        byte[] hash = digest.digest(input.getBytes());

        // Convert to hexadecimal format
        StringBuilder hexHash = new StringBuilder();
        for (byte b : hash) {
            hexHash.append(String.format("%02x", b)); // Convert byte to hex
        }
        return hexHash.toString();
    }

    public static String genSalt(){
        SecureRandom secureRandom = new SecureRandom();
        return String.valueOf(secureRandom.nextInt(1 << 10)); 
        // 1 << 10 is 1KB 
    }
    
}
