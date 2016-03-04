/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CSSDFarm.FieldStation;
import CSSDFarm.GPSData;
import CSSDFarm.Report;
import CSSDFarm.SensorData;
import CSSDFarm.Server;
import java.util.Date;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luke
 */
public class ServerTests {
    
    static final Server server = Server.getInstance();
    
    public ServerTests() {
        server.authenticateUser("John", "Password");
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
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testRemoveFeldStation(){
        server.addFieldStation("Luke", "Station");
        server.removeFieldStation("Luke");
        FieldStation result = server.getFieldStation("Luke");
        //assertFalse(result == null);        
    }
    
    @Test
    public void testAddFeldStation(){
        server.addFieldStation("Luke", "Station");
        FieldStation result = server.getFieldStation("Luke");
        assertFalse(result == null);        
    }
    
    @Test
    public void testAuthenticateUser(){        
        boolean result = server.authenticateUser("John", "Password");
        assertTrue(result == true);        
    }
    
    @Test
    public void testServerUpload(){        
        server.addFieldStation("test1", "test2");
        server.addSensor("test1", "sensor", "Soil Moisture", "%", 10, 55, true);
        FieldStation result = server.getFieldStation("test1");
        
        if(!server.getTurnedOn())
            server.togglePower(1);
        Date readingDate = new Date();
        result.update("data/buffer.ser", new SensorData("sensor", readingDate,"LUX",55, new GPSData(12345f, 12345f, 0.5f), 120));
        Report resultReport = server.compileReport("test1");
        assertTrue(resultReport.getDataForSensorOnDate("sensor",readingDate).size() > 0);        
    }
    
    @Test
    public void testServerNotOn(){       
        //When server is turned off no data will be uploaded by the fieldstation
        //So the report generated will be empty
        server.addFieldStation("test1", "test2");
        server.addSensor("test1", "sensor", "Soil Moisture", "%", 10, 55, true);
        FieldStation result = server.getFieldStation("test1");
        Date readingDate = new Date();
        server.togglePower(0);
        result.update("data/buffer.ser", new SensorData("sensor", readingDate,"LUX",55, new GPSData(12345f, 12345f, 0.5f), 120));
        Report resultReport = server.compileReport("test1");
        assertTrue(resultReport.getDataForSensorOnDate("sensor",readingDate).isEmpty());       
        
        //When the server is turned back on, at the next upload all data is uploaded
        server.togglePower(1);
        result.update("data/buffer.ser", new SensorData("sensor", readingDate,"LUX",55, new GPSData(12345f, 12345f, 0.5f), 120));
        Report serverOnResultReport = server.compileReport("test1");
        assertTrue(serverOnResultReport.getDataForSensorOnDate("sensor",readingDate).size() == 2);       
        
        
    }
}
