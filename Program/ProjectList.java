package Program;

import java.util.ArrayList;

public class ProjectList extends ArrayList<Project> {
    public Project get(String name){
        for (Project project : this){
            if (project.getName().equals(name)){
                return project;
            }
        }
        return null;
    }
    
}
