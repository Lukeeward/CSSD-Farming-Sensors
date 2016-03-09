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

    /**
     * Gets the colum type for the SensorTable. 
     * 
     * @return int, the number of columns.
     */
    public int getColumnCount() {
        return 4;
    }

    /**
     * Gets the column name depending on the column index.
     * 
     * @param column, the column index.
     * @return String, the name of the column.
     */
    public String getColumnName(int column) {
        if(column == 0)      return "Id"; //Name of colum 0
        else if(column == 1) return "Type"; //Name of colum 1
        else if(column == 2) return "Unit"; //Name of colum 2
        else if(column == 3) return "GPS"; //Name of colum 3

        throw new IllegalStateException();
    }

    /**
     * Gets the colum value from the object for the specified column.
     * 
     * @param sensor Sensor, the object to get the column content from.
     * @param column int, the column index.
     * @return Object, the value of the column.
     */
    public Object getColumnValue(Sensor sensor, int column) {
        if(column == 0)      return sensor.getId(); //Content of colum 0
        else if(column == 1) return sensor.getType(); //Content of colum 1
        else if(column == 2) return sensor.getUnits(); //Content of colum 2
        else if(column == 3) return sensor.getGps().GPStoString(); //Content of colum 3

        throw new IllegalStateException();
    }
}
