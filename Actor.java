import java.util.List;

/**
* The interface to be extended by any class wishing to participate
* in the simulation
*/    
public interface Actor 
{
    /**
    * The interface to be extended by any class wishing to participate
    * in the simulation
    */ 
    abstract void act(List<Actor> newActors);
    
    /**
    * Returns if the actor is alive or not
    * in the simulation
    */  
    abstract boolean isAlive();
    
    /**
    * Returns if the actor is alive or not
    * in the simulation
    */  
    abstract boolean timeaffected();
    
    /**
    * Returns if the actor is alive or not
    * in the simulation
    */  
    abstract void setDead();
    
    /**
    * Returns if the actor is alive or not
    * in the simulation
    */  
    abstract Location getLocation();
    
    /**
    * Returns if the actor is alive or not
    * in the simulation
    */  
    abstract void setLocation(Location location);
    
    /**
    * Returns if the actor is alive or not
    * in the simulation
    */  
    abstract Field getField();
    
    // /**
    // * Returns if the actor is alive or not
    // * in the simulation
    // */  
    // abstract void simulateOneStep();
    
    // recommment all of this
}
