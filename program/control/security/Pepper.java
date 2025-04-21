package program.control.security;

/**
 * <p>
 * {@code Pepper} provides a static secret value that is combined with user passwords
 * during the hashing process to enhance security.
 * </p>
 *
 * <p>
 * A pepper is similar to a salt but differs in that it is kept secret within the application
 * code rather than being stored alongside the hash. This makes brute-force and dictionary
 * attacks significantly harder, even if the database is compromised.
 * </p>
 *
 * <p>
 * The pepper should ideally be stored securely and not hard-coded directly in the source code
 * for production applications. However, for simplicity, this implementation returns a fixed string.
 * </p>
 *
 * @see Password Class that uses {@code Pepper} as part of its hashing mechanism.
 * @see HashingUtils Utility class for hashing passwords.
 */
public class Pepper {

    /**
     * Returns the static pepper string used during password hashing.
     *
     * @return A secret {@code String} value that is appended during password hashing.
     */
    public static String getPepper() {
        return "SC2002 FOLKS";
    }
}
