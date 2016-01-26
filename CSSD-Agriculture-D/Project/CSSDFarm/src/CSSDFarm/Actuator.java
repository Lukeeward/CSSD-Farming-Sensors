package CSSDFarm;

public class Actuator {
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
