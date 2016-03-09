package CSSDFarm;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The sensor data contains the data relating to a specific sensor
 */
public class SensorData implements Serializable {
    private String sensorID;
    private Date date;
    private String unit;
    private float value;
    private GPSData location;
    private float power;
    
    /**
     * Sensor Data Constructor
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
     * @return returns the sensor id
     */
    public String getId()
    {
        return sensorID;
    }
    
    /**
     * Return value of sensor data
     * @return returns the sensor value
     */
    public float getValue()
    {
        return value;
    }
    
    /**
     * Set value of sensor data
     * @param value Sets the sensor value
     */
    public void setValue(float value){
        this.value = value;
    }
    
    /**
     * Get unit of sensor data e.g lux
     * @return returns the sensor units
     */
    public String getUnit()
    {
        return unit;//mm - inches
    }
    
    /**
     * Get date of sensor data
     * @return Returns the SensorData creation date
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
     * @return returns the SensorData date
     */
    public Date getFullDate(){
        return date;
    }
    
    /**
     * get GPSdata location
     * @return Returns the location
     */
    public GPSData getLocation()
    {
        return location;
    }
    
    /**
     * Get power 
     * @return returns the power
     */
    public float getPower()
    {
        return power;
    }
    
    /**
     * Converts Celsius units to Fahrenheit
     */
    public void convertUnit()
    {
        //If Unit is celcuis
        //Convert the value to F
        if(unit.equals("°C"))
        {
            value = (((value)-32)*5)/9;
            unit = "°F";
        } else {
            if (unit.equals("°F")) {
                value = (((value)*9)/5)+32;
                unit = "°C";
            }
        }
    }
    
    
}
