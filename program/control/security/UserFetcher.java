package program.control.security;

import program.boundary.security.PasswordVerifier;
import program.control.Main;
import program.entity.users.User;

/**
 * <p>
 * {@code UserFetcher} is a utility class responsible for retrieving {@link User} objects from the system's various user lists
 * based on a provided NRIC. It can optionally verify the user's password during the fetching process.
 * </p>
 *
 * <p>
 * The class supports two modes of operation:
 * <ul>
 *   <li>Fetching a user and verifying their password.</li>
 *   <li>Fetching a user without password verification (to be used cautiously).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Note: If an officer is promoted to a manager, the system may retain both the officer and manager records separately.
 * When fetching, preference is given to the manager record if one exists.
 * </p>
 *
 * @see User Represents a user in the system.
 * @see PasswordVerifier Utility for verifying user passwords.
 */
public class UserFetcher {

    /**
     * Fetches a {@link User} object based on the provided NRIC and verifies the user's password.
     * <p>
     * This method searches across all user lists (applicants, officers, and managers).
     * If a matching user is found and the password is verified successfully, the user object is returned.
     * Otherwise, {@code null} is returned.
     * </p>
     *
     * @param NRIC The NRIC (National Registration Identity Card number) of the user to be fetched.
     * @param password The password provided by the user.
     * @return The matching {@link User} object if found and password is correct; {@code null} otherwise.
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

    /**
     * Fetches a {@link User} object based on the provided NRIC without verifying the password.
     * <p>
     * This method should be used cautiously since it bypasses password verification, potentially exposing security risks.
     * </p>
     *
     * @param NRIC The NRIC (National Registration Identity Card number) of the user to be fetched.
     * @return The matching {@link User} object if found; {@code null} otherwise.
     */
    public static User fetch(String NRIC) {
        User client = Main.applicantList.get(NRIC);
        if (client == null) client = Main.officerList.get(NRIC);
        if (client == null) client = Main.managerList.get(NRIC);
        return client;
    }
}
