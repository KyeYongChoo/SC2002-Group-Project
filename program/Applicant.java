package program;

// My friend chatGPTed it and said it really should have an applicant class so here's a kinda empty class; all the functionality is already in
public class Applicant extends User{
    public Applicant(String NRIC, String name, int age, String marital_status) throws Exception{
        super(NRIC, name, age, marital_status);
    }
    public Applicant(String NRIC, String name, int age, String marital_status, String password) throws Exception{
        super(NRIC, name, age, marital_status, password);
    }
}
