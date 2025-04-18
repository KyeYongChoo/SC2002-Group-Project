package program.control;

import program.boundary.menu.*;
import program.boundary.menuTemplate.MenuNavigator;
import program.boundary.security.LoginHandler;
import program.control.enquiry.EnquiryList;
import program.control.housingApply.HousingReqList;
import program.control.officerApply.AssignReqList;
import program.entity.caching.DataInitializer;
import program.entity.caching.RecordSaver;
import program.entity.project.ProjectList;
import program.entity.users.User;
import program.entity.users.UserList;

public class Main {
    public static User curUser = null;
    public static UserList applicantList = new UserList();
    public static UserList managerList = new UserList();
    public static UserList officerList = new UserList();
    public static ProjectList projectList = new ProjectList();
    public static HousingReqList housingReqList = new HousingReqList();
    public static EnquiryList enquiryList = new EnquiryList();
    public static AssignReqList assignReqList = new AssignReqList();
    
    public static void main(String[] args) throws Exception{
        DataInitializer.initialise();

        // TESTING
        boolean skipLogin = false;
        User curUser; 
        if (skipLogin){
            curUser = SkipLogin(USER.Applicant);
        } else {
            curUser = LoginHandler.loginUser();
        }

        // PRODUCTION
        // curUser = LoginHandler.loginUser();
        
        MenuNavigator.getInstance().pushMenu(new MainMenu(curUser));
        MenuNavigator.getInstance().start(curUser);

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
            case Officer -> officerList.get(2);// David : 2 is not assigned to any project
            case Manager -> managerList.get(0);
        };
    }
}
