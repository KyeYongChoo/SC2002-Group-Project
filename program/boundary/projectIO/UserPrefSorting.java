package program.boundary.projectIO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import program.control.housingApply.HousingReq;
import program.entity.project.Project;
import program.entity.users.User;

public class UserPrefSorting {

    public static List<Project> userFilters(User user, List<Project> projects) {
        return userFilterStream(user, projects)
            .collect(Collectors.toList());
    }

    public static Stream<Project> userFilterStream(User user, List<Project> projects) {
        return projects.stream()
            .filter(project -> ProjectFilter.filterByFlatType(project, user.getFilterSetting()))    // Additional filtering for flat types
            .sorted((p1, p2) -> ProjectFilter.compareProjects(p1, p2, user.getFilterSetting()));    // Sort based on filter setting
    }

    public static List<HousingReq> sortHousingReqs(User user, List<HousingReq> reqList) {
    return reqList.stream()
        .filter(req -> ProjectFilter.filterByFlatType(req.getProject(), user.getFilterSetting()))
        .sorted((r1, r2) -> ProjectFilter.compareProjects(r1.getProject(), r2.getProject(), user.getFilterSetting()))
        .collect(Collectors.toList());
    }

}
