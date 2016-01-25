package CSSDFarm;
import java.util.Date;

public class SensorData {
    
    private String sensorID;
    private Date time;
    private String unit;
    private float value;
    private GPSData location;
    
    public float getValue()
    {
        return 0;
    }
    
    public String getUnit()
    {
        return null;
    }
    
    public void convertUnit()
    {
    }
    
    public Date getTime()
    {
        return null;
    }
}
