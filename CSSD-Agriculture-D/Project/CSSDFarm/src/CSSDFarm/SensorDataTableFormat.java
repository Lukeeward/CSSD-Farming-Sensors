package CSSDFarm;

import ca.odell.glazedlists.gui.TableFormat;
import java.util.Date;

/**
 * The Table Format class for the SensorData Table. 
 * @author lnseg
 */
public class SensorDataTableFormat implements TableFormat<SensorData> {

    /**
     * Gets the column count
     * 
     * @return int, the number of columns. 
     */
    public int getColumnCount() {
        return 6;
    }

    /**
     * Get the column name based on the column index.
     * 
     * @param column int, the index of the column.
     * @return String, the name of the column.
     */
    public String getColumnName(int column) {
        if(column == 0)      return "Id"; //Name of colum 0
        else if(column == 1) return "Unit"; //Name of colum 2
        else if(column == 2) return "Reading"; //Name of colum 3
        else if(column == 3) return "Last Updated"; //Name of colum 4
        else if(column == 4) return "GPS"; //Name of colum 5
        else if(column == 5) return "Power"; //Name of colum 6

        throw new IllegalStateException();
    }

    /**
     * Gets the colum value from the object for the specified column.
     * @param sensorData SensorData, the object to get the column content from.
     * @param column int, the column index.
     * @return Object, the value of the column.
     */
    public Object getColumnValue(SensorData sensorData, int column) {
        if(column == 0)      return sensorData.getId(); //Content of colum 0
        else if(column == 1) return sensorData.getUnit(); //Content of colum 1
        else if(column == 2) return sensorData.getValue(); //Content of colum 2
        else if(column == 3) { 
            long currentDate = new Date().getTime();
            long lastUpdated = sensorData.getFullDate().getTime();
            long diff = currentDate - lastUpdated;
            long diffSeconds = diff / 1000 % 60;
            long diffMinuites = diff / (60 * 1000) % 60; 
            long diffHours = diff / (60 * 60 * 1000); 
            if(diffMinuites <= 0){
                if (diffSeconds == 1)
                    return diffSeconds + " Second Ago";
                else
                    return diffSeconds + " Seconds Ago";
            }
            else
                if(diffHours <= 0)
                    return diffMinuites + " Minuites Ago";
                else
                    return diffHours + " Hours and " + diffMinuites + " Minuites Ago";
        } //Content of colum 3
            
        else if(column == 4) return sensorData.getLocation().GPStoString(); //Content of colum 4
        else if(column == 5) return (String.valueOf(sensorData.getPower()) + "%"); //Content of colum 5
        throw new IllegalStateException();
    }
}
