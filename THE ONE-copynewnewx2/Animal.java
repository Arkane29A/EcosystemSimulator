import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal implements Actor
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    private boolean timebehaviour;
    
    
    private int BREEDING_AGE;
    private int MAX_AGE;
    
    private int female;
    
    private int age;
    
    private static final Random rand = Randomizer.getRandom();
    
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        timebehaviour = false;
        
        female = rand.nextInt(2);
        
        //0 female
        // 1 male
        
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Actor> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }
    
    
    public boolean timeaffected()
    {
        return timebehaviour;
    }
    
    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    
    public boolean isFemale()
    {
        if (female == 0)
        {
            return true;             
        }
        
        else
        {
            return false;
        }
        
    }
    
    public void setTimeBehaviour()
    {
        timebehaviour = true;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    public Field getField()
    {
        return field;
    }
    
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    public boolean findMate() {
        Field field = getField();
        if (this.getLocation() == null) {
            return false;
        }
        List<Location> adjacent = field.adjacentLocations(this.getLocation());
        for (Location where : adjacent) {
            if (field.getObjectAt(where) != null) {
                if (!(field.getObjectAt(where) instanceof Plant)) {
                    Animal speciesInNextCell = (Animal) field.getObjectAt(where);
                    if (speciesInNextCell != null && this.getClass().equals(speciesInNextCell.getClass()) && speciesInNextCell.isFemale() != this.isFemale() && speciesInNextCell.canBreed()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    //give birth or act
}
