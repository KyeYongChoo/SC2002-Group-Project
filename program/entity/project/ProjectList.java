package program.entity.project;

import java.util.ArrayList;

/**
 * <p>
 * The {@code ProjectList} class extends {@code ArrayList<Project>} and is designed to manage a collection of
 * {@link Project} objects. It provides a method to retrieve a {@code Project} by its name, ignoring case and trimming
 * any surrounding whitespace.
 * </p>
 *
 * <p>
 * This class simplifies the management of a list of projects and allows for efficient searching of projects by their
 * names.
 * </p>
 *
 * @see program.entity.project.Project
 */
public class ProjectList extends ArrayList<Project> {

    /**
     * Retrieves a project by its name, ignoring case and trimming any surrounding whitespace.
     *
     * @param name the name of the project to retrieve
     * @return the {@code Project} with the specified name, or {@code null} if no such project exists
     */
    public Project get(String name) {
        for (Project project : this) {
            if (project.getName().trim().toUpperCase().equals(name.trim().toUpperCase())) {
                return project;
            }
        }
        return null;
    }
}
