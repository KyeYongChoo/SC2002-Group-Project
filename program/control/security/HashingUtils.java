package program.control.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p>
 * {@code HashingUtils} provides utility methods for hashing data and generating cryptographic salts.
 * </p>
 *
 * <p>
 * It currently supports:
 * </p>
 * <ul>
 *   <li>Generating a SHA-256 hash from a given input string.</li>
 *   <li>Creating a random salt using a cryptographically strong random number generator.</li>
 * </ul>
 *
 * <p>
 * This class is typically used in contexts where data security, password storage,
 * or verification processes are required.
 * </p>
 */
public class HashingUtils {

    /**
     * Generates a SHA-256 hash for the specified input string.
     *
     * @param input The plain text string to be hashed.
     * @return The resulting hash in hexadecimal string format.
     * @throws RuntimeException if the SHA-256 algorithm is not available on the system.
     */
    public static String hash(String input) {
        MessageDigest digest;
        try {
            // Obtain an instance of the SHA-256 hashing algorithm
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: SHA-256 algorithm not found.");
            throw new RuntimeException(e);
        }

        // Compute the SHA-256 hash of the input string
        byte[] hash = digest.digest(input.getBytes());

        // Convert the resulting byte array to a hexadecimal string
        StringBuilder hexHash = new StringBuilder();
        for (byte b : hash) {
            hexHash.append(String.format("%02x", b));
        }
        return hexHash.toString();
    }

    /**
     * Generates a random salt value.
     * <p>
     * The salt is produced using a cryptographically secure random number generator
     * and returned as a string representing an integer between 0 and 1023 (inclusive).
     * </p>
     *
     * @return A random salt string.
     */
    public static String genSalt() {
        SecureRandom secureRandom = new SecureRandom();
        return String.valueOf(secureRandom.nextInt(1 << 10)); // 1 << 10 = 1024 possible values
    }
}
