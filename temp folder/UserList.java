package program;

import java.util.ArrayList;

public class UserList extends ArrayList<User> {
    /*
     * Returns first instance of the user in given List (or all Lists, if searchAllLists is true)
     * @param searchAllLists Default: true When true, searches through Manager List, then Officer List, then User List. 
     */
    public User get(String UserId){
        return get(UserId,true);
    }
    public User get(String UserId, boolean searchAllLists){
        if (searchAllLists){
            for (User user : MainActivity.managerList){
                if (user.getUserId().equals(UserId)){
                    return user;
                }
            }
            for (User user : MainActivity.officerList){
                if (user.getUserId().equals(UserId)){
                    return user;
                }
            }
            for (User user : MainActivity.applicantList){
                if (user.getUserId().equals(UserId)){
                    return user;
                }
            }
        }
        else{
            for (User user : this){
                if (user.getUserId().equals(UserId)){
                    return user;
                }
            }
        }
        return null;
    }
    public User getByName(String Name){
        for (User user : this){
            if (user.getName().equals(Name)){
                return user;
            }
        }
        return null;
    }
    
}
