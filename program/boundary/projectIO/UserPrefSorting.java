package program.boundary.projectIO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import program.control.housingApply.HousingReq;
import program.entity.project.Project;
import program.entity.users.Manager;
import program.entity.users.User;

public class UserPrefSorting {

    public static List<Project> userFilters(User user, List<Project> projects) {
        return userFilterStream(user, projects)
            .collect(Collectors.toList());
    }

    public static Stream<Project> userFilterStream(User user, List<Project> projects) {
        return projects.stream()
            .filter(project -> filterByFlatType(project, user))    // Additional filtering for flat types
            .sorted((p1, p2) -> compareProjects(p1, p2, user.getFilterSetting()));    // Sort based on filter setting
    }

    public static List<HousingReq> sortHousingReqs(User user, List<HousingReq> reqList) {
        return reqList.stream()
        .filter(req -> filterByFlatType(req.getProject(), user))
        .sorted((r1, r2) -> compareProjects(r1.getProject(), r2.getProject(), user.getFilterSetting()))
        .collect(Collectors.toList());
    }

    public static boolean filterByFlatType(Project project, User user) {
        switch (user.getFilterSetting()) {
            case FLAT_TYPE_2_ROOM:
                return project.getUnits2Room() > 0; // Include only projects with non-zero 2-room units
            case FLAT_TYPE_3_ROOM:
                return project.getUnits3Room() > 0; // Include only projects with non-zero 3-room units
            case OWN_PROJECTS_ONLY:
                return project.isManager((Manager) user);
            default:
                return true; // No additional filtering for other settings
        }
    }

    public static int compareProjects(Project p1, Project p2, User.FILTER_SETTING filterSetting) {
        switch (filterSetting) {
            case LOCATION:
                return p1.getNeighbourhood().compareToIgnoreCase(p2.getNeighbourhood());
            case FLAT_TYPE_2_ROOM:
                return Integer.compare(p2.getUnits2Room(), p1.getUnits2Room()); // Sort by 2-room units in descending order
            case FLAT_TYPE_3_ROOM:
                return Integer.compare(p2.getUnits3Room(), p1.getUnits3Room()); // Sort by 3-room units in descending order
            case ALPHABETICAL:
            default:
                return p1.getName().compareToIgnoreCase(p2.getName());
        }
    }
}
