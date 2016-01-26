package CSSDFarm;
import java.util.*;

public class UserAccount {
    private Vector<FieldStation> stations;
    private String username;
    private String password;
    
    public UserAccount(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public boolean checkCredentials(String username, String password){
        return (this.username.equals(username)) && (this.password.equals(password));
    }
    
    public void addStation(FieldStation fieldStation){
        
    }
    
    public void removeStation(FieldStation fieldStation){
        
    }
    
    public FieldStation getStation(String id){
        return null;
    }
    
    public Vector<FieldStation> getFieldStations(){
        return null;
    }
    
    public boolean canAccess(String string1){
        return false;
    }
    
}
