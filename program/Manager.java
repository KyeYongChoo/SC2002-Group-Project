package program;

public class Manager extends Officer {
    public Manager(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }

    // needs implementation, I put like this for now to stop it from flagging up as an error 
    public Manager promoteOfficer(Officer officer){
        return this;
    }
}
