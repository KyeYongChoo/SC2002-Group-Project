package program.control.security;

import program.boundary.security.PasswordVerifier;
import program.control.Main;
import program.entity.users.User;

public class UserFetcher {
    /*
     * Also does verification of password
     * @param NRIC The NRIC of the user to be fetched
     * @param password The password of the user to be fetched
     * @return The user object if found and password is correct, null otherwise
     */
    public static User fetch(String NRIC, String password) {
        User client = Main.applicantList.get(NRIC);
        if (client == null) client = Main.officerList.get(NRIC);
        // if officer promoted to Manager, then there will both be an officer Record and a ManagerRecord. 
        // Intentionally so that officer may never access its own record anymore
        // nvm looks like i misread the requirements but ill leave this in anyway
        if (client == null || Main.managerList.get(NRIC)!= null) client = Main.managerList.get(NRIC);

        if (client != null && PasswordVerifier.verify(client, password)) {
            return client;
        }
        return null;
    }
    /*
     * Fetches the user object without verifying password
     * @param NRIC The NRIC of the user to be fetched
     * @return The user object if found, null otherwise
     * @note This method does not verify the password, so it should be used with caution.
     */
    public static User fetch(String NRIC) {
        User client = Main.applicantList.get(NRIC);
        if (client == null) client = Main.officerList.get(NRIC);
        if (client == null) client = Main.managerList.get(NRIC);
        return client;
    }
}
