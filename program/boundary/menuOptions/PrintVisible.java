package program.boundary.menuOptions;

import program.boundary.menuTemplate.MenuItem;
import program.entity.project.ProjectList;
import program.entity.users.User;

public class PrintVisible extends MenuItem {
    
    public PrintVisible(User user) {
        super("View list of projects open to your user group", () -> {
            ProjectList.printVisible(user);
        });
    }
}
