package program.boundary.projectIO;

import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.User;

public class RoomTypeSelector {
    private static final Scanner sc = AppScanner.getInstance();

    /**
     * Determines the room type the user wants to apply for based on their eligibility
     * and the project's available units.
     * 
     * @param user The user applying for the project.
     * @param project The project the user is applying for.
     * @return The selected ROOM_TYPE (room2 or room3).
     */
    public static ROOM_TYPE selectRoomType(User user, Project project) {
        ROOM_TYPE targetRoomType = ROOM_TYPE.room2;

        // If the user is eligible for 3-room units, determine the room type
        if (user.see3Rooms()) {
            if (project.getUnits3Room() == 0) {
                targetRoomType = ROOM_TYPE.room2; // Only 2-room units are available
            } else if (project.getUnits2Room() == 0) {
                targetRoomType = ROOM_TYPE.room3; // Only 3-room units are available
            } else {
                // Both room types are available, ask the user to choose
                String userInput;
                do {
                    System.out.println("Please select room type (Enter 1 or 2):");
                    System.out.println("1. 2-Room");
                    System.out.println("2. 3-Room");
                    userInput = sc.nextLine();
                } while (!"1".equals(userInput) && !"2".equals(userInput));

                if ("1".equals(userInput)) {
                    targetRoomType = ROOM_TYPE.room2;
                } else if ("2".equals(userInput)) {
                    targetRoomType = ROOM_TYPE.room3;
                }
            }
        }

        return targetRoomType;
    }
}