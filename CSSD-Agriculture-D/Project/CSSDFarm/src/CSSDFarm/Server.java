package CSSDFarm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * The server stores and processes data relating to the whole system.
 * @author lnseg
 */
public class Server implements Serializable {
    private Map<String, Map<String, HistoricalData>> data;
    private UserAccount currentUser;
    private Vector<UserAccount> users;
    private Vector<FieldStation> stations;
    private Boolean serverIsOn;
    private static Server server;
    
    
    /**
     * Default Constructor for the Server.
     */
    private Server(){
        //Initialises the Vector<UserAccount> users
        this.users = new Vector<UserAccount>(); 
        
        //Used for debugging.
        this.users.add(new UserAccount("John","Password",0));
        //this.users.add(new UserAccount("Luke","Beffer", 1));
        
        //Initialises the Vector<FieldStation> stations
        this.stations = new Vector<FieldStation>(); 
        
        //Initialises the HashMap<> data
        this.data = new HashMap<>(); 
        this.serverIsOn = true; 
    }
    
    /**
     * Gets the instance of the Server. 
     * @return Server, the instance of the Server. 
     */
    public static Server getInstance(){
        //If the server is null, create a new instance otherwise return the Server. 
        if(server == null)
            return getInstance(null);
        else 
            return server;
    }
    
    /**
     * Get the instance of the server with a pre loaded server. 
     * @param loadedServer The server from the server.ser serialized file
     * @return Server, the instance of the server. 
     */
    public static Server getInstance(Server loadedServer){
        //If there's a server to be loaded, otherwise create a new one. 
        if(loadedServer != null)
            server = loadedServer;
        else
            server = new Server();
        
        return server;
    }

    /**
     * Authenticates the user
     * 
     * @param username, the users username
     * @param password, the users password
     * @return boolean, the success of the users authentication. 
     */
    public boolean authenticateUser(String username, String password){
        //for each user, check the credentials and set the current user
        for(int i = 0; i < users.size(); i++){
            if(users.elementAt(i).checkCredentials(username, password))
            {
                currentUser = users.elementAt(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the state of the Server. 
     * @return boolean, the state of the server.
     */
    public Boolean getServerIsOn(){
        return serverIsOn;
    }
    
    /**
     * Toggles the Server on and off. 
     * @param val int, whether to turn the server on or off.
     */
    public void togglePower(int val){
        if (val == 0)
            this.serverIsOn = false;
        else
            this.serverIsOn = true;
        //this.serverIsOn = !serverIsOn;
    }
    
    /**
     * Gets the field stations
     * @return VectorFieldStations, A vector of field stations.
     */
    public Vector<FieldStation> getUserFieldStation(){
        //if the user role is 1 return all the field stations.
        //Otherwise just return the ones for that user.
        if(currentUser.getUserRole() == 1)
            return stations;
        else
            return currentUser.getFieldStations();
    }

    /**
     * Compiles the report for the Field Station.
     * 
     * @param fieldStationId, the id of the field station for the report to be created. 
     * @return Report, the report for the field station.
     */
    public Report compileReport(String fieldStationId){
        Map<String, HistoricalData> sensorHashMap = data.get(fieldStationId);
        
        //HashMap probably null due to the selected fieldstation having no sensors
        //making it empty prevents an error later on
        if(sensorHashMap == null)
            sensorHashMap = new HashMap<>();
        
        HistoricalData stationHistoricalData = sensorHashMap.get(fieldStationId);
        return new Report(fieldStationId, sensorHashMap);
    }
    
    /**
     * Create a user account for registering users 
     * @param username String, the username of the user. 
     * @param password String, the password of the user. 
     * @param userRole int, the userRole of the user. 
     */
    public void createUserAccount(String username, String password, int userRole){
        users.add(new UserAccount(username, password, userRole));
    }
    
    /**
     * Creates a new Field station. 
     * 
     * @param id String, the id of the field station.
     * @param name String, the name of the field station.
     */
    public void createFieldStation(String id, String name){
       stations.add(new FieldStation(id, name));
    }
    
    /**
     * Adds a new Field station. 
     * 
     * @param id String, the id of the field station.
     * @param name String, the name of the field station.
     */
    public void addFieldStation(String id, String name){
        createFieldStation(id, name);
        FieldStation station = getFieldStation(id);
        currentUser.addStation(station);
    }
    
    /**
     * Removes a field station.
     * 
     * @param id, the id of the station to remove. 
     */
    public void removeFieldStation(String id){
        FieldStation toDelete = currentUser.getStation(id);
        stations.remove(toDelete);
        currentUser.removeStation(toDelete);
        data.remove(id);
    }
    
        /**
     * Adds a new sensor.
     * @param fieldStationId String, the id of the FieldStation the sensor will be attached to. 
     * @param sensorId String, the id of the Sensor.
     * @param sensorType String, the type of the sensor. 
     * @param sensorUnits String, the units of the sensor. 
     * @param interval int, the number of seconds between each sensor reading. 
     * @param threshold int, the threshold of the Sensor reading to know when to activate the Actuator. 
     * @param upperlimit boolean, whether the threshold is the upper or lower limit. 
     */
    public void addSensor(String fieldStationId, String sensorId, String sensorType, String sensorUnits, int interval, int threshold, boolean upperlimit){
        //Gets the field station
        FieldStation aFieldStation = getFieldStation(fieldStationId);
        
        //Adds the sensor to the field station.
        aFieldStation.addSensor(sensorId, sensorType, sensorUnits, interval, threshold, upperlimit);
        
        //Gets the hashmap for the field station. Otherwise initialise it. 
        Map<String, HistoricalData> sensorHashMap = data.get(aFieldStation.getId());
        if(sensorHashMap == null) {
            data.put(aFieldStation.getId(), new HashMap<>());
        }
        sensorHashMap = data.get(aFieldStation.getId());
        sensorHashMap.put(sensorId, new HistoricalData(sensorType, aFieldStation.getId(), sensorId));
    }
    
    /**
     * Removes a sensor from the field station.
     * 
     * @param fieldStationId, the id of the field station with the sensor attached. 
     * @param sensorId, the id of the sensor to remove. 
     */
    public void removeSensor(String fieldStationId, String sensorId){
        data.get(fieldStationId).remove(sensorId);
    }

    /**
     * Gets the most recent data 
     * 
     * @param fieldStationId, the if of the field station to get data from. 
     * @param sensorId, the id of the sensor to get data from.
     * @return SensorData, the data from the sensor.
     */
    public SensorData getMostRecentData(String fieldStationId, String sensorId){
        //Check to see if the user has access to the field station
        if(currentUser.canAccess(fieldStationId))
        {
            //for each field station
            for(FieldStation aFieldStation : stations)
            {
                //if the field station id matched the param fieldStationId
                if(aFieldStation.getId() == fieldStationId)
                {
                    //Get the data for the sensor on that field station.
                    return aFieldStation.getData(sensorId);
                }
            }
        }
        return null;
    }
    
    /**
     * Adds SensorData to the HistoricalData. 
     * 
     * @param sensorData, the SensorData to add to the HistoricalData
     * @param fieldStationId, the id of the field station the sensor belongs to. 
     * 
     * @return boolean
     */
    public boolean addData(Vector<SensorData> sensorData, String fieldStationId){
        //Gets the hashmap for the FieldStation.
        Map<String, HistoricalData> sensorHashMap = data.get(fieldStationId);
        
        //For each SensorData in Vector<SensorData> Get the current historical data for that Sensor
        //Then add the new data to that. 
        for(SensorData data : sensorData){
            if (data.getId() != null){
                HistoricalData historicaldata = sensorHashMap.get(data.getId());                
                if (historicaldata != null)
                    historicaldata.addData(data);
            }
        }
        
        //Returns true as they are expecting connection issues with an actual 'server'
        //The return true would be the response from the server so they know to clear buffer

        return true;
    }
    
    /**
     * Gets the FieldStation for the logged in user.
     * 
     * @return Vector FieldStation, the fieldStations
     */
    public Vector<FieldStation> loadData(){
        //If the user is a farm station return all the FieldStations
        //Otherwise just the ones assignes to a user.
        if(currentUser == null) {
            return null;
        } else {
            if(currentUser.getUserRole() == 1)
                return stations;
            else
                return currentUser.getFieldStations();
        }
    }
    
    /**
     * Checks that the FieldStation Id is unique.
     * 
     * @param id, the FieldStation Id to check.
     * 
     * @return boolean, whether the Id is unique or not. 
     */
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
    
    /**
     * Gets a FieldStation.
     * @param id, the id of the FieldStation to get. 
     * 
     * @return FieldStation, the field station with the param id.
     */
    public FieldStation getFieldStation(String id){
        for(FieldStation aStation : stations)
        {
            if(aStation.getId().equals(id)){
                return aStation;
            }
        }
        return null;
    }
    
    /**
     * Gets the user role of the user. 
     * 
     * @return int, the user role of the user.
     */
    public int getUsersRole(){
        return currentUser.getUserRole();
    }
    

}
