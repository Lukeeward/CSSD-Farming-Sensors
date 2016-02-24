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
        sensors = new SetOfSensors();
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
    
    //string1 could be the filename of where the data is stored?
    public void update(String string1, SensorData sensorData){
        addToBuffer(string1, sensorData);
        if(uploadData())
        {
            clearBuffer();
        }
    }
    
    public SensorData getData(String id){
        return null;
    }
    
    public boolean uploadData(){
        //Possibly upload multiple sensorDatas from the buffer files
        Random rn = new Random();
        int reading = rn.nextInt(10) + 1;
    
        Vector<SensorData> dummyData = new Vector<SensorData>();
        dummyData.add(new SensorData("test", new Date(),"mm",reading, new GPSData(53.367785f, -1.507226f, 0.5f), 120));
        Server.getInstance().addData(dummyData, id);
        return true;
    }
    
    public void addToBuffer(String string1, SensorData sensorData){
        //This could be where the data is serialised before being sent to the server
    }
    
    public void clearBuffer(){
        System.out.print("Clearning buffer");
    }
    
    public void addSensor(String sensorId, String sensorType, String sensorUnits, int interval){
        Sensor theSensor = new Sensor(sensorId, sensorType, sensorUnits, interval);
        sensors.addSensor(theSensor);
    }
}
