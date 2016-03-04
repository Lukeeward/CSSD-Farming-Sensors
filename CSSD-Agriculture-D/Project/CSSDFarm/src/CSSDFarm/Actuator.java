package CSSDFarm;

import java.io.Serializable;

/**
* The Actuator is the mechanism attached to sensors that, when active, aims to 
* increase or decrease the sensors data reading through a mechanical action. e.g. turning on sprinkler
*/

public class Actuator implements Serializable{
    private boolean isActive;
    
    /**
     * Default constructor for Actuator 
     */
    public Actuator(){
    }
    
    /**
     * Constructor for Actuator class.
     * The supplied boolean sets the Actuators isActive attribute upon creation
     *
     * @param isActive boolean determining whether the actuator is On or Off
     */
    public Actuator(boolean isActive){
        this.isActive = isActive;
    }
    
    /**
     * Sets the Actuators isActive attribute to true 
     * meaning the Actuator is currently active.
     */
    public void activate(){
        isActive = true;
    }
    
    /**
     * Gets the isAtive boolean determining whether the Actuator is on or off.
     * @return The isActive boolean, whether the Actuator is currently on or off
     */
    public boolean isActive(){        
        return isActive;
    }
    
    /**
     * Sets the Actuator isActive attribute to false
     * meaning the Actuator is currently not active.
     */
    public void deactivate(){
        isActive = false;
    }
}
