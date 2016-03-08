package CSSDFarm;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;


/**
* HistoricalData contains the SensorData relating to a sensor within a FieldStation
*/

class HistoricalData implements Serializable {
    private String dataType;
    private String fieldStationID;
    private String sensorID;
    private Vector<SensorData> data;
    
    /**
     * Generates a new HistoricalData consisting of an empty SensorData vector
     * relates to the supplied FieldStation and Sensor id.
     * 
     * @param SensorType The SensorType the sensor data related to
     * @param fieldStationId The FieldStation id the historical data class related to
     * @param sensorId The Sensor the contained data is from
     */
    public HistoricalData(String SensorType, String fieldStationId, String sensorId){
        this.dataType = SensorType;
        this.fieldStationID = fieldStationId;
        this.sensorID = sensorId;
        this.data = new Vector<SensorData>();
    }
    
    /**
     * Returns a vector of SensorDatas contained locally within the Historical Data object
     * 
     * @param date The date in which to filter the SensorData by
     * @return A vector of filtered SensorData
     */
    public Vector<SensorData> getData(Date date){
        Vector<SensorData> returnData = new Vector<SensorData>();
        
        //format the date so it matches the .getDate() returned date format
        DateFormat inputformatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = inputformatter.format(date);
        
        try {
            //try to convert the string back to a date. 
            date = inputformatter.parse(newDate);
        } catch(ParseException ex) {
            System.out.println(ex); 
        }
        
        for(SensorData sensorData : data)
        {
            //for each SensorData. 
            if (sensorData.getDate().equals(date)){
                //If dates are the same add to return data.
                returnData.add(sensorData);
            }
        }
        
        //Sort the return data by date. 
        Collections.sort(returnData, new Comparator<SensorData>() {
            public int compare(SensorData o1, SensorData o2) {
                return o1.getFullDate().compareTo(o2.getFullDate());
            }
        });
        
        return returnData;
    }
    
    /**
     * Returns the most recently added SensorData
     * 
     * @return The most recently added SensorData Object
     */
    public SensorData getMostRecent(){
        return data.lastElement();
    }
    
    /**
     * Returns the HistoricalData SensorData type
     * 
     * @return A string consisting of a SensorData type
     */
    public String getType(){
        return dataType;
    }
    
    /**
     * Adds a new SensorData to the HistoricalData data vector
     * 
     * @param data The SensorData object to add
     */
    public void addData(SensorData data){
        this.data.add(data);
    }
    
    /**
     * Returns the HistoricalData's corresponding SensorId
     * 
     * @return A string consisting of a SensorData id
     */
    public String getSensorId(){
        return this.sensorID;
    }
    
    //GET MOST RECENT TWICE!? Maybe getAllData?
    /*
    public SensorData getMostRecent(){
        return null;
    }
    */
}
