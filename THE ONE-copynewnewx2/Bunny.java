import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a Bunny.
 * Bunnys age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Bunny extends Animal implements Actor
{
    // Characteristics shared by all Bunnys (class variables).

    // The age at which a Bunny can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a Bunny can live.
    private static final int MAX_AGE = 80;
    // The likelihood of a Bunny breeding.
    private static final double BREEDING_PROBABILITY = 0.038;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    private static final int GRASS_FOOD_VALUE = 6;

    // Individual characteristics (instance fields).

    // The Bunny's age.
    private int age;
    //Bunny's food level
    private int foodLevel;

    /**
     * Create a new Bunny. A bunny is created with a random age.
     * 
     * @param randomAge If true, the bunny will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Bunny(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * Make this Bunny more hungry. This could result in the Hyena's death.
     */
        private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * This is what the bunny does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newBunnys A list to return newly born bunnys.
     * @param field The field currently occupied.
     */
    public void act(List<Actor> newBunnys)
    {

        incrementAge(); // increases the age

        if (isAlive()) 
        {

            if (findMate() == true) //only breeds if there it is next to a tile with the opposite gender
            {
                giveBirth(newBunnys);


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
     * Increase the age.
     * This could result in the bunny's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) { //if age is over max age the animal dies
            setDead();
        }
    }

    /**
     * Check whether or not this Bunny is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBunnys A list to return newly born Bunnys.
     */
    private void giveBirth(List<Actor> newBunny)
    {
        // New bunnys are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Bunny young = new Bunny(false, field, loc);
            newBunny.add(young);
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
     * A bunny can breed if it has reached the breeding age.
     * @return true if the bunny can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
