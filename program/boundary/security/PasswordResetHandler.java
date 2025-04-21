package program.boundary.security;
import program.control.security.Password;
import program.entity.users.User;

/**
 * <p>
 * The {@code PasswordResetHandler} class is responsible for handling the process of resetting a user's password.
 * It allows a password reset by validating the user and the new password input, then hashing the new password
 * before updating the user's password.
 * </p>
 */
public class PasswordResetHandler {

    /**
     * <p>
     * Resets the password for a specified {@link User}. It performs basic validation to ensure the user object and
     * new password are valid. If valid, it generates a hashed version of the new password and updates the user's password.
     * </p>
     *
     * @param user        <p>The {@link User} whose password is to be reset.</p>
     * @param newPassword <p>The new password to be set for the user.</p>
     * @return <p>{@code true} if the password was successfully reset, or {@code false} if the reset failed
     *         (e.g., due to invalid user or password).</p>
     */
    public static boolean resetPassword(User user, String newPassword) {
        // <p>Validate the user and password input to ensure they are not null or empty.</p>
        if (user == null || newPassword == null || newPassword.isEmpty()) {
            System.out.println("Invalid user or password.");
            return false;  // <p>Return false to indicate failure in resetting the password.</p>
        }

        // <p>Generate a hashed version of the new password using the Password utility class.</p>
        Password newPasswordObj = new Password(newPassword);

        // <p>Set the user's password to the newly hashed password.</p>
        user.setPassword(newPasswordObj);

        System.out.println("Password reset successfully for user: " + user.getName());
        return true;  // <p>Return true to indicate that the password reset was successful.</p>
    }
}
