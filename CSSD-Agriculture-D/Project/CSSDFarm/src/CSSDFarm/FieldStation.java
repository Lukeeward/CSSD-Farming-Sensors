package CSSDFarm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The field station has a set of sensors attached to it and deals with the 
 * collection of data from each sensor.
 */
public class FieldStation implements Serializable {
    private String name;
    private String id;
    private SetOfSensors sensors;
    
    /**
     * Constructor for FieldStation class.
     * 
     * @param id String that sets the id of the FieldStation
     * @param name String that sets the name of the FieldStation
     */
    public FieldStation(String id, String name){
        this.id = id;
        this.name = name;
        
        //Initialises the SetOfSensors sensors.
        sensors = new SetOfSensors();
    }
    
    /**
     * Gets the id of the FieldStation
     * 
     * @return id String
     */
    public String getId(){
        return id;
    }
    
    /**
     * Gets the name of the FieldStation
     * 
     * @return name String
     */
    public String getName(){
        return name;
    }
    
    /**
     * Gets the sensors attached to the FieldStation
     * 
     * @return sensors SetOfSensors
     */
    public SetOfSensors getSetOfSensors(){
        return sensors;
    }
    
    /**
     * Adds a Sensor to the SetOfSensors sensors and sets the FieldStation on that Sensor. 
     * 
     * @param sensor Sensor
     */
    public void addSensor(Sensor sensor){
        sensors.addSensor(sensor);
        sensor.setFieldStation(this);
    }
    
    /**
     * Removes a Sensor from the SetOfSensors sensors.
     * 
     * @param id String, the id of the Sensor to remove. 
     */
    public void removeSensor(String id){
        sensors.removeSensor(sensors.getSensor(id));
    }

    /**
     * Adds SensorData to the Buffer.
     * Then tries to write that Buffer to the Server if there's a connection.
     * 
     * @param filename String
     * @param sensorData SensorData
     */
    public void update(String filename, SensorData sensorData){
        //Adds the sensor data to the buffer. 
        addToBuffer(filename, sensorData);
        
        //Uploads the data to the server if it's turned on. 
        if (Server.getInstance().getServerIsOn())
            if(uploadData()){
                //If the upload was successful, clear the buffer.
                clearBuffer();
            }
    }
    
    /**
     * Returns the SensorData from a found Sensor.
     * 
     * @param id The SensorId to get SensorData from
     * @return
     */
    public SensorData getData(String sensorId){
        return sensors.getData(sensorId);
    }
    
    /**
     * Uploads the data from the Buffer to the Server.
     * 
     * @return Successful boolean to see if the update was successful.
     */
    public boolean uploadData(){
        ObjectInputStream instream = null;
        Vector<SensorData> dummyData = new Vector<SensorData>();
        
        try {
            //Tries to create an ObjectInStream for the buffer serialisation file.
            FileInputStream fileinput = new FileInputStream ("data/buffer.ser");
            instream = new ObjectInputStream(fileinput);
            
            //Loops through the ObjectInputStream
            do{
                try {
                    //Tries to get an object from the ObjectInputStream
                    SensorData newData = (SensorData)instream.readObject();
                    //Adds the data to the Vector<SensorData> if not null.
                    if (newData != null)
                        dummyData.add(newData);
                } catch(ClassNotFoundException ex) {
                    //Caught Exception if the instream.readObject() isn't of type SensorData
                    System.out.println(ex);
                }
            } while (true);
        } catch(IOException io) {
            //Caught Exception if the instream.readObject found the end of the file or failed to create the instream
            System.out.println(io);             
        }
        
        try {
            //Tries to colse the instream.
            instream.close();
        } catch (IOException ex) {
            System.out.println(ex); 
        }
    
        if (dummyData.size() > 0){
            //if there was any SensorData found in the Buffer add it to the Server. 
            Server.getInstance().addData(dummyData, id);
            return true;
        } else {
            //Otherwise something went wrong or there was no SensorData to add so return false.
            return false;
        }
            
            
    }
    
    /**
     * Adds the SensorData to the Buffer.
     * 
     * @param filename String the name of the buffer file.
     * @param sensorData SensorData the SensorData to be added to the Buffer.
     */
    public void addToBuffer(String filename, SensorData sensorData){
        ObjectOutputStream outstream;
        try {
            File bufferFile = new File(filename);
            //Checks to see if the buffer file already exists. 
            if(bufferFile.exists())
            {
                //Create an AppendableObjectOutputStream to add the the end of the buffer file as opposed to over writeing it. 
                outstream = new AppendableObjectOutputStream(new FileOutputStream (filename, true));
            } else {
                //Create an ObjectOutputStream to create a new Buffer file. 
                outstream = new ObjectOutputStream(new FileOutputStream (filename));   
            }
            //Adds the SensorData to the end of the buffer file.
            outstream.writeObject(sensorData);
            outstream.close();
        } catch(IOException io) {
            System.out.println(io);
        }
    }
    
    /**
     * Clears down the Buffer after the content has been added to the Server to avoid replications.
     */
    public void clearBuffer(){
        System.out.println("Clearning beffer");
        //Deletes the buffer file
        File bufferFile = new File("data/buffer.ser");
        bufferFile.delete();
    }
    
    /**
     * Adds a new Sensor to the SetOfSensors.
     * 
     * @param sensorId String, the sensor Id
     * @param sensorType String, the type of Sensor e.g. Soil Moisture, Soil Acidity, Light Intensity.
     * @param sensorUnits String, the unit of measurement relating to the Sensor Type e.g. %, C, F.
     * @param interval int, the interval time between each reading. 
     * @param threshold int, the threshold of the Sensor reading to know when to activate the Actuator. 
     * @param upperlimit boolean, whether the threshold is the upper or lower limit. 
     */
    public void addSensor(String sensorId, String sensorType, String sensorUnits, int interval, int threshold, boolean upperlimit){
        Sensor theSensor = new Sensor(sensorId, sensorType, sensorUnits, interval, threshold, upperlimit);
        sensors.addSensor(theSensor);
        theSensor.setFieldStation(this);
    }
    
    
    //The AppendableObjectOutputStream to Append the the buffer file as opposed to over writing it. 
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
       public AppendableObjectOutputStream(OutputStream out) throws IOException {
         super(out);
       }

       @Override
       protected void writeStreamHeader() throws IOException {}
    }
}
