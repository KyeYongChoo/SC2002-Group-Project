package program.entity.users;

import java.util.ArrayList;

import program.control.Main;

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
            for (User user : Main.managerList){
                if (user.getUserId().equals(UserId)){
                    return user;
                }
            }
            for (User user : Main.officerList){
                if (user.getUserId().equals(UserId)){
                    return user;
                }
            }
            for (User user : Main.applicantList){
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (User user : this) {
            sb.append(user.toString());
            if (! user.equals(this.get(this.size() - 1))) sb.append(",");
        }
        sb.append("\"");
        return sb.toString();
    }
    
}
