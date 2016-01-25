package CSSDFarm;
import java.util.Map;


public class Server {
    private Map<String, HistoricalData> data;
    private UserAccount currentUser;
    private Interface currentClient;
    private Vector<UserAccount> users;
    private Vector<FieldStation> stations;
    
    public Vector<FieldStation> authenticateUser(String, String){
        return null;
    }
    
    public Report compileReport(String){
        return null;
    }
    
    public void createUserAccount(String, String){
        return null;
    }
    
    public void createFieldStation(String){
        return null;
    }
    
    public void addFieldStation(String, String){
        return null;
    }
    
    public SensorData getMostRecentData(String, String){
        return null;
    }
    
    public boolean addData(Vector<SensorData){
        return null;
    }
    
    public Vector<FieldStation> loadData(){
        return null;
    }
    
    public boolean verifyFieldStation(String){
        return null;
    }
    
    public void addFieldStation(String, String){
        return null;
    }
    
    public FieldStation getFieldStation(String){
        return null;
    }
    
    public void addSensor(String, String, String, String){
        return null;
    }
}
