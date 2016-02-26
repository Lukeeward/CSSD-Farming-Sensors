package CSSDFarm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldStation {
    private String name;
    private String id;
    private SetOfSensors sensors;
    private Vector<SensorData> buffer;
    
    public FieldStation(String id, String name)
    {
        this.id = id;
        this.name = name;
        sensors = new SetOfSensors();
    }
    
    public Report compileReport(){
        return null;
    }
    
    public String getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public SetOfSensors getSetOfSensors(){
        return sensors;
    }
    
    public void addSensor(Sensor sensor){
        sensors.addSensor(sensor);
        sensor.setFieldStation(this);
    }
    
    public void removeSensor(String id){
        sensors.removeSensor(sensors.getSensor(id));
    }
    
    //string1 could be the filename of where the data is stored?
    public void update(String filename, SensorData sensorData){
        addToBuffer(filename, sensorData);
        if(uploadData())
        {
            clearBuffer();
        }
    }
    
    public SensorData getData(String id){
        return null;
    }
    
    public boolean uploadData(){
        ObjectInputStream instream = null;
        Vector<SensorData> dummyData = new Vector<SensorData>();
        try {
            FileInputStream fileinput = new FileInputStream ("buffer.ser");
            instream = new ObjectInputStream(fileinput);
            do{
                try {
                    SensorData newData = (SensorData)instream.readObject();
                    dummyData.add(newData);
                } catch(ClassNotFoundException ex) {
                    System.out.println(ex);
                }
            } while (true);
        } catch(IOException io) {
            System.out.println(io);             
        }
        try {
            instream.close();
        } catch (IOException ex) {
            System.out.println(ex); 
        }
    

        //        dummyData.add(new SensorData("test", new Date(),"mm",reading, new GPSData(53.367785f, -1.507226f, 0.5f), 120));
        Server.getInstance().addData(dummyData, id);
        return true;
    }
    
    public void addToBuffer(String filename, SensorData sensorData){
        ObjectOutputStream outstream;
        try {
            File bufferFile = new File(filename);
            if(bufferFile.exists())
            {
                outstream = new AppendableObjectOutputStream(new FileOutputStream (filename, true));
            } else {
                outstream = new ObjectOutputStream(new FileOutputStream (filename));   
            }
            outstream.writeObject(sensorData);
            outstream.close();
        } catch(IOException io) {
            System.out.println(io);
        }
        
        //This could be where the data is serialised before being sent to the server
    }
    
    public void clearBuffer(){
        System.out.print("Clearning beffer");
        File bufferFile = new File("buffer.ser");
        bufferFile.delete();
    }
    
    public void addSensor(String sensorId, String sensorType, String sensorUnits, int interval){
        Sensor theSensor = new Sensor(sensorId, sensorType, sensorUnits, interval);
        sensors.addSensor(theSensor);
        theSensor.setFieldStation(this);
    }
    
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
       public AppendableObjectOutputStream(OutputStream out) throws IOException {
         super(out);
       }

       @Override
       protected void writeStreamHeader() throws IOException {}
    }
}
