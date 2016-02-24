package CSSDFarm;

import ca.odell.glazedlists.gui.TableFormat;

public class SensorDataTableFormat implements TableFormat<SensorData> {
    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int column) {
        if(column == 0)      return "Id";
        else if(column == 1) return "Unit";
        else if(column == 2) return "Reading";
        else if(column == 3) return "Last Updated";
        else if(column == 4) return "GPS";
        else if(column == 5) return "Power";

        throw new IllegalStateException();
    }

    public Object getColumnValue(SensorData sensorData, int column) {
        if(column == 0)      return sensorData.getID();
        else if(column == 1) return sensorData.getUnit();
        else if(column == 2) return sensorData.getValue();
        else if(column == 3) return sensorData.getFullDate();
        else if(column == 4) return sensorData.getLocation().GPStoString();
        else if(column == 5) return sensorData.getPower();
        throw new IllegalStateException();
    }
}
