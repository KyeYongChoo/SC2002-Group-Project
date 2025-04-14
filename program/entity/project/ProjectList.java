package program.entity.project;

import java.util.ArrayList;
import java.util.stream.Stream;

import program.boundary.projectIO.UserPrefSorting;
import program.entity.users.User;

public class ProjectList extends ArrayList<Project> {
    public Project get(String name){
        for (Project project : this){
            if (project.getName().trim().toUpperCase().equals(name.trim().toUpperCase())){
                return project;
            }
        }
        return null;
    }

    // Currently Unused. Could be used for shorthand later on
    public Stream<Project> userFilterStream(User user) {
        return UserPrefSorting.userFilterStream(user, this);
    }
}
