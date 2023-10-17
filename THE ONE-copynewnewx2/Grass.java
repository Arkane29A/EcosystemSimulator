import java.util.List;
import java.util.Random;

/**
 * A model of a Grass.
 * Grass age, move, eat impalas, and die.
 * 
 * @author Saadh Zahid and Kabir Suri
 * @version 2022.03.02(2)
 */
public class Grass extends Plant implements Actor
{
    // Characteristics shared by all grass (class variables).

    // The age at which a Grass can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a Grass can live.
    private static final int MAX_AGE = 35;
    // The likelihood of a Grass breeding.
    private static final double BREEDING_PROBABILITY = 0.035;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The grass's age.
    private int age;

    /**
     * Create a Grass. A Grass is created with a random age and food level.
     * 
     * @param randomAge If true, the Grass will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the Grass does, it has an age and spreads out,
     * it gets eaten by other animals such as bunny
     */
    public void act(List<Actor> newGrass)
    {
        incrementAge();
        Field field = getField();

        if(isAlive()) {
            giveBirth(newGrass);            
            // Try to move into a free location.
        }
        else 
        {
            // Overcrowding.
            setDead();
        }
        }
    

    /**
     * Increase the age.
     * This could result in the grass dying out
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) { //if age is over max age the grass dies
            setDead();
        }
    }
    
    /**
     * Check whether or not this Grass is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newgrass A list to return newly born grass.
     */
    private void giveBirth(List<Actor> newGrass)
    {
        // New grass are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(false, field, loc);
            newGrass.add(young);
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
     * A grass can breed if it has reached the breeding age.
     * @return true if the grass can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}

