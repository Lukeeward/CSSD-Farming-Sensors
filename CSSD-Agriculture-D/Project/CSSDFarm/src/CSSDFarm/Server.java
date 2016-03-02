package CSSDFarm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Server implements Serializable {
    private Map<String, Map<String, HistoricalData>> data;
    private UserAccount currentUser;
    private Interface currentClient;
    private Vector<UserAccount> users;
    private Vector<FieldStation> stations;
    private Boolean turnedOn;
    
    private static Server server;
    
    private Server(){
        this.users = new Vector<UserAccount>();
        this.users.add(new UserAccount("John","Password",0));
        this.users.add(new UserAccount("Luke","Beffer", 1));
        this.stations = new Vector<FieldStation>();
        this.data = new HashMap<>();
        this.turnedOn = true;
    }
    
    public static Server getInstance(){
        if(server == null)
            return getInstance(null);
        else 
            return server;
    }
    
    public static Server getInstance(Server loadedServer){
        if(loadedServer != null)
            server = loadedServer;
        else
            server = new Server();
        
        return server;
        //return (loadedServer != null) ? (server = loadedServer) : (server = new Server());
    }
    
    //why does authenticate return that
    public boolean authenticateUser(String username, String password){
        for(int i = 0; i < users.size(); i++){
            if(users.elementAt(i).checkCredentials(username, password))
            {
                currentUser = users.elementAt(i);
                return true;
            }
        }
        return false;
    }
    
    public Boolean getTurnedOn(){
        return turnedOn;
    }
    
    public void togglePower(int val){
        if (val == 0)
            this.turnedOn = false;
        else
            this.turnedOn = true;
        //this.turnedOn = !turnedOn;
    }
    
    public Vector<FieldStation> getUserFieldStation(){
        return currentUser.getFieldStations();
    }
    //String1 = FieldStationId??
    public Report compileReport(String fieldStationId){
        Map<String, HistoricalData> sensorHashMap = data.get(fieldStationId);
        
        //HashMap probably null due to the selected fieldstation having no sensors
        //making it empty prevents an error later on
        if(sensorHashMap == null)
            sensorHashMap = new HashMap<>();
        HistoricalData stationHistoricalData = sensorHashMap.get(fieldStationId);
        return new Report(fieldStationId, sensorHashMap);
    }
    
    public void createUserAccount(String username, String password, int userRole){
        users.add(new UserAccount(username, password, userRole));
    }
    
    public void createFieldStation(String id, String name){
       stations.add(new FieldStation(id, name));
    }
    
    public void addFieldStation(String id, String name){
        createFieldStation(id, name);
        FieldStation station = getFieldStation(id);
        currentUser.addStation(station);
    }
    
    public void removeFieldStation(String id){
        FieldStation toDelete = currentUser.getStation(id);
        //TODO: If Farmer then remove from server
        stations.remove(toDelete);
        currentUser.removeStation(toDelete);
        data.remove(id);
    }
    
    public void removeSensor(String fieldStationId, String sensorId){
        data.get(fieldStationId).remove(sensorId);
    }
    //String1 = stationid??
    //String2 = sensorID???
    public SensorData getMostRecentData(String string1, String string2){
        if(currentUser.canAccess(string1))
        {
            for(FieldStation aFieldStation : stations)
            {
                if(aFieldStation.getId() == string1)
                {
                    return aFieldStation.getData(string2);
                }
            }
        }
        return null;
    }
    
    //Returns true as they are expecting connection issues with an actual 'server'
    //The return true would be the response from the server so they know to clear buffer
    public boolean addData(Vector<SensorData> sensorData, String fieldStationId){
        Map<String, HistoricalData> sensorHashMap = data.get(fieldStationId);
        for(SensorData data : sensorData){
            if (data.getId() != null){
                HistoricalData historicaldata = sensorHashMap.get(data.getId());                
                if (historicaldata != null)
                    historicaldata.addData(data);
            }
        }
        return true;
    }
    
    public Vector<FieldStation> loadData(){
        if(currentUser == null) {
            return null;
        } else {
            return currentUser.getFieldStations();
        }
    }
    
    public boolean verifyFieldStation(String id){
        for(FieldStation fieldStation: stations){
            if(fieldStation.getId().toUpperCase().equals(id.toUpperCase()))
                return false;
        }
        return true;
    }
    
    //repeated call "addFieldStation"
//    public void addFieldStation(String, String){
//        return null;
//    }
    
    public FieldStation getFieldStation(String id){
        for(FieldStation aStation : stations)
        {
            if(aStation.getId().equals(id)){
                return aStation;
            }
        }
        return null;
    }
    
    public int getUsersRole(){
        return currentUser.getUserRole();
    }
    
    public void addSensor(String fieldStationId, String sensorId, String sensorType, String sensorUnits, int interval, int threshold, boolean upperlimit){
        FieldStation aFieldStation = getFieldStation(fieldStationId);
        aFieldStation.addSensor(sensorId, sensorType, sensorUnits, interval, threshold, upperlimit);
        Map<String, HistoricalData> sensorHashMap = data.get(aFieldStation.getId());
        if(sensorHashMap == null) {
            data.put(aFieldStation.getId(), new HashMap<>());
        }
        sensorHashMap = data.get(aFieldStation.getId());
        sensorHashMap.put(sensorId, new HistoricalData(sensorType, aFieldStation.getId(), sensorId));
        //data.put(aFieldStation.getId(), new HistoricalData(sensorType, aFieldStation.getId(), sensorId));
    }
}
