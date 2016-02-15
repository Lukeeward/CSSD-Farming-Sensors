/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CSSDFarm.FieldStation;
import CSSDFarm.Server;
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
    
    static final Server server = new Server();
    
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
        assertFalse(result == null);        
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
}
