package CSSDFarm;
import java.util.Date;
import java.util.Vector;

class HistoricalData {
    private String dataType;
    private String fieldStationID;
    private String sensorID;
    private Vector<SensorData> data;
    
    public HistoricalData(){
    }
    
    public SensorData getData(Date date){
        return null;
    }
    
    public SensorData getMostRecent(){
        return null;
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
