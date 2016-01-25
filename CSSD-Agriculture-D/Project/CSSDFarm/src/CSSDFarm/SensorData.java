/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSSDFarm;

import java.util.Date;

/**
 *
 * @author Tom
 */
public class SensorData {
    
    private String sensorID;
    private Date time;
    private String unit;
    private float value;
    private GPSData location;
    
    public float getValue()
    {
        return 0;
    }
    
    public String getUnit()
    {
        return null;
    }
    
    public void convertUnit()
    {
    }
    
    public Date getTime()
    {
        return null;
    }
}
