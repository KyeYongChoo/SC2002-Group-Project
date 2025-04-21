package program.boundary.projectIO;

import java.util.Scanner;

import program.boundary.console.AppScanner;
import program.entity.project.Project;
import program.entity.project.Project.ROOM_TYPE;
import program.entity.users.User;

/**
 * <p>
 * The {@code RoomTypeSelector} class allows a {@link User} to select the type of room
 * they wish to apply for within a specified {@link Project}. The available room types are
 * determined by the user's eligibility (whether they can apply for 3-room units) and the
 * availability of different room types in the project.
 * </p>
 *
 * <p>
 * This selection process ensures that users can only apply for room types that they are
 * eligible for, and that the availability of units within the project is taken into account
 * when making the selection.
 * </p>
 */
public class RoomTypeSelector {

    /**
     * <p>
     * {@code sc} is a static, final instance of the {@link Scanner} class, provided
     * by {@link AppScanner}. It is used to capture user input from the console when
     * prompting the user to select a room type.
     * </p>
     */
    private static final Scanner sc = AppScanner.getInstance();

    /**
     * <p>
     * Determines the room type the user wants to apply for, based on their eligibility and
     * the availability of room types within the specified {@link Project}.
     * </p>
     *
     * <p>
     * The user's eligibility is checked to see if they can apply for 3-room units. The project
     * is also checked to see which room types are available. If both room types are available,
     * the user is prompted to select one. The method returns the selected {@link ROOM_TYPE},
     * either {@code ROOM_TYPE.room2} or {@code ROOM_TYPE.room3}.
     * </p>
     *
     * @param user    the {@link User} applying for the project
     * @param project the {@link Project} the user is applying for
     * @return the selected {@link ROOM_TYPE}, either {@code ROOM_TYPE.room2} or {@code ROOM_TYPE.room3}
     */
    public static ROOM_TYPE selectRoomType(User user, Project project) {
        // Default room type is 2-room
        ROOM_TYPE targetRoomType = ROOM_TYPE.room2;

        // Check if the user is eligible for 3-room units
        if (user.see3Rooms()) {
            // If the user is eligible for 3-room units, check availability
            if (project.getUnits3Room() == 0) {
                targetRoomType = ROOM_TYPE.room2; // Only 2-room units available
            } else if (project.getUnits2Room() == 0) {
                targetRoomType = ROOM_TYPE.room3; // Only 3-room units available
            } else {
                // Both room types are available, so prompt the user to choose
                String userInput;
                do {
                    System.out.println("Please select room type (Enter 1 or 2):");
                    System.out.println("1. 2-Room");
                    System.out.println("2. 3-Room");
                    userInput = sc.nextLine();
                    // Keep prompting until the user enters a valid option
                } while (!"1".equals(userInput) && !"2".equals(userInput));

                // Set the target room type based on user input
                if ("1".equals(userInput)) {
                    targetRoomType = ROOM_TYPE.room2;
                } else if ("2".equals(userInput)) {
                    targetRoomType = ROOM_TYPE.room3;
                }
            }
        }
        // If the user is not eligible for 3-room units, return the default (2-room)
        return targetRoomType;
    }
}
