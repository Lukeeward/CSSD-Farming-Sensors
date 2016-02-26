package CSSDFarm;

import java.io.Serializable;

/**
* The Actuator system that interacts with Sensors
*/

public class Actuator implements Serializable{
    private boolean isActive;
    
    public Actuator(){
    }
    
    public Actuator(boolean isActive){
        this.isActive = isActive;
    }
    
    public void activate(){
        isActive = true;
    }
    
    public boolean isActive(){        
        return isActive;
    }
    
    public void deactivate(){
        isActive = false;
    }
}
