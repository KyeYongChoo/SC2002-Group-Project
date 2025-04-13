package program.boundary.projectIO;

import program.entity.project.Project;
import program.entity.users.User;

public class ProjectFilter {

    public static boolean filterByFlatType(Project project, User.FILTER_SETTING filterSetting) {
        switch (filterSetting) {
            case FLAT_TYPE_2_ROOM:
                return project.getUnits2Room() > 0; // Include only projects with non-zero 2-room units
            case FLAT_TYPE_3_ROOM:
                return project.getUnits3Room() > 0; // Include only projects with non-zero 3-room units
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