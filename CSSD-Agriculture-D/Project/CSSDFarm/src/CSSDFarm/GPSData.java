package CSSDFarm;

import java.io.Serializable;

/**
 * GPSData contains the sensors current geographical coordinates
 * @author Luke
 */
public class GPSData implements Serializable{
    private float latitude;
    private float longitude;
    private float altitude;
    
    /**
     * GPSData Constructor takes the current GPS latitude, longitude and altitude 
     * and sets the relevant attributes
     * 
     * @param latitude A float representing the latitude geographic coordinate of the GPS
     * @param longitude A float representing the longitude geographic coordinate of the GPS
     * @param altitude A float representing the altitude of the GPS
     */
    public GPSData(float latitude, float longitude, float altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    /**
     * Gets the GPS latitude value
     * @return The current GPS latitude float
     */
    public float getLatitude(){
        return latitude;
    }
    
    /**
     * Gets the GPS longitude value
     * @return The current GPS longitude float
     */
    public float getLongitude(){
        return longitude;
    }
    
    /**
     * Gets the GPS altitude value
     * @return The current GPS altitude float
     */
    public float getAltitude(){
        return altitude;
    }
    
    /**
     * Override toString function to be used for the heat map.
     * @return Formatted string of the GPS coordinates.
     */
    @Override
    public String toString() {
        return "{lat : " + this.latitude + ", long : " + this.longitude + ",alt : " + this.altitude + "}";
    }
        
    /**
     * The GPS latitude, longitude and altitude formatted to a geographic coordinate string
     * @return The GPS latitude, longitude and altitude string
     */
    public String GPStoString(){
        return latitude + "," + longitude + "," + altitude;
    }
}
