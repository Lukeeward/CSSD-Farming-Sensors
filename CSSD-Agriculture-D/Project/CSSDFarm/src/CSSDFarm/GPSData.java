package CSSDFarm;

import java.io.Serializable;

public class GPSData implements Serializable{
    private float latitude;
    private float longitude;
    private float altitude;
    
    public GPSData(float latitude, float longitude, float altitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    public float getLatitude(){
        return latitude;
    }
    
    public float getLongitude(){
        return longitude;
    }
    
    public float getAltitude(){
        return altitude;
    }
    
    @Override
    public String toString() {
        return "{lat : " + this.latitude + ", long : " + this.longitude + ",alt : " + this.altitude + "}";
    }
        
    public String GPStoString(){
        return latitude + "," + longitude + "," + altitude;
    }
}
