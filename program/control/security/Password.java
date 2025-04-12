package program.control.security;

public class Password {
    private String hash; 
    private String salt;

    public Password(){
        salt = HashingUtils.genSalt();
        hash = HashingUtils.hash("password" + salt + Pepper.getPepper());
    }

    public Password(String defaultPassword){
        salt = HashingUtils.genSalt();
        hash = HashingUtils.hash(defaultPassword + salt + Pepper.getPepper());
    }

    public Password(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt(){
        return salt;
    }
}
