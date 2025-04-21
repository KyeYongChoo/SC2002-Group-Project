package program.control.security;

/**
 * <p>
 * {@code Password} represents a securely stored password by combining hashing, salting, and peppering techniques.
 * </p>
 *
 * <p>
 * This class provides constructors for generating password hashes with random salts as well as for reconstructing
 * existing passwords from stored hash and salt values.
 * </p>
 *
 * <p>
 * The hashing process combines:
 * </p>
 * <ul>
 *   <li>The user-provided or default password</li>
 *   <li>A randomly generated {@link #salt}</li>
 *   <li>A globally stored application {@link Pepper#getPepper() pepper}</li>
 * </ul>
 * before applying a SHA-256 hash function.
 * </p>
 *
 * @see HashingUtils Utility class for hashing and salt generation.
 * @see Pepper Class for retrieving a static pepper value.
 */
public class Password {

    /**
     * The hashed representation of the password combined with its salt and pepper.
     */
    private String hash;

    /**
     * A randomly generated salt unique to this password.
     */
    private String salt;

    /**
     * Constructs a {@code Password} object with a default password string ("password"),
     * generating a new random salt and hashing the combination of password, salt, and pepper.
     */
    public Password(){
        salt = HashingUtils.genSalt();
        hash = HashingUtils.hash("password" + salt + Pepper.getPepper());
    }

    /**
     * Constructs a {@code Password} object from a specified initial password.
     * A new random salt is generated, and the combination of password, salt, and pepper is hashed.
     *
     * @param defaultPassword The initial password to be hashed.
     */
    public Password(String defaultPassword){
        salt = HashingUtils.genSalt();
        hash = HashingUtils.hash(defaultPassword + salt + Pepper.getPepper());
    }

    /**
     * Constructs a {@code Password} object directly from an existing hash and salt.
     * This is useful when reconstructing a password from storage (e.g., database retrieval).
     *
     * @param hash The precomputed hash value.
     * @param salt The associated salt used during the original hashing process.
     */
    public Password(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    /**
     * Returns the stored hash value.
     *
     * @return The hashed password string.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Returns the stored salt value.
     *
     * @return The salt associated with this password.
     */
    public String getSalt() {
        return salt;
    }
}
