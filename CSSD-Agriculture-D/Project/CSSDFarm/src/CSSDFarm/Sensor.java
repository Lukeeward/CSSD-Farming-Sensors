package CSSDFarm;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sensor implements Serializable {
    
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
    
    public Sensor(String id, String sensorType, String units, int interval, int threshold, boolean upperlimit)
    {
        this.id = id;
        this.sensorType = sensorType;
        this.units = units;
        this.intervalSeconds = interval;
        this.actuator = new Actuator();
        this.threshold = threshold;
        this.thresholdIsUpperLimit = upperlimit;
        calculateLocation();
        collectData();
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
        Random rand = new Random();
        float randomNumber = (float)(0 + rand.nextInt((100 - 0) + 1));
        data = new SensorData(id, new Date(),"mm",randomNumber, location, 120);
        lastReadingTime = new Date();
    }
    
    public String getNextIntervalTime(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastReadingTime);
        cal.add(Calendar.SECOND, intervalSeconds);
        String intDate = df.format(cal.getTime());
        return intDate;
    }
    
    public boolean onInterval()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String intDate = getNextIntervalTime();
        
        System.out.println(intDate);
        System.out.println(df.format(new Date()));
        
        Date intervalDate = new Date();
                
        try {
            intervalDate = df.parse(intDate);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
                
        if(intDate.equals(df.format(new Date())) || intervalDate.before(new Date())){
            System.out.println(lastReadingTime.getTime());
            collectData();
            //The Sequence Diagram says it should call the fieldstation but doesnt say what the string is
            //"data.txt"??
            station.update("data/buffer.ser", data);
            
            if(withinThreshold())
            {
                this.actuator.deactivate();
            } else {
                this.actuator.activate();
            }
            
            return true;
        }
        return false;
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
        location = new GPSData(53.367785f, -1.507226f, 0.5f);
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
