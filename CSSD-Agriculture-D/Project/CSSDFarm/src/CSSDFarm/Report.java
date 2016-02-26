package CSSDFarm;
import java.util.*;
import java.util.Map.Entry;

public class Report {
    
    private Map<String, HistoricalData> data = new HashMap<>();
    
    public Report(String FieldStationid, Map<String, HistoricalData> data){
        this.data = data;
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
    
    public Vector<SensorData> getDataForSensorOnDate(String sensorId, Date date){
        for(Entry<String,HistoricalData> entry : data.entrySet()){
            if(entry.getValue().getSensorId().equals(sensorId)){
                return entry.getValue().getData(date);
            }
        }
        return null;
    }
    
    public Vector<SensorData> getDataByTypeAndDate(String sensorType, Date date){
        Vector<SensorData> returnData = new Vector<SensorData>();
        for(HistoricalData dt : getDataByType(sensorType))
        {
            Vector<SensorData> ret = dt.getData(date);
            if(ret.size() >= 1){
                returnData.add(ret.lastElement());
            }
        }
        return returnData;
    }
    
    public void addData(SensorData sensorData){
        
    } 
    
}
