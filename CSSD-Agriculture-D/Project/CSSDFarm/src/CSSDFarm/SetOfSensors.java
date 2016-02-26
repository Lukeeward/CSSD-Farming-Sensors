package CSSDFarm;
import java.io.Serializable;
import java.util.Vector;

public class SetOfSensors implements Serializable{
    private Vector<Sensor> data;
    
    public SetOfSensors() {
        data = new Vector<Sensor>();
    }
    
    public void addSensor(Sensor sensor){
        data.add(sensor);
    }
    
    public void removeSensor(Sensor sensor){
        data.remove(sensor);
    }
    
    public Vector<Sensor> getSensors(){
        return data;
    }
    
    public Sensor getSensor(String id){
        for(Sensor aSensor : data){
            if(aSensor.getId().equals(id))
            {
                return aSensor;
            }
        }
        return null;
    }
    
    public void checkSensors(){
        //Check sensor threshold??
    }
    
    public SensorData getData(String string){
        return null;
    }
    
    public Vector<Sensor> getByType(String type){
        Vector<Sensor> sensors = new Vector<Sensor>();
        for(Sensor sensor : data){
            if(sensor.getType() == type)
                sensors.add(sensor);
        }
        return sensors;
    }
}
