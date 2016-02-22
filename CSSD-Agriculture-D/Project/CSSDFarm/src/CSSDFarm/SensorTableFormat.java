/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSSDFarm;

import ca.odell.glazedlists.gui.TableFormat;

/**
 *
 * @author lnseg
 */
public class SensorTableFormat implements TableFormat<Sensor> {
    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int column) {
        if(column == 0)      return "Id";
        else if(column == 1) return "Type";
        else if(column == 2) return "Unit";
        else if(column == 3) return "GPS";

        throw new IllegalStateException();
    }

    public Object getColumnValue(Sensor sensor, int column) {
        if(column == 0)      return sensor.getId();
        else if(column == 1) return sensor.getType();
        else if(column == 2) return sensor.getUnits();
        else if(column == 3) return sensor.getGps().GPStoString();

        throw new IllegalStateException();
    }
}
