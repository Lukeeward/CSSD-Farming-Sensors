package CSSDFarm;
import java.util.Date;

public class Sensor {
    
    private String id;
    private FieldStation station;
    private int intervalSeconds;
    private String sensorType;
    private GPSData location;
    private float power;
    private SensorData data;
    private float threshold;
    private boolean thresholdIsUpperLimit;
    private Date lastReadingTime;
    private SensorData sensorData;
    
    public SensorData getData()
    {   
        return null;
    }
    
    public void toggleModule()
    {
    }
    
    public void collectData()
    {
    }
    
    public void onInterval()
    {
    }
    
    public void setFieldStation(FieldStation fieldStation)
    {
    }
    
    public void activate()
    {
    }
    
    public void calculateLocation()     
    {
    }
    
    public void update(FieldStation fieldStation, Sensor sensor)
    {
    }
    
    public boolean withinThreshold()
    {
        return false;
    }
}
