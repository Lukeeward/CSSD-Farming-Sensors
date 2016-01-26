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
    GPSData gps = new GPSData(123445.23433f, 213123.323f, 231232.2323f); 
    
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
    public void testGetLongitude(){
        assertTrue(gps.getLongitude() == 213123.323f);
    }
    
    @Test
    public void testGetLatitude(){
        assertTrue(gps.getLatitude() == 123445.23433f);
    }
    
    @Test
    public void testGetAltitude(){
        assertTrue(gps.getAltitude() ==  231232.2323f);
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
