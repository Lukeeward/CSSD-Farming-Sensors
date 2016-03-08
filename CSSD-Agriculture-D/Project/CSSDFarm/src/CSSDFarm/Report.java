package CSSDFarm;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * The Report gives an interface in which other classes can access historical
 * data relating to a FieldStation
 * 
 * @author Luke
 */
public class Report implements Serializable {
    
    private Map<String, HistoricalData> data = new HashMap<>();
    
    /**
     * Generates a new Report containing the supplied HistoricalData Map
     * 
     * @param FieldStationid The FieldStation the report corresponds to 
     * @param data The HashMap containing a FieldStations HistoricalData
     */
    public Report(String FieldStationid, Map<String, HistoricalData> data){
        this.data = data;
    }
    
    /**
     * Filters HistoricalDatas SensorData by a supplied sensorType String
     * returning the SensorDatas that are of that sensorType
     *
     * @param sensorType A sensorType String e.g. Soil moisture
     * @return A HistoricalData Vector containing the matching SensorDatas
     */
    public Vector<HistoricalData> getDataByType(String sensorType){
        Vector<HistoricalData> returnData = new Vector<HistoricalData>();
        for(Entry<String,HistoricalData> entry : data.entrySet()){
            if (entry.getValue().getType().equals(sensorType)){
                returnData.add(entry.getValue());
            }
        }
        return returnData;
    }
    
    /**
     * Filters HistoricalDatas SensorData by a supplied sensorId
     * returning the SensorDatas that are attributed to that sensor 
     * and generated on the supplied date.
     * 
     * @param sensorId The SensorId String with which to filter the SensorDatas
     * @param date The date with which to filter the SensorData with 
     * @return A vector of filtered SensorDatas that meet the ID and Date criteria
     */
    public Vector<SensorData> getDataForSensorOnDate(String sensorId, Date date){
        for(Entry<String,HistoricalData> entry : data.entrySet()){
            if(entry.getValue().getSensorId().equals(sensorId)){
                return entry.getValue().getData(date);
            }
        }
        return null;
    }
    
    /**
     * Filters HistoricalDatas SensorData by a supplied sensorType String
     * returning the SensorDatas that are attributed to that sensorType 
     * and generated on the supplied date.
     * 
     * @param sensorType The SensorType String with which to filter the SensorDatas
     * @param date The date with which to filter the SensorData with 
     * @return A vector of filtered SensorDatas that meet the ID and Date criteria
     */
    public Vector<SensorData> getDataByTypeAndDate(String sensorType, Date date){
        Vector<SensorData> returnData = new Vector<SensorData>();
        for(HistoricalData dt : getDataByType(sensorType))
        {
            Vector<SensorData> ret = dt.getData(date);
            if(ret.size() >= 1){
                System.out.println();
                returnData.add(ret.lastElement());
            }
        }
        return returnData;
    }
    
    /**
     * Add a SensorData value to a HistoricalData in the report
     *
     * @param sensorData the SensorData to add to a HistoricalData
     */
    public void addData(SensorData sensorData){
        HistoricalData historicalData = data.get(sensorData.getId());
        historicalData.addData(sensorData);
    } 
    
}
