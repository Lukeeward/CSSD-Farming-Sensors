package CSSDFarm;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tom
 */
public class SensorData implements Serializable {
    private String sensorID;
    private Date date;
    private String unit;
    private float value;
    private GPSData location;
    private float power;
    
    /**
     *
     * @param sensorID, ID of the sensor
     * @param time, time
     * @param unit, unit of measurement e.g mm
     * @param value, value of the sensor
     * @param location, GPSData with longitude and latitude
     * @param power, power of sensor 
     */
    public SensorData(String sensorID, Date time, String unit, float value, GPSData location, float power){
        this.sensorID = sensorID;
        this.date = time;
        this.unit = unit;
        this.value = value;
        this.location = location;
        this.power = power;
    }
    
    /**
     * Return ID of sensor data
     * @return
     */
    public String getId()
    {
        return sensorID;
    }
    
    /**
     * Return value of sensor data
     * @return
     */
    public float getValue()
    {
        return value;
    }
    
    /**
     * Set value of sensor data
     * @param value
     */
    public void setValue(float value){
        this.value = value;
    }
    
    /**
     * Get unit of sensor data e.g lux
     * @return
     */
    public String getUnit()
    {
        return unit;//mm - inches
    }
    
    /**
     * Get date of sensor data
     * @return
     */
    public Date getDate()
    {
        DateFormat inputformatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = inputformatter.format(date);
        Date date = new Date();
                
        try {
            date = inputformatter.parse(newDate);
        } catch(ParseException ex) {
            
        }
        return date;
    }
    
    /**
     * Get full date 
     * @return
     */
    public Date getFullDate(){
        return date;
    }
    
    /**
     * get GPSdata location
     * @return
     */
    public GPSData getLocation()
    {
        return location;
    }
    
    /**
     * Get power 
     * @return
     */
    public float getPower()
    {
        return power;
    }
    
    /**
     * 
     */
    public void convertUnit()
    {
        //if unit == mm then inches for example
        //or if celcius convert to farenheit
    }
    
    
}
