package CSSDFarm;

public class GPSData {
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
        
    public String GPStoString(){
        return latitude + "," + longitude + "," + altitude;
    }
}
