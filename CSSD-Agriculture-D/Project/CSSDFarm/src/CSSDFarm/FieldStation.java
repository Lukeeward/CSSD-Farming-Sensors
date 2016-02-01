package CSSDFarm;
import java.util.*;

public class FieldStation {
    private String name;
    private String id;
    private SetOfSensors sensors;
    private Vector<SensorData> buffer;
    
    public FieldStation(String name, String id)
    {
        this.name = name;
        this.id = id;
    }
    
    public Report compileReport(){
        return null;
    }
    
    public String getId(){
        return id;
    }
    
    public void addSensor(Sensor sensor){
        
    }
    
    public void removeSensor(String id){
        
    }
    
    public void update(String string1, SensorData sensorData){
        
    }
    
    public SensorData getData(String id){
        return null;
    }
    
    public boolean uploadData(){
        return false;
    }
    
    public void addToBuffer(String string1, SensorData sensorData){
        
    }
    
    public void clearBuffer(){
        
    }
    
    public void addSensor(String sensorId, String sensorType, String sensorUnits, int interval){
        Sensor theSensor = new Sensor(sensorId, sensorType, sensorUnits, interval);
        sensors.addSensor(theSensor);
    }
}
