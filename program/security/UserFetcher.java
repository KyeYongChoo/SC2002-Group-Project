package program.security;

import program.Main;
import program.User;

public class UserFetcher {
    public static User fetch(String NRIC, String password) {
        User client = Main.applicantList.get(NRIC);
        if (client == null) client = Main.officerList.get(NRIC);
        if (client == null) client = Main.managerList.get(NRIC);

        if (client != null && PasswordVerifier.verify(client, password)) {
            return client;
        }
        return null;
    }
}
