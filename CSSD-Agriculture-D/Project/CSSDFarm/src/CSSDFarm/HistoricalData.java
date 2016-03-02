package CSSDFarm;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

class HistoricalData implements Serializable {
    private String dataType;
    private String fieldStationID;
    private String sensorID;
    private Vector<SensorData> data;
    
    public HistoricalData(String SensorType, String fieldStationId, String sensorId){
        this.dataType = SensorType;
        this.fieldStationID = fieldStationId;
        this.sensorID = sensorId;
        this.data = new Vector<SensorData>();
    }
    
    public Vector<SensorData> getData(Date date){
        Vector<SensorData> returnData = new Vector<SensorData>();
        
        //format the date so it matches the .getDate() returned date format
        DateFormat inputformatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = inputformatter.format(date);
        try {
            date = inputformatter.parse(newDate);
        } catch(ParseException ex) {
            
        }
        
        for(SensorData dt : data)
        {
            if (dt.getDate().equals(date)){
                returnData.add(dt);
            }
        }
        
        Collections.sort(returnData, new Comparator<SensorData>() {
            public int compare(SensorData o1, SensorData o2) {
                return o1.getFullDate().compareTo(o2.getFullDate());
                //return Float.compare(o1.getValue(), o2.getValue());
            }
        });
        
        return returnData;
    }
    
    public SensorData getMostRecent(){
        return null;
    }
    
    public String getType(){
        return dataType;
    }
    
    public void addData(SensorData data){
        this.data.add(data);
    }
    
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
