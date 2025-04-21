package program.boundary.security;
import program.control.security.HashingUtils;
import program.control.security.Password;
import program.entity.users.User;

import static program.control.security.Pepper.getPepper;

/**
 * <p>
 * The {@code PasswordVerifier} class is responsible for verifying whether a provided password
 * matches the stored hashed password for a given {@link User}. It hashes the user-provided password,
 * combining it with a stored salt and a global pepper value, then compares the result with the stored hash.
 * </p>
 */
public class PasswordVerifier {
    /**
     * <p>
     * Verifies if the password entered by the user matches the stored hashed password for a given {@link User}.
     * This is done by retrieving the user's stored {@link Password} object, which contains the hash and salt,
     * and then hashing the combination of the input password, the user's salt, and a global pepper value.
     * The result is compared with the stored hash to determine if the passwords match.
     * </p>
     *
     * @param user          <p>The {@link User} whose password needs to be verified.</p>
     * @param passwordInput <p>The password input provided by the user for verification.</p>
     * @return <p>{@code true} if the hashed input password matches the stored hash, indicating a successful verification;</p>
     * <p>{@code false} otherwise.</p>
     */
    public static boolean verify(User user, String passwordInput){
        // <p>Retrieve the user's stored Password object which contains the hash and salt.</p>
        Password password = user.getPassword();

        // <p>Hash the input password, appending the user's salt and a global pepper value, then compare it with the stored hash.</p>
        return password.getHash().equals(HashingUtils.hash(passwordInput + password.getSalt() + getPepper()));
    }
}
