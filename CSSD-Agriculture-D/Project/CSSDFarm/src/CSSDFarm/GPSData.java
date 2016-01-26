package CSSDFarm;

public class GPSData {
    private float latitude;
    private float longitude;
    private float altitude;
    
    public GPSData(float lat, float lon, float alt){
        latitude = lat;
        longitude = lon;
        altitude = alt;
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
}
