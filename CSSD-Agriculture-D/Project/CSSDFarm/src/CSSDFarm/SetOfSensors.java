package CSSDFarm;
import java.io.Serializable;
import java.util.Vector;

/**
 * A Set of sensors class that deals with the addition and removal of sensors from the field station.
 * @author Tom
 */
public class SetOfSensors implements Serializable{
    private Vector<Sensor> data;
    
    /**
     * Constructor that initialises a new vector of sensors
     */
    public SetOfSensors() {
        data = new Vector<Sensor>();
    }
    
    /**
     * Add a sensor to the set of sensors 
     * @param sensor
     */
    public void addSensor(Sensor sensor){
        data.add(sensor);
    }
    
    /**
     * Remove a sensor from the set of sensors
     * @param sensor
     */
    public void removeSensor(Sensor sensor){
        data.remove(sensor);
    }
    
    /**
     * Return the vector of sensors 
     * @return 
     */
    public Vector<Sensor> getSensors(){
        return data;
    }
    
    /**
     * Get a sensor with the ID you pass in. Return null if no such sensor exists
     * @param id
     * @return
     */
    public Sensor getSensor(String id){
        for(Sensor aSensor : data){
            if(aSensor.getId().equals(id))
            {
                return aSensor;
            }
        }
        return null;
    }
    
    /**
     * Finds a sensor by sensorid and returns its SensorData
     * 
     * @param sensorId The SensorId of the sensor to get the data from
     * @return A SensorData for the found sensor
     */
    public SensorData getData(String sensorId){
        for(Sensor sensor : data){
            if(sensor.getId().equals(sensorId)){
                return sensor.getData();
            }
        }
        return null;
    }
    
    /**
     * Get all sensors of a specific type
     * @param type
     * @return
     */
    public Vector<Sensor> getByType(String type){
        Vector<Sensor> sensors = new Vector<Sensor>();
        for(Sensor sensor : data){
            if(sensor.getType() == type)
                sensors.add(sensor);
        }
        return sensors;
    }
}
