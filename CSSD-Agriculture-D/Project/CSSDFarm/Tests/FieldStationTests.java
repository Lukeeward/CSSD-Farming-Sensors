/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CSSDFarm.FieldStation;
import CSSDFarm.GPSData;
import CSSDFarm.SensorData;
import java.io.File;
import java.util.Date;
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
public class FieldStationTests {
    
    FieldStation test = new FieldStation("heh", "hoh");
    public FieldStationTests() {
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
    public void bufferCreatedTest() {
        test.addToBuffer("data/buffer.ser", new SensorData("SensorId",new Date(),"mm", 123, new GPSData(1234f,1234f,1234f), 120));
        File bufferFile = new File("data/buffer.ser");
        assertTrue(bufferFile.exists());
    }
    
    @Test
    public void bufferClearedTest() {
        test.clearBuffer();
        File bufferFile = new File("data/buffer.ser");
        assertFalse(bufferFile.exists());
    }
}
