package CSSDFarm;
import java.util.Date;

public class SensorData {
    private String sensorID;
    private Date time;
    private String unit;
    private float value;
    private GPSData location;
    private float power;
    
    public SensorData(String sensorID, Date time, String unit, float value, GPSData location, float power){
        this.sensorID = sensorID;
        this.time = time;
        this.unit = unit;
        this.value = value;
        this.location = location;
        this.power = power;
    }
    
    public String getID()
    {
        return sensorID;
    }
    
    public float getValue()
    {
        return value;
    }
    
    public String getUnit()
    {
        return unit;//mm - inches
    }
    
    public Date getTime()
    {
        return time;
    }
    
    public GPSData getLocation()
    {
        return location;
    }
    
    public float getPower()
    {
        return power;
    }
    
    public void convertUnit()
    {
        //if unit == mm then inches for example
        //or if celcius convert to farenheit
    }
    
    
}
