package program.entity.project;

import java.util.ArrayList;

import program.control.Main;
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

    public static void printVisible(User client){
        Main.projectList.stream()
            .filter(project -> project.isVisibleTo(client))
            .forEach(project -> project.printVisible(client));
    }
    
}
