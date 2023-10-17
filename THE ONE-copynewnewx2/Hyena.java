import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * A model of a Hyena.
 * Hyena age, move, eat impalas, and die.
 * 
 * @author Saadh Zahid and Kabir Suri
 * @version 2022.03.02(2)
 */
public class Hyena extends Animal implements Actor
{
    // The age at which a Hyena can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a Hyena can live.
    private static final int MAX_AGE = 65;
    // The likelihood of a Hyena breeding.
    private static final double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single Impala. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int PREY_FOOD_VALUE = 10;
    
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    //Hyena's age
    private int age;
    //Hyena's food level
    private int foodLevel;
    //time of hyena, only hunt during certain times of the day
    private int time; 
    
    /**
     * Create a Hyena. A Hyena is created with a random age and food level.
     * 
     * @param randomAge If true, the Hyena will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hyena(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables
        super(field, location);

        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PREY_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PREY_FOOD_VALUE;

        }

    }
    
    /**
     * Increase the age. This could result in the hyena's death.
     */
    private void incrementAge()
    {
        age++; 
        if(age > MAX_AGE) {
            setDead(); //if age is over max age the animal dies
        }
    }

    /**
     * Make this Hyena more hungry. This could result in the Hyena's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
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
     * This is what the Hyena does most of the time: it hunts for
     * Impala's. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHyenas A list to return newly born wolves.
     */
    public void act(List<Actor> newHyenas)
    {
        incrementAge(); // increases the age
        
        time +=1;

        if (time >24)
        {
            time =0;
        }
        else

        //hyenas inactive between 4-15
        if (time >=4 && time <=15 )
        {
            //period of inactivity, only gives birth
            if(isAlive())
            {
                giveBirth(newHyenas);
            }

        }

        else
        {
            {
                incrementHunger(); // increases the hunger from method, if hunger passes limit it dies
                if (isAlive()) 
                {
                                     

                    if (findMate() == true) //only breeds if there it is next to a tile with the opposite gender
                    {
                        giveBirth(newHyenas);

                        Location newLocation = getField().freeAdjacentLocation(getLocation());
                        if(newLocation != null) {
                            setLocation(newLocation);
                        }
                        else {
                            // Overcrowding - no space for it to breed and place a new animal on tile
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
        }

    }
    
    /**
     * Look for impala's adjacent to the current location.
     * Only the first live impala is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField(); 
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator(); //created into an iterator to easily implement the remove while its running
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Impala) {
                Impala impala = (Impala) animal;
                if(impala.isAlive()) { 
                    impala.setDead(); //if impala is on adjacent tile impala will be eaten and the tile will be taken over
                    foodLevel = PREY_FOOD_VALUE;
                    return where;
                }

            }
        }
        return null;
    }     

    /**
     * Check whether or not this Hyena is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHyenas A list to return newly born hyenas.
     */
    private void giveBirth(List<Actor> newHyenas)
    {
        // New Hyenas are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Hyena young = new Hyena(false, field, loc);
            newHyenas.add(young);
        }
    }

    /**
     * A Hyena can breed if it has reached the breeding age.
     * @return true if the Hyena can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }

}
