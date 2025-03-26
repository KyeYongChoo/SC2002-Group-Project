package program;

public class UserAgeException extends Exception{

    public UserAgeException(String message) {
        super(message);
    }
    public UserAgeException(){
        super("No eligible HDB types for user");
    }
    
    
}
