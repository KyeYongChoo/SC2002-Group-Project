package program.boundary.projectIO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import program.control.housingApply.HousingReq;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.User;

/**
 * <p>
 * The {@code UserPrefSorting} class provides utility methods for filtering and sorting
 * lists of {@link Project} and {@link HousingReq} objects based on a {@link User}'s preferences.
 * </p>
 * <p>
 * It applies the user's filter settings for criteria such as location, flat type (2-room or 3-room),
 * and alphabetical order. The class also includes a filter for showing only projects managed by a specific user.
 * </p>
 */
public class UserPrefSorting {

    /**
     * <p>
     * Filters and sorts a list of {@link Project} objects according to a {@link User}'s preferences.
     * The method first filters the projects based on the user's flat type preference, if applicable,
     * by using the {@link #filterByFlatType(Project, User)} method. Then, it sorts the filtered projects
     * according to the user's overall filter setting (location, flat type, or alphabetical order)
     * by calling the {@link #compareProjects(Project, Project, User.FILTER_SETTING)} method.
     * </p>
     *
     * @param user     the {@link User} whose preferences will guide the filtering and sorting.
     * @param projects the list of {@link Project} objects to be filtered and sorted.
     * @return a new {@link List} of {@link Project} objects that have been filtered and sorted
     *         based on the user's preferences.
     */
    public static List<Project> userFilters(User user, List<Project> projects) {
        return userFilterStream(user, projects)
                .collect(Collectors.toList());
    }

    /**
     * <p>
     * Creates a {@link Stream} of {@link Project} objects filtered and sorted based on a {@link User}'s preferences.
     * This method is similar to {@link #userFilters(User, List)}, but returns a {@code Stream} instead of a {@code List},
     * enabling further stream operations.
     * </p>
     *
     * @param user     the {@link User} whose preferences will guide the filtering and sorting.
     * @param projects the list of {@link Project} objects to be filtered and sorted.
     * @return a {@link Stream} of {@link Project} objects that have been filtered and sorted according to the user's preferences.
     */
    public static Stream<Project> userFilterStream(User user, List<Project> projects) {
        return projects.stream()
                .filter(project -> filterByFlatType(project, user))    // Filter projects based on the user's preferred flat type.
                .sorted((p1, p2) -> compareProjects(p1, p2, user.getFilterSetting()));    // Sort the filtered projects based on the user's filter setting.
    }

    /**
     * <p>
     * Filters and sorts a list of {@link HousingReq} objects based on a {@link User}'s preferences.
     * This method filters the requests according to the availability of the requested flat type in the associated project,
     * and then sorts them according to the user's filter setting for the projects.
     * </p>
     *
     * @param user    the {@link User} whose preferences will guide the filtering and sorting.
     * @param reqList the list of {@link HousingReq} objects to be filtered and sorted.
     * @return a new {@link List} of {@link HousingReq} objects that have been filtered and sorted according to the user's preferences.
     */
    public static List<HousingReq> sortHousingReqs(User user, List<HousingReq> reqList) {
        return reqList.stream()
                .filter(req -> filterByFlatType(req.getProject(), user))
                .sorted((r1, r2) -> compareProjects(r1.getProject(), r2.getProject(), user.getFilterSetting()))
                .collect(Collectors.toList());
    }

    /**
     * <p>
     * Filters a {@link Project} based on a {@link User}'s preferred flat type setting.
     * If the user has set a preference for 2-room units, it will include projects with available 2-room units.
     * If the preference is for 3-room units, it will include projects with available 3-room units.
     * If the user prefers only their own projects, it will include projects managed by the user (if the user is a {@link Manager}).
     * For all other filter settings, it will include all projects.
     * </p>
     *
     * @param project the {@link Project} to be filtered.
     * @param user    the {@link User} whose filter setting will be applied.
     * @return {@code true} if the project meets the filter criteria, {@code false} otherwise.
     */
    public static boolean filterByFlatType(Project project, User user) {
        switch (user.getFilterSetting()) {
            case FLAT_TYPE_2_ROOM:
                return project.getUnits2Room() > 0; // Include projects with at least one 2-room unit.
            case FLAT_TYPE_3_ROOM:
                return project.getUnits3Room() > 0; // Include projects with at least one 3-room unit.
            case OWN_PROJECTS_ONLY:
                return user instanceof Manager && project.isManager((Manager) user); // Include projects managed by the user if they are a Manager.
            default:
                return true; // Include all projects for other filter settings.
        }
    }

    /**
     * <p>
     * Compares two {@link Project} objects based on a {@link User}'s filter setting.
     * The comparison behavior changes based on the selected filter setting:
     * </p>
     * <ul>
     * <li>{@code LOCATION}: Projects are compared based on their neighborhood (case-insensitive).</li>
     * <li>{@code FLAT_TYPE_2_ROOM}: Projects are compared based on the number of available 2-room units, in descending order.</li>
     * <li>{@code FLAT_TYPE_3_ROOM}: Projects are compared based on the number of available 3-room units, in descending order.</li>
     * <li>{@code ALPHABETICAL} or default: Projects are compared alphabetically by name (case-insensitive).</li>
     * </ul>
     *
     * @param p1            the first {@link Project} to compare.
     * @param p2            the second {@link Project} to compare.
     * @param filterSetting the {@link User.FILTER_SETTING} that defines the comparison criteria.
     * @return a negative integer, zero, or a positive integer if the first project is less than, equal to, or greater than the second.
     */
    public static int compareProjects(Project p1, Project p2, User.FILTER_SETTING filterSetting) {
        switch (filterSetting) {
            case LOCATION:
                return p1.getNeighbourhood().compareToIgnoreCase(p2.getNeighbourhood()); // Compare based on neighborhood.
            case FLAT_TYPE_2_ROOM:
                return Integer.compare(p2.getUnits2Room(), p1.getUnits2Room()); // Compare based on the number of 2-room units (descending order).
            case FLAT_TYPE_3_ROOM:
                return Integer.compare(p2.getUnits3Room(), p1.getUnits3Room()); // Compare based on the number of 3-room units (descending order).
            case ALPHABETICAL:
            default:
                return p1.getName().compareToIgnoreCase(p2.getName()); // Compare based on project name (alphabetically).
        }
    }
}
