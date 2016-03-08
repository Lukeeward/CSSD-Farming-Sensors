package CSSDFarm;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lnseg
 */
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
    
    /**
     * Constructor for the Sensor. 
     * 
     * @param id String, the id of the Sensor.
     * @param station FieldStation, the FieldStation that the sensor is a part of. 
     * @param intervalSeconds int, the number of seconds between each sensor reading. 
     * @param sensorType String, the type of the sensor. 
     * @param power float, the power of the Sensor.int, the threshold of the Sensor reading to know when to activate the Actuator. 
     * @param thresholdIsUpperLimit boolean, whether the threshold is the upper or lower limit. 
     * @param actuator Actuator, the mechanical component relating to the sensor that may be switched on and off for maintenence. 
     */
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
        
        //Set SensorData for unit tests, dont think this is called otherwise..
        data = new SensorData(id, new Date(),"LUX",12, location, 99);
    }
    
    /**
     * Constructor for the Sensor. 
     * 
     * @param id String, the id of the Sensor.
     * @param sensorType String, the type of Sensor e.g. Soil Moisture, Soil Acidity, Light Intensity.
     * @param units String, the unit of measurement relating to the Sensor Type e.g. %, C, F.
     * @param interval int, the number of seconds between each sensor reading. 
     * @param threshold boolean, whether the threshold is the upper or lower limit. 
     * @param upperlimit boolean, whether the threshold is the upper or lower limit. 
     */
    public Sensor(String id, String sensorType, String units, int interval, int threshold, boolean upperlimit)
    {
        this.id = id;
        this.sensorType = sensorType;
        this.units = units;
        this.intervalSeconds = interval;
        this.actuator = new Actuator();
        this.threshold = threshold;
        this.thresholdIsUpperLimit = upperlimit;
        //Set GPSData
        calculateLocation();
        //Collects the first lot of data.
        collectData();
    }
    
    /**
     * Gets the SensorData data. 
     * 
     * @return SensorData.
     */
    public SensorData getData()
    {   
        return data;
    }
    
    /**
     * Gets the id of the sensor. 
     * @return String
     */
    public String getId(){
        return this.id;
    }
    
    /**
     * Gets the type of the Sensor. 
     * @return String.
     */
    public String getType() {
        return this.sensorType;
    }
    
    /**
     * Gets the interval time in seconds for the Sensor. 
     * @return float.
     */
    public float getIntervalSeconds() {
        return this.intervalSeconds;
    }
    
    /**
     * Gets the units of the Sensor.
     * @return String.
     */
    public String getUnits(){
        return this.units;
    }
    
    /**
     * Gets the GPS of the sensor. 
     * @return GPSData.
     */
    public GPSData getGps(){
        return location;
    }
    
    /**
     * Gets the threshold of the Sensor. 
     * @return float.
     */
    public float getThreshold(){
        return threshold;
    }
    
    /**
     * Gets the thresholdIsUpperLimit to see if the threshold is upper of lower limit.
     * @return boolean.
     */
    public boolean getThresholdIsUpperLimit(){
        return thresholdIsUpperLimit;
    }
    
    /**
     * Gets the Actuator
     * @return Actuator.
     */
    public Actuator getActuator(){
        return actuator;
    }
    
    /**
     * Toggles the Actuator one or off depending on the previous state.
     */
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
    
    /**
     * Collects some new data for the Sensor. 
     */
    public void collectData()
    {        
        float newVal = 0;
        //sets the value for the sensorData to increment or decrement
        //once it has reached the threshold it changes its value and slowly moves towards the threshold
        if (data == null){
            newVal = 0;
        }
        else{   
            Random rand = new Random();
            int randLimit;
            //calculate the randomRange based on the total threshold to make it unique for each sensor
            if (threshold > 0){
                randLimit = (int)Math.sqrt(threshold);
                float randomNumber = (float)(0 + rand.nextInt((randLimit - 0) + 1));
                
                if (data.getValue() < threshold){
                    newVal = (data.getValue()+randomNumber);
                }            
                else if (data.getValue() > threshold){
                    newVal = (data.getValue()-randomNumber);
                }
                else{
                    if (thresholdIsUpperLimit)
                        newVal = threshold/2;
                    else
                        newVal = threshold*2;
                }
            }
            else{
                randLimit = (int)Math.sqrt(Math.abs(threshold));
                float randomNumber = (float)(0 + rand.nextInt((randLimit - 0) + 1));
                
                float val = data.getValue();
                if (thresholdIsUpperLimit){
                    if (val > threshold)
                        newVal = threshold*2;
                    else if (val < threshold)
                        newVal = (val+randomNumber);                    
                    else
                        newVal = threshold*2;
                }else {
                    if (val < threshold)
                        newVal = threshold/2;
                    
                    else if (val > threshold)
                        newVal = (val-randomNumber);
                    else
                        newVal = threshold/2;
                    
                }
            }
        }
                    
        data = new SensorData(id, new Date(),units,newVal, location, 99);
        lastReadingTime = new Date();
    }
    
    /**
     * Looks at the previous reading time to calculate when the next reading should be made. 
     * @return String of the reading date. 
     */
    public String getNextIntervalTime(){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastReadingTime);
        cal.add(Calendar.SECOND, intervalSeconds);
        String intDate = df.format(cal.getTime());
        return intDate;
    }
    
    /**
     * Decides if it is time to take a reading. 
     * @return boolean
     */
    public boolean onInterval()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        //Calls the getNextIntervalTime() to get the next interval time. 
        String intDate = getNextIntervalTime();
        
        Date intervalDate = new Date();
                
        try {
            //Trys to convert the intDate from a string to a Date. 
            intervalDate = df.parse(intDate);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
                
        if(intDate.equals(df.format(new Date())) || intervalDate.before(new Date())){
            //If the interval date is <= to current date then the sensor needs to take a reading. 
            System.out.println(lastReadingTime.getTime());
            
            //Collects the data for the sensor.
            collectData();
            
            //The Sequence Diagram says it should call the fieldstation but doesnt say what the string is
            station.update("data/buffer.ser", data);
            
            //Check to see if the latest reading is within the threshold or not 
            //and deactivates or activates the Actuator. 
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
    
    /**
     * Sets the field station of the Sensor. 
     * @param fieldStation FieldStation
     */
    public void setFieldStation(FieldStation fieldStation)
    {
        station = fieldStation;
    }
    
    /**
     *
     */
    public void activate()
    {
        actuator.activate();
    }
    
    /**
     * Calculated the location of the Sensor. 
     */
    public void calculateLocation()     
    {
        //change to realistic gps data
        //53.427854, -1.585078 bl
        //53.427649, -1.570444 br
        //53.433837, -1.571689 tr
        //53.433683, -1.586280 tl
        Random rand = new Random();
        
        //Generates a random vaule between a minimum and maximum 
        //so that all the Sensors appear in a given area. 
        int minLatValue = 427854;
        int maxLatValue = 433837;
        int range = (maxLatValue - minLatValue);
        int ranNum = rand.nextInt((range - 0 + 1) + 0);
        String newCoordinate = "53." + (minLatValue + ranNum);
        
        int minLongValue = 570444;
        int maxLongValue = 586280;
        int longRange = (maxLongValue - minLongValue);
        int longRanNum = rand.nextInt((longRange - 0 + 1) + 0);
        String longNewCoordinate = "-1." + (minLongValue + longRanNum);
        location = new GPSData(Float.parseFloat(newCoordinate), Float.parseFloat(longNewCoordinate), 0.5f);
    }
    
    
    /**
     * Check to see if the latest reading was within the threshold or not. s
     * @return boolean
     */
    public boolean withinThreshold()
    {        
        //If the threshold is the upper limit. 
        if(thresholdIsUpperLimit){
            //if the value if less than the threshold return true. 
            if(data.getValue() <= threshold){
                return true;
            } else {
                //if value is higher than threshold set the value to threshold
                if (threshold > 0)
                    data.setValue(threshold);
                //if the value if greater than the threshold return true. 
                return false;
            }
        } else {
            if(data.getValue() >= threshold) {
                //if the value if greater than the threshold return true. 
                return true;
            } else {
                //if the value if less than the threshold return true. 
                return false;
            }
        }        
    }
}
