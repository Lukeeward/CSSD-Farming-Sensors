/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import CSSDFarm.Sensor;
import java.util.UUID;
import CSSDFarm.FieldStation;
import CSSDFarm.Actuator;
/**
 *
 * @author Luke
 */
public class SensorTests {
    String id = UUID.randomUUID().toString();
    FieldStation station = new FieldStation("123","Lukeyx2");
    int interval = 200;
    String type = "Temperature"; 
    float power = 1234.5f; //??? replace me when we know what power is
    float threshold = 20f;
    boolean thresholdIsUpperLimit = true;
    Actuator actuator = new Actuator();
    
    //I have presumed these are the values needed for a sensor constructor
    Sensor testSensor = new Sensor(id,station,interval,type,power,threshold,thresholdIsUpperLimit,actuator);
    
    public SensorTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testWithinThreshold(){

        String id = UUID.randomUUID().toString();
        FieldStation station = new FieldStation("123", "Lukey123");
        int interval = 200;
        String type = "Temperature"; 
        float power = 1234.5f; //??? replace me when we know what power is
        float threshold = 20f;
        boolean thresholdIsUpperLimit = true;
        Actuator actuator = new Actuator();

        //I have presumed these are the values needed for a sensor constructor
        Sensor thresholdTestSensor = new Sensor(id,station,interval,type,power,threshold,thresholdIsUpperLimit,actuator);

        //SensorData is 12.00f and threshold is 20.00f
        //threshholdIsUpperLimit = true
        //within the threshold
        assertTrue(thresholdTestSensor.withinThreshold());

        threshold = 10f;
        thresholdTestSensor = new Sensor(id,station,interval,type,power,threshold,thresholdIsUpperLimit,actuator);

        //SensorData is 12.00f and threshold is 10.00f
        //threshholdIsUpperLimit = true
        //above the threshold
        assertFalse(thresholdTestSensor.withinThreshold());

        threshold = 10f;
        thresholdIsUpperLimit = false;
        thresholdTestSensor = new Sensor(id,station,interval,type,power,threshold,thresholdIsUpperLimit,actuator);

        //SensorData is 12.00f and threshold is 10.00f
        //threshholdIsUpperLimit = false
        //within threshold
        assertTrue(thresholdTestSensor.withinThreshold());

        threshold = 20f;
        thresholdTestSensor = new Sensor(id,station,interval,type,power,threshold,thresholdIsUpperLimit,actuator);

        //SensorData is 12.00f and threshold is 10.00f
        //threshholdIsUpperLimit = false
        //below threshold
        assertFalse(thresholdTestSensor.withinThreshold());
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
