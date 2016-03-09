/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CSSDFarm.Actuator;
import CSSDFarm.FieldStation;
import CSSDFarm.GPSData;
import CSSDFarm.Sensor;
import CSSDFarm.SensorData;
import CSSDFarm.Server;
import CSSDFarm.SetOfSensors;
import CSSDFarm.UserInterface;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import javax.swing.JOptionPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Webby
 */
//Automated GUI test
//test both the class' and GUI and how they interact

public class UserInterfaceTests {
    static final Server server = Server.getInstance();
    UserInterface ui = new UserInterface();
    Robot bot;
    ClipboardOwner owner = new ClipboardOwner() {
        public void lostOwnership(Clipboard clipboard,Transferable contents){}
    };    
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection stringSelection;
    static int totalFieldStationsToAdd = 0;
    static int totalSensorsToAdd = 0;
    int testPassed = 0;
    
    public UserInterfaceTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        //set how many fieldStations and Sensors for each should the automation should add
        totalFieldStationsToAdd = 2;
        totalSensorsToAdd = 2;
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try{
            bot = new Robot();
        }
        catch(Exception eX){
            System.out.println(eX);
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGUI(){
        ui.setVisible(true);
        
        //login
        logIn();
        //move GUI to the manager screen
        goToManagerScreen();
        
        //remove all existing fieldStations and test
        removeAllFieldStations();
        
        //add the fieldstations and sensors 
        for (int i=0;i<totalFieldStationsToAdd;i++){
            addFieldStation();
            
            for (int l=0;l<totalSensorsToAdd;l++)
                addSensor();
        }
        
        //navigate GUI to report screen
        //goToReportScreen();
        
        JOptionPane.showMessageDialog(null,"Tests Passed: " + testPassed,"Test Results",JOptionPane.INFORMATION_MESSAGE);

        
        try {
            Thread.sleep(500);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void logIn(){
        //enters username and password on GUI
        selectAll();
        pasteString("John");
        tabKey();
        selectAll();
        pasteString("Password");
        tabKey();
        enterKey();
    }
    
    public void pasteString(String paste){
        stringSelection = new StringSelection(paste);
        clipboard.setContents(stringSelection, owner);
        bot.keyPress(KeyEvent.VK_CONTROL);
        bot.keyPress(KeyEvent.VK_V);
        bot.keyRelease(KeyEvent.VK_V);
        bot.keyRelease(KeyEvent.VK_CONTROL);
        waitForGui();
        
    }
    
    public void clickMouse(){
        waitForGui();
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        waitForGui();
    }
    
    public void enterKey(){
        waitForGui();
        bot.keyPress(KeyEvent.VK_ENTER);
        bot.keyRelease(KeyEvent.VK_ENTER);
        waitForGui();
    }
    
    public void goToManagerScreen(){
        bot.mouseMove(ui.btnManager.getLocationOnScreen().x,ui.btnManager.getLocationOnScreen().y);
        clickMouse();
    }
    
    public void goToReportScreen(){
        
        bot.mouseMove(ui.btnReport.getLocationOnScreen().x,ui.btnReport.getLocationOnScreen().y);
        clickMouse();
        waitForGui();
    }
    
    public void addFieldStation(){
        bot.mouseMove(ui.btnAddFieldStation.getLocationOnScreen().x,ui.btnAddFieldStation.getLocationOnScreen().y);
        clickMouse();
        int n = genRanNum(15000);
        pasteString("FieldStationTest" + n);
        tabKey();
        pasteString("FieldStationTest" + n);
        tabKey();
        spaceKey();
        waitForGui();
        FieldStation fname = (FieldStation)ui.listUserStations.getSelectedValue();
        assertTrue(fname.getName().equals("FieldStationTest" + n));
        testPassed();
        waitForGui();
    }
    
    public void testPassed(){
        testPassed = testPassed+1;
    }
    
    public void addSensor(){
        waitForGui();
        bot.mouseMove(ui.btnAddSensor.getLocationOnScreen().x,ui.btnAddSensor.getLocationOnScreen().y);
        clickMouse();
        waitForGui();
        int n = genRanNum(15000);
        ui.txtSensorId.setText("SensorTest" + n);
        tabKey();
        selectSensorType();
        selectSensorUnits();
        selectSensorIntervalSecs();
        selectThreshold();
        selectIfUpperLimit();
        waitForGui();
        String sensorName = ui.tblSensorTable.getValueAt(ui.tblSensorTable.getSelectedRow(), 0).toString();
        FieldStation fs = (FieldStation)ui.listUserStations.getSelectedValue();
        Sensor sens = fs.getSetOfSensors().getSensor(sensorName) ;
        assertTrue(sens.getId().equals("SensorTest" + n));
        testPassed();
        
        for (int x=0;x<1000;x++){
            //collects new sensor data
            //imitates the interval
            sens.collectData();
            assertTrue(sens.getData().getUnit() != null);
            testPassed();
            Actuator ac = sens.getActuator();
            if (sens.getThresholdIsUpperLimit()){
                if (!sens.withinThreshold()){
                        ac.activate();
                        assertTrue(ac.isActive());
                        testPassed();
                }
                else{
                    ac.deactivate();
                    assertFalse(ac.isActive());
                    testPassed();
                }
            }
            
        }
    }
    
    public int genRanNum(int limit){
        Random rand = new Random();
        return rand.nextInt(limit) + 1;
    }
    
    public void removeFieldStation(){
        waitForGui();
        FieldStation fname = (FieldStation)ui.listUserStations.getSelectedValue();
        bot.mouseMove(ui.btnRemoveFieldStation.getLocationOnScreen().x,ui.btnRemoveFieldStation.getLocationOnScreen().y);
        clickMouse();
        spaceKey();
        assertTrue(ui.listUserStations.getSelectedValue() != fname);
        testPassed();
    }
    
    public void selectSensorType(){
        //selects the sensor type 
        int totalSTypes = genRanNum(ui.comboSensorType.getItemCount());
        System.out.println(totalSTypes);
        for (int l=0;l<totalSTypes;l++)
            downKey();
        enterKey();
        tabKey();
    }
    
    public void selectSensorUnits(){
        int totalSUnits = genRanNum(ui.comboSensorUnits.getItemCount());
        for (int i=0;i<totalSUnits;i++)
            downKey();
        enterKey();
        tabKey();
        tabKey();
        tabKey();
        tabKey();
    }
    
    public void selectThreshold(){
        pasteString(String.valueOf(genRanNum(150)));
        tabKey();
    }
    
    public void selectIfUpperLimit(){
        int total = genRanNum(1);
        for (int i=0;i<total;i++)
            spaceKey();
        bot.mouseMove(ui.btnSaveSensor.getLocationOnScreen().x,ui.btnSaveSensor.getLocationOnScreen().y);
        clickMouse();
    }
    
    private void selectSensorIntervalSecs() {
        int totalS = genRanNum(30);
        if (totalS <= 1)
            totalS = 2;
        for (int i=0;i<totalS;i++)
            downKey();
        tabKey();
    }
    
    public void removeAllFieldStations(){
        waitForGui();
        ui.listUserStations.setSelectedIndex(0);
        int totalFieldStations = ui.listUserStations.getModel().getSize();
        System.out.println(totalFieldStations);
        for (int i=0;i<totalFieldStations;i++)
            removeFieldStation();
    }
    
    public void spaceKey(){
        waitForGui();
        bot.keyPress(KeyEvent.VK_SPACE);
        bot.keyRelease(KeyEvent.VK_SPACE);
        waitForGui();
    }
    
    public void selectAll(){
        waitForGui();
        bot.keyPress(KeyEvent.VK_CONTROL);
        bot.keyPress(KeyEvent.VK_A);
        waitForGui();
    }
    
    public void tabKey(){
        waitForGui();
        bot.keyPress(KeyEvent.VK_TAB);
        bot.keyRelease(KeyEvent.VK_TAB);
        waitForGui();
    }
    
    public void downKey(){
        waitForGui();
        bot.keyPress(KeyEvent.VK_DOWN);
        bot.keyRelease(KeyEvent.VK_DOWN);
    }
    
    public void waitForGui(){
        try {
            Thread.sleep(250);                 
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    
    
    
}
