package Program;

import java.util.ArrayList;

public class UserList extends ArrayList<User> {
    public User get(String UserId){
        for (User user : this){
            if (user.getUserId().equals(UserId)){
                return user;
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
