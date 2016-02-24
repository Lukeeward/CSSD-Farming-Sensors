package CSSDFarm;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Server {
    private Map<String, HistoricalData> data;
    private UserAccount currentUser;
    private Interface currentClient;
    private Vector<UserAccount> users;
    private Vector<FieldStation> stations;
    
    private static Server server = new Server();
    
    private Server(){
        this.users = new Vector<UserAccount>();
        this.users.add(new UserAccount("John","Password"));
        this.stations = new Vector<FieldStation>();
        this.data = new HashMap<>();
    }
    
    public static Server getInstance(){
        return server;
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
    
    public Vector<FieldStation> getUserFieldStation(){
        return currentUser.getFieldStations();
    }
    //String1 = FieldStationId??
    public Report compileReport(String fieldStationId){
        HistoricalData stationHistoricalData = data.get(fieldStationId);
        return new Report(fieldStationId, stationHistoricalData);
    }
    
    public void createUserAccount(String username, String password){
        users.add(new UserAccount(username, password));
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
        HistoricalData historicaldata = data.get(fieldStationId);
        for(SensorData data : sensorData){
            historicaldata.addData(data);
        }
        return true;
    }
    
    public Vector<FieldStation> loadData(){
        return currentUser.getFieldStations();
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
    
    public void addSensor(String fieldStationId, String sensorId, String sensorType, String sensorUnits, int interval){
        FieldStation aFieldStation = getFieldStation(fieldStationId);
        aFieldStation.addSensor(sensorId, sensorType, sensorUnits, interval);
        data.put(aFieldStation.getId(), new HistoricalData(sensorType, aFieldStation.getId(), sensorId));
    }
}
