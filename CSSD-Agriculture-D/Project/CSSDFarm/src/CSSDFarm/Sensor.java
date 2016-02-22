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
    private String units;
    
    public Sensor(String id, FieldStation station, int intervalSeconds, String sensorType, 
            float power, float threshold, boolean thresholdIsUpperLimit, Actuator actuator){
        this.id = id;
        this.station =  station;
        this.intervalSeconds = intervalSeconds;
        this.sensorType = sensorType;
        this.power = power;
        this.threshold = threshold;
        this.thresholdIsUpperLimit = thresholdIsUpperLimit;
        this.actuator = actuator;
        //Set GPSData
        calculateLocation();
        //Set SensorData
        collectData();
    }
    
    public Sensor(String id, String sensorType, String units, int interval)
    {
        this.id = id;
        this.sensorType = sensorType;
        this.units = units;
        this.intervalSeconds = interval;
        calculateLocation();
    }
    

    public SensorData getData()
    {   
        return data;
    }
    
    public String getId(){
        return this.id;
    }
    
    public String getType() {
        return this.sensorType;
    }
    
    public String getUnits(){
        return this.units;
    }
    
    public GPSData getGps(){
        return location;
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
        data = new SensorData(id, new Date(),"mm",12.0f, location, 120);
    }
    
    public void onInterval()
    {
        //If lastReadingTime == lastReadingTime + intervalSeconds
        /*if(new Date(lastReadingTime.getSeconds() + intervalSeconds) == new Date())
        {*/
            collectData();
            //The Sequence Diagram says it should call the fieldstation but doesnt say what the string is
            //"data.txt"??
            station.update("data", data);
        //}
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
