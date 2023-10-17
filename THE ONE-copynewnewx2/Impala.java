import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * A model of a Impala.
 * Impala age, move, breed and die.
 * 
 * @author Saadh Zahid and Kabir Suri
 * @version 2022.03.02(2)
 */

public class Impala extends Animal implements Actor
{

    // Characteristics shared by all Imapalas (class variables).

    // The age at which a Imapala can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a Imapala can live.
    private static final int MAX_AGE = 42;
    // The likelihood of a Imapala breeding.
    private static final double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    
    private static final int GRASS_FOOD_VALUE = 7;

    
    
    // The Impala's age.
    private int age;
    //Impala's food level
    private int foodLevel;

    /**
     * Create a new Impala. A Impala is created with a random age.
     * 
     * @param randomAge If true, the Impala will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Impala(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);
        age = 0;

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }

    }

    /**
     * Make this Impala more hungry. This could result in the Hyena's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) { //if age is over max age the animal dies
            setDead();
        }
    }
    
    
    /**
     * This is what the Impala does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newImpalas A list to return newly born Impalas.
     * @param field The field currently occupied.
     */
    public void act(List<Actor> newImpalas)
    {
        incrementAge();
        //always increment age before acting as it can die and would affect the simulation

        if (isAlive()) 
        {

            if (findMate() == true)
            {
                giveBirth(newImpalas); //only breeds if there it is next to a tile with the opposite gender


                Location newLocation = getField().freeAdjacentLocation(getLocation());
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }

            else
            {
                Location newLocation = getField().freeAdjacentLocation(getLocation());
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    // Overcrowding - no space for it to breed and place a new animal on tile
                    setDead();
                }
            }
        }
        
    }
    

    /**
     * Look for grass adjacent to the current location.
     * Only the first live impala is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Grass) {
                Grass grass = (Grass) animal;
                if(grass.isAlive()) { 
                    grass.setDead();
                    foodLevel = GRASS_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Increase the age.
     * This could result in the Impala's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether or not this Impala is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newImpala A list to return newly born Impalas.
     */
    
    private void giveBirth(List<Actor> newImpala)
    {
        // New Impalas are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Impala young = new Impala(false, field, loc);
            newImpala.add(young);
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /**
     * A Impala can breed if it has reached the breeding age.
     * @return true if the Impala can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }

}