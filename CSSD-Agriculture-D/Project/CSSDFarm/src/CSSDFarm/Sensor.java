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
    private Actuator actuator;
    
    public Sensor(){
    
    }
    
    public SensorData getData()
    {   
        return data;
    }
    
    public void toggleModule()
    {
        if(actuator.isActive())
        {
            actuator.deactivate();
        }
        else
        {
            actuator.activate();
        }
    }
    
    public void collectData()
    {
        //Replace 'mm' with 'unit'
        //Replace 12.0f with 'value'
        data = new SensorData(id, new Date(),"mm",12.0f, location);
    }
    
    public void onInterval()
    {
        //If lastReadingTime == lastReadingTime + intervalSeconds
        /*if(new Date(lastReadingTime.getSeconds() + intervalSeconds) == new Date())
        {
            collectData();
        }*/
    }
    
    public void setFieldStation(FieldStation fieldStation)
    {
        station = fieldStation;
    }
    
    public void activate()
    {
    }
    
    public void calculateLocation()     
    {
        //change to realistic gps data
        location = new GPSData(12345.54321f, 6789.9876f, 123.4f);
    }
    
    public void update(FieldStation fieldStation, Sensor sensor)
    {
    }
    
    public boolean withinThreshold()
    {
        if(thresholdIsUpperLimit)
        {
            if(data.getValue() <= threshold)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(data.getValue() >= threshold)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
