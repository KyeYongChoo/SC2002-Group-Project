package program.boundary.security;
import program.control.security.HashingUtils;
import program.control.security.Password;
import program.entity.users.User;

import static program.control.security.Pepper.getPepper;

public class PasswordVerifier {
    public static boolean verify(User user, String passwordInput){
        Password password = user.getPassword();
        return password.getHash().equals(HashingUtils.hash(passwordInput + password.getSalt() + getPepper()));
    }
}
