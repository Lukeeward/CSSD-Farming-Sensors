package CSSDFarm;
import java.util.*;
import java.util.Map.Entry;

public class Report {
    
    private Map<String, HistoricalData> data = new HashMap<>();
    
    public Report(String FieldStationid, HistoricalData data){
        this.data.put(FieldStationid, data);
    }
    
    public Vector<SensorData> getDataByDate(Date date){
        return null;
    }
    
    public Vector<HistoricalData> getDataByType(String sensorType){
        Vector<HistoricalData> returnData = new Vector<HistoricalData>();
        for(Entry<String,HistoricalData> entry : data.entrySet()){
            if (entry.getValue().getType().equals(sensorType)){
                returnData.add(entry.getValue());
            }
        }
        return returnData;
    }
    
    public Vector<SensorData> getDataByTypeAndDate(String sensorType, Date date){
        Vector<SensorData> returnData = new Vector<SensorData>();
        for(HistoricalData dt : getDataByType(sensorType))
        {
            returnData.addAll(dt.getData(date));
        }
        return returnData;
    }
    
    public void addData(SensorData sensorData){
        
    } 
    
}
