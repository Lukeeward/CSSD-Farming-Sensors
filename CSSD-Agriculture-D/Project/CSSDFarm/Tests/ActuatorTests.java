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
import CSSDFarm.Actuator;

/**
 *
 * @author lnseg
 */
public class ActuatorTests {
    boolean isActive = false;
    Actuator actuator = new Actuator(isActive);
    
    public ActuatorTests() {
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
    public void testIsActive(){
        assertTrue(actuator.isActive() == isActive);
    }
    
    @Test
    public void testActivate(){
        actuator.activate();
        assertTrue(actuator.isActive() == true);
    }
    
    @Test
    public void testDeactivate(){
        actuator.deactivate();
        assertTrue(actuator.isActive() == false);
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
