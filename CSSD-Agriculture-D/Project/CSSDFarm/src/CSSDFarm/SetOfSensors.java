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
    
    public void checkSensors(){
        
    }
    
    public SensorData getData(String string){
        return null;
    }
}
