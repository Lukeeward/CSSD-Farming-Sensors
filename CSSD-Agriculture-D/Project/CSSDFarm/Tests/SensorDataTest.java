/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CSSDFarm.GPSData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import CSSDFarm.SensorData;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Webby
 */
public class SensorDataTest {
    String id = UUID.randomUUID().toString();
    Date time = new Date();
    GPSData gps = new GPSData(123445.23433f, 213123.323f, 231232.2323f); 
    SensorData sensorData = new SensorData(id, time, "mm", 11, gps);
    
    public SensorDataTest() {
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
    public void testGetValue(){
        assertTrue(sensorData.getValue() == 11);
    }
    
    public void testGetUnit(){
        assertTrue("mm".equals(sensorData.getUnit()));
    }
    
    public void testGetTime(){
        assertTrue(sensorData.getTime() ==  time);
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
