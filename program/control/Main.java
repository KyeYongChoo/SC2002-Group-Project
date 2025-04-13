package program.control;

import program.boundary.menu.*;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.security.LoginHandler;
import program.control.interclass.AssignReqList;
import program.control.interclass.EnquiryList;
import program.control.interclass.HousingReqList;
import program.entity.caching.DataInitializer;
import program.entity.caching.RecordSaver;
import program.entity.project.ProjectList;
import program.entity.users.User;
import program.entity.users.UserList;

public class Main {
    public static UserList applicantList = new UserList();
    public static UserList managerList = new UserList();
    public static UserList officerList = new UserList();
    public static ProjectList projectList = new ProjectList();
    public static HousingReqList reqList = new HousingReqList();
    public static EnquiryList enquiryList = new EnquiryList();
    public static AssignReqList assignReqList = new AssignReqList();
    
    public static void main(String[] args) throws Exception{
        DataInitializer.initialise();

        // TESTING
        boolean skipLogin = true;
        User client; 
        if (skipLogin){
            client = SkipLogin(USER.Applicant);
        } else {
            client = LoginHandler.loginUser();
        }

        // PRODUCTION
        // User client = LoginHandler.loginUser();
        
        MenuNavigator.getInstance().pushMenu(new MainMenu(client));
        MenuNavigator.getInstance().start(client);

        RecordSaver.save();
    }

    private enum USER{
        Applicant,
        Officer,
        Manager
    }
    
    public static User SkipLogin(USER choice){
        return switch (choice) {
            case Applicant -> applicantList.get(0);
            case Officer -> officerList.get(0);
            case Manager -> managerList.get(0);
        };
    }
}
