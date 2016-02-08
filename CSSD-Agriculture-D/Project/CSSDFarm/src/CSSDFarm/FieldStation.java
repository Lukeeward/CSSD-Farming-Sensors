package CSSDFarm;
import java.util.*;

public class FieldStation {
    private String name;
    private String id;
    private SetOfSensors sensors;
    private Vector<SensorData> buffer;
    
    public FieldStation(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    public Report compileReport(){
        return null;
    }
    
    public String getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public SetOfSensors getSetOfSensors(){
        return sensors;
    }
    
    public void addSensor(Sensor sensor){
        sensors.addSensor(sensor);
    }
    
    public void removeSensor(String id){
        sensors.removeSensor(sensors.getSensor(id));
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
