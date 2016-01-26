package CSSDFarm;
import java.util.Map;
import java.util.Vector;

public class Server {
    private Map<String, HistoricalData> data;
    private UserAccount currentUser;
    private Interface currentClient;
    private Vector<UserAccount> users;
    private Vector<FieldStation> stations;
    
    //why does authenticate return that
    public Vector<FieldStation> authenticateUser(String username, String password){
        return null;
    }
    
    public Report compileReport(String string1){
        return null;
    }
    
    public void createUserAccount(String username, String password){
        
    }
    
    public void createFieldStation(String name){
       
    }
    
    public void addFieldStation(String name, String id){
        
    }
    
    public SensorData getMostRecentData(String string1, String string2){
        return null;
    }
    
    public boolean addData(Vector<SensorData> sensorData){
        return false;
    }
    
    public Vector<FieldStation> loadData(){
        return null;
    }
    
    public boolean verifyFieldStation(String id){
        return false;
    }
    
    //repeated call "addFieldStation"
//    public void addFieldStation(String, String){
//        return null;
//    }
    
    public FieldStation getFieldStation(String id){
        return null;
    }
    
    public void addSensor(String string1, String string2, String string3, String string4){
        
    }
}
