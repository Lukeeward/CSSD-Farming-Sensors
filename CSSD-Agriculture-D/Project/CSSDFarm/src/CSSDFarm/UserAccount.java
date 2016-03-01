package CSSDFarm;
import java.io.Serializable;
import java.util.*;

public class UserAccount implements Serializable{
    private Vector<FieldStation> stations;
    private String username;
    private String password;
    private int userRole; 
    
    public UserAccount(String username, String password, int userRole){
        this.stations = new Vector<FieldStation>();
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }
    
    public boolean checkCredentials(String username, String password){
        return (this.username.equals(username)) && (this.password.equals(password));
    }
    
    public void addStation(FieldStation fieldStation){
        this.stations.add(fieldStation);
    }
    
    public void removeStation(FieldStation fieldStation){
        stations.remove(fieldStation);
    }
    
    public FieldStation getStation(String id){
        for(FieldStation aStation : stations){
            if(aStation.getId().equals(id)){
                return aStation;
            }
        }
        return null;
    }
    
    public String getUserRolesString(int userRole){
        switch(userRole){
            case 0: return "Farmer";
            case 1: return "Food Processing Manager";
        }
        return null;
    }
    
    public int getUserRole(){
        return userRole;
    }
    
    public Vector<FieldStation> getFieldStations(){
        return stations;
    }
    
    public boolean canAccess(String string1){
        return false;
    }
    
}
