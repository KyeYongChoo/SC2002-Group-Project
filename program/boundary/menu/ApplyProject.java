package program.boundary.menu;

import java.util.Scanner;
import program.boundary.AppScanner;
import program.boundary.menuTemplate.MenuGroup;
import program.control.Main;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.Manager;
import program.entity.users.User;

public class ApplyProject extends MenuGroup {
    private static final Scanner sc = AppScanner.getInstance();
    public ApplyProject(User user) {
        super("Apply to a project", 
            // if user aint a manager or satisfy one of the BTO requirements, they can apply for projects
            dummyVar -> !(user instanceof Manager) && 
                        ((user.see2Rooms())||(user.see3Rooms())) 
        );
        Main.projectList.stream().filter(
                    project -> 
                        project.isVisibleTo(user) 
                        && !project.conflictInterest(user)
                        && ((user.see2Rooms() && project.getUnits2Room() > 0) || (user.see3Rooms() && project.getUnits3Room() > 0))
        ).forEach(
            project -> {
                this.addMenuItem(
                    project.getName(), 
                    () -> {
                        String userInput;
                        ROOM_TYPE targetRoomType = ROOM_TYPE.room2;
                        if (user.see3Rooms()){
                            if (project.getUnits3Room() == 0) targetRoomType = ROOM_TYPE.room2;
                            else if (project.getUnits2Room() == 0) targetRoomType = ROOM_TYPE.room3;
                            else {
                                do { 
                                    System.out.println("Please select room type (Enter 1 or 2):");
                                    System.out.println("1. 2-Room");
                                    System.out.println("2. 3-Room");
                                    userInput = sc.nextLine();
                                } while (!"1".equals(userInput) && !"2".equals(userInput));
                                if ("1".equals(userInput)) targetRoomType = ROOM_TYPE.room2;
                                if ("2".equals(userInput)) targetRoomType = ROOM_TYPE.room3;
                            }
                        }
                        Main.reqList.add(user, project, targetRoomType);
                        System.out.println("Application submitted successfully.");
                    }
                );
            }
        );
    }
                    // ProjectList.printVisible(officer);
                    // do { 
                    //     System.out.println("Please enter name of project: ");
                    //     userInput = sc.nextLine();
                    //     targetProject = projectList.get(userInput);
                    // } while (targetProject == null);
                    
                    // if (officer.getProject().equals(targetProject)) {
                    //     System.out.println("Sorry, you are already in charge of this HDB");
                        
                    // }
                    // if ((officer.see3Rooms() && targetProject.getUnits3Room() == 0 && targetProject.getUnits3Room() == 0)
                    // || (officer.see2Rooms() && targetProject.getUnits2Room() == 0)){
                    //     System.out.println("Sorry. No more vacant rooms");
                    //     break;
                    // }

                    // ROOM_TYPE targetRoomType = null;
                    // if (officer.see3Rooms()){
                    //     if (targetProject.getUnits3Room() == 0) targetRoomType = ROOM_TYPE.room2;
                    //     else if (targetProject.getUnits2Room() == 0) targetRoomType = ROOM_TYPE.room3;
                    //     else {
                    //         do { 
                    //             System.out.println("Please select room type (Enter 1 or 2):");
                    //             System.out.println("1. 2-Room");
                    //             System.out.println("2. 3-Room");
                    //             userInput = sc.nextLine();
                    //         } while (!"1".equals(userInput) && !"2".equals(userInput));
                    //         if ("1".equals(userInput)) targetRoomType = ROOM_TYPE.room2;
                    //         if ("2".equals(userInput)) targetRoomType = ROOM_TYPE.room3;
                    //     }
                    // }
                    // else if (officer.see2Rooms()) targetRoomType = ROOM_TYPE.room2;
                    // else throw new Exception("Check out the input part of MainActivity this case should never be reachable");
                    // if (!reqList.add(officer,targetProject,targetRoomType)){
                    //     System.out.println("System did not add application.");
                    // }
                    // if (!client.see2Rooms() && !client.see3Rooms()){
                    //     System.out.println("Sorry, you are not eligible to apply for any kind of HDB");
                    //     break;
                    // }
                    // ProjectList.printVisible(client);
                    // do { 
                    //     System.out.println("Please enter name of project: ");
                    //     userInput = sc.nextLine();
                    //     targetProject = projectList.get(userInput);
                    // } while (targetProject == null);
                    
                    // if ((client.see3Rooms() && targetProject.getUnits3Room() == 0 && targetProject.getUnits3Room() == 0)
                    // || (client.see2Rooms() && targetProject.getUnits2Room() == 0)){
                    //     System.out.println("Sorry. No more vacant rooms");
                    //     break;
                    // }

                    // ROOM_TYPE targetRoomType = null;
                    // if (client.see3Rooms()){
                    //     if (targetProject.getUnits3Room() == 0) targetRoomType = ROOM_TYPE.room2;
                    //     else if (targetProject.getUnits2Room() == 0) targetRoomType = ROOM_TYPE.room3;
                    //     else {
                    //         do { 
                    //             System.out.println("Please select room type (Enter 1 or 2):");
                    //             System.out.println("1. 2-Room");
                    //             System.out.println("2. 3-Room");
                    //             userInput = sc.nextLine();
                    //         } while (!"1".equals(userInput) && !"2".equals(userInput));
                    //         if ("1".equals(userInput)) targetRoomType = ROOM_TYPE.room2;
                    //         if ("2".equals(userInput)) targetRoomType = ROOM_TYPE.room3;
                    //     }
                    // }
                    // else if (client.see2Rooms()) targetRoomType = ROOM_TYPE.room2;
                    // else throw new Exception("Check out the input part of MainActivity this case should never be reachable");
                    // if (!reqList.add(client,targetProject,targetRoomType)){
                    //     System.out.println("System did not add application.");
                    // }
                    
                    // break;
}
