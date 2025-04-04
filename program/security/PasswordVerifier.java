package program.security;

import program.User;

public class PasswordVerifier {
    public static boolean verify(User user, String passwordInput){
        Password password = user.getPassword();
        return password.getHash().equals(HashingUtils.hash(passwordInput + password.getSalt() + Pepper.getPepper()));
    }
}
