package program.boundary.housingApplyIO;

import java.util.Scanner;
import program.boundary.console.AppScanner;
import program.control.housingApply.HousingReq;
import program.control.housingApply.HousingReqList;
import program.entity.users.Manager;
import program.entity.users.User;

public class HousingReqSelector {
    private static final Scanner sc = AppScanner.getInstance();

    public static HousingReq selectActiveRequest(User client) {
        HousingReq activeReq = HousingReqList.activeReq(client);
        if (activeReq == null) {
            System.out.println("No active applications found.");
        }
        return activeReq;
    }

    public static HousingReq selectWithdrawalRequest(Manager manager, HousingReqList withdrawalList) {
        if (withdrawalList.isEmpty()) {
            System.out.println("No withdrawal requests available.");
            return null;
        }

        HousingReqPrinter.printWithdrawalRequests(withdrawalList);

        System.out.println("\nPlease choose a withdrawal request to process:");
        int choice = -1;
        do {
            try {
                choice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (choice < 0 || choice >= withdrawalList.size());

        return withdrawalList.get(choice);
    }
}