import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A model of a wolf.
 * wolf age, move, eat bunnys, and die.
 * 
 * @author Saadh Zahid and Kabir Suri
 * @version 2022.03.02(2)
 */
public class Wolf extends Animal implements Actor
{
    // Characteristics shared by all wolf (class variables).

    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a wolf can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.11;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 13;
    // The food value of a single bunnys. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int BUNNY_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    // The wolf's age.
    private int age;
    // The wolf's food level, which is increased by eating bunnys.
    private int foodLevel;

    /**
     * Create a wolf. A wolf is created with a random age and food level.
     * 
     * @param randomAge If true, the wolves will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(BUNNY_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = BUNNY_FOOD_VALUE;
        }
    }

    /**
     * This is what the wolf does most of the time: it hunts for
     * bunny's. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newWolf A list to return newly born wolves.
     */
    public void act(List<Actor> newWolf)
    {
        incrementHunger(); // increases the hunger from method, if hunger passes limit it dies
        incrementAge(); //similar to hunger if age goes over its limit it dies

        if (isAlive()) 
        {

            if (findMate() == true)
            {
                giveBirth(newWolf);


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
                    // Overcrowding.
                    setDead();
                }
            }
        }

    }
    
    
    /**
     * Increase the age. This could result in the wolf's death.
     */
    private void incrementAge()
    {
        age++; //if age is over MAX_AGE variable then animal dies
        if(age > MAX_AGE)  { 
            setDead();
        }
    }

    /**
     * Make this wolf more hungry. This could result in the wolf's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) { 
            setDead();
        }
    }

    /**
     * Look for bunnys adjacent to the current location.
     * Only the first live bunnys is eaten.
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
            if(animal instanceof Bunny) {
                Bunny bunny = (Bunny) animal;
                if(bunny.isAlive()) { 
                    bunny.setDead(); //if bunny is on adjacent tile impala will be eaten and the tile will be taken over
                    foodLevel = BUNNY_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newWolves A list to return newly born wolfes.
     */
    private void giveBirth(List<Actor> newWolves)
    {
        // New wolves are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, field, loc);
            newWolves.add(young);
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
     * A wolfs can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
