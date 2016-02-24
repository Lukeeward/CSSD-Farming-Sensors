package CSSDFarm;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorData {
    private String sensorID;
    private Date date;
    private String unit;
    private float value;
    private GPSData location;
    private float power;
    
    public SensorData(String sensorID, Date time, String unit, float value, GPSData location, float power){
        this.sensorID = sensorID;
        this.date = time;
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
    
    public Date getFullDate(){
        return date;
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
