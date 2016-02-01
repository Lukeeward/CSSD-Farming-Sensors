package CSSDFarm;
import java.util.Vector;

public class SetOfSensors {
    private Vector<Sensor> data;
    
    public void addSensor(Sensor sensor){
        data.add(sensor);
    }
    
    public void removeSensor(Sensor sensor){
        data.remove(sensor);
    }
    
    public Sensor getSensor(String id){
        for(Sensor aSensor : data){
            if(aSensor.getId() == id)
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
}
