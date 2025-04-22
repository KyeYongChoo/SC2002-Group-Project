package program.entity.users;

import java.util.ArrayList;

import program.control.Main;

/**
 * <p>
 * The {@code UserList} class extends {@link ArrayList} and manages a collection of {@link User} objects.
 * This class provides methods to search for a user by their user ID or name, and to represent the list
 * of users as a string.
 * </p>
 *
 * <p>
 * The {@code get} method can either search through all user lists (e.g., manager list, officer list,
 * applicant list) or only the current list of users based on the parameter provided.
 * </p>
 */
public class UserList extends ArrayList<User> {

    /**
     * Retrieves the first user matching the given User ID by searching through all available user lists.
     *
     * @param UserId the user ID to search for
     * @return the {@link User} instance with the specified User ID, or {@code null} if not found
     */
    public User get(String UserId) {
        return get(UserId, true);
    }

    /**
     * Retrieves the first user matching the given User ID.
     * It can search through all user lists or just the current list, depending on the {@code searchAllLists} flag.
     *
     * @param UserId the user ID to search for
     * @param searchAllLists when {@code true}, searches all user lists; otherwise, searches only the current list
     * @return the {@link User} instance with the specified User ID, or {@code null} if not found
     */
    public User get(String UserId, boolean searchAllLists) {
        if (searchAllLists) {
            // Search through manager list
            for (User user : Main.managerList) {
                if (user.getUserId().equals(UserId)) {
                    return user;
                }
            }
            // Search through officer list
            for (User user : Main.officerList) {
                if (user.getUserId().equals(UserId)) {
                    return user;
                }
            }
            // Search through applicant list
            for (User user : Main.applicantList) {
                if (user.getUserId().equals(UserId)) {
                    return user;
                }
            }
        } else {
            // Search only through the current user list
            for (User user : this) {
                if (user.getUserId().equals(UserId)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the first user matching the specified name.
     *
     * @param Name the name of the user to search for
     * @return the {@link User} instance with the specified name, or {@code null} if not found
     */
    public User getByName(String Name) {
        for (User user : this) {
            if (user.getName().equals(Name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns a string representation of the list of users in JSON-like format.
     *
     * @return a string representation of the user list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (User user : this) {
            sb.append(user.toString());
            if (!user.equals(this.get(this.size() - 1))) {
                sb.append(",");
            }
        }
        sb.append("\"");
        return sb.toString();
    }
}
