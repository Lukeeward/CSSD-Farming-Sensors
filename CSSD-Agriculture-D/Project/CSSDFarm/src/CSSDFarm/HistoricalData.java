package CSSDFarm;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

class HistoricalData {
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
        for(SensorData dt : data)
        {
            if (dt.getDate().equals(date)){
                returnData.add(dt);
            }
        }
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
    
    
    //GET MOST RECENT TWICE!? Maybe getAllData?
    /*
    public SensorData getMostRecent(){
        return null;
    }
    */
}
