package program.security;

import program.User;

public class PasswordResetHandler {

    public static boolean resetPassword(User user, String newPassword) {
        if (user == null || newPassword == null || newPassword.isEmpty()) {
            System.out.println("Invalid user or password.");
            return false;
        }

        // Generate a new hashed password
        Password newPasswordObj = new Password(newPassword);
        user.setPassword(newPasswordObj);

        System.out.println("Password reset successfully for user: " + user.getName());
        return true;
    }
}