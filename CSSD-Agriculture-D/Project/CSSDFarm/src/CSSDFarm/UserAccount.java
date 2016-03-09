package CSSDFarm;
import java.io.Serializable;
import java.util.*;

/**
 * UserAccount class holds user account information and field stations linked to their account
 */

public class UserAccount implements Serializable{
    private Vector<FieldStation> stations;
    private String username;
    private String password;
    private int userRole; 
    
    /**
     * Constructor that takes username, password and user role.
     * @param username The users username
     * @param password The users password
     * @param userRole The users User role
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
     * @param username The supplied username
     * @param password The supplied password
     * @return A boolean as to whether the credentials were correct
     */
    public boolean checkCredentials(String username, String password){
        return (this.username.equals(username)) && (this.password.equals(password));
    }
    
    /**
     * Add a field station to user account
     * @param fieldStation The FieldStations to add
     */
    public void addStation(FieldStation fieldStation){
        this.stations.add(fieldStation);
    }
    
    /**
     * Remove field station from user account
     * @param fieldStation the FieldStation to remove.
     */
    public void removeStation(FieldStation fieldStation){
        stations.remove(fieldStation);
    }
    
    /**
     * Get field station with ID from user account
     * @param id The Id of the station to find
     * @return The found station
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
     * @param userRole The userrole int
     * @return The found userrole
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
     * @return The users user role
     */
    public int getUserRole(){
        return userRole;
    }
    
    /**
     * Get all field stations
     * @return The users fieldstations
     */
    public Vector<FieldStation> getFieldStations(){
        return stations;
    }
    
    /**
     * Checks a supplied FieldStation id against the users station list
     * 
     * @param stationId The FieldStation Id to check against user stations
     * @return Whether the station was found
     */
    public boolean canAccess(String stationId){
        for(FieldStation field : stations){
            if(field.getId().equals(stationId)){
                return true;
            }
        } 
        return false;
    }
    
}
