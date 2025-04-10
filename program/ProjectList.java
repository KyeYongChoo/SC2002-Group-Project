package program;

import java.util.ArrayList;

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
        .forEach(project -> System.out.println(project.getName()));
    }
    
}
