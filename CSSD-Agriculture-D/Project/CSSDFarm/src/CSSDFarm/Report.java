package CSSDFarm;
import java.util.*;

public class Report {
    
    private Map<String, HistoricalData> data;
    
    public Report(String FieldStationid, HistoricalData data){
        this.data.put(FieldStationid, data);
    }
    
    public Vector<SensorData> getDataByDate(Date date){
        return null;
    }
    
    public Vector<HistoricalData> getDataByType(String s1){
        return null;
    }
    
    public Vector<SensorData> getDataByTypeAndDate(String s1, Date date){
        return null;
    }
    
    public void addData(SensorData sensorData){
        
    } 
    
}
