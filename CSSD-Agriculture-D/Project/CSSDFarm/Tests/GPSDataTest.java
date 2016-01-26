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
import CSSDFarm.GPSData;

/**
 *
 * @author Luke
 */
public class GPSDataTest {
    float latitude = 123445.23433f;
    float longitude = 213123.323f;
    float altitude = 231232.2323f;
    
    GPSData gps = new GPSData(latitude, longitude, altitude); 
    
    public GPSDataTest() {
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
    public void testGetLatitude(){
        assertTrue(gps.getLatitude() == latitude);
    }
    
    @Test
    public void testGetLongitude(){
        assertTrue(gps.getLongitude() == longitude);
    }
    
    @Test
    public void testGetAltitude(){
        assertTrue(gps.getAltitude() ==  altitude);
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
