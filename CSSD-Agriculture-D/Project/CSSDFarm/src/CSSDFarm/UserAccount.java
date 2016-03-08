package CSSDFarm;
import java.io.Serializable;
import java.util.*;

/**
 * UserAccount class holds user account information and field stations linked to their account
 * @author Tom
 */
public class UserAccount implements Serializable{
    private Vector<FieldStation> stations;
    private String username;
    private String password;
    private int userRole; 
    
    /**
     * Constructor that takes username, password and user role.
     * @param username
     * @param password
     * @param userRole
     */
    public UserAccount(String username, String password, int userRole){
        this.stations = new Vector<FieldStation>();
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }
    
    /**
     * Checks if username and password that is inputted matches the user account
     * used for logging in.
     * @param username
     * @param password
     * @return
     */
    public boolean checkCredentials(String username, String password){
        return (this.username.equals(username)) && (this.password.equals(password));
    }
    
    /**
     * Add a field station to user account
     * @param fieldStation
     */
    public void addStation(FieldStation fieldStation){
        this.stations.add(fieldStation);
    }
    
    /**
     * Remove field station from user account
     * @param fieldStation
     */
    public void removeStation(FieldStation fieldStation){
        stations.remove(fieldStation);
    }
    
    /**
     * Get field station with ID from user account
     * @param id
     * @return
     */
    public FieldStation getStation(String id){
        for(FieldStation aStation : stations){
            if(aStation.getId().equals(id)){
                return aStation;
            }
        }
        return null;
    }
    
    /**
     * Get user role string 
     * @param userRole
     * @return
     */
    public String getUserRolesString(int userRole){
        switch(userRole){
            case 0: return "Farmer";
            case 1: return "Food Processing Manager";
        }
        return null;
    }
    
    /**
     * get user role int
     * @return
     */
    public int getUserRole(){
        return userRole;
    }
    
    /**
     * Get all field stations
     * @return
     */
    public Vector<FieldStation> getFieldStations(){
        return stations;
    }
    
    /**
     * 
     * @param string1
     * @return
     */
    public boolean canAccess(String string1){
        return false;
    }
    
}
