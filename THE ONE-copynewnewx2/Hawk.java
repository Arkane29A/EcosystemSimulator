import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * A model of a Hawk.
 * Hawk age, move, eat mouse, and die.
 * 
 * @author Saadh Zahid and Kabir Suri
 * @version 2022.03.02(2)
 */
public class Hawk extends Animal implements Actor
{
    // instance variables - replace the example below with your own
    // The age at which a Hawk can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a Hawk can live.
    private static final int MAX_AGE = 520;
    // The likelihood of a Hawk breeding.
    private static final double BREEDING_PROBABILITY = 0.33;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 50;
    // The food value of a single mouse. In effect, this is the
    // number of steps a Hawk can go before it has to eat again.
    private static final int PREY_FOOD_VALUE = 11;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The Hawk's age.
    private int age;
    private int foodLevel;
    // The Hawk's food level, which is increased by eating mouse's.
    private int time;

    /**
     * Create a Hawk. A Hawk is created with a random age and food level.
     * 
     * @param randomAge If true, the wolves will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hawk(boolean randomAge, Field field, Location location)
    {
        // initialise instance variables

        super(field, location);
        setTimeBehaviour();

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
     * Increase the age. This could result in the Hawk's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead(); //if age is over MAX_AGE variable then animal dies
        }
    }

    /**
     * Make this Hawk more hungry. This could result in the Hawk's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead(); //needs to eat in the given time frame or it dies.
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
     * This is what the Hawk does most of the time: it hunts for
     * mouse's. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHawk A list to return newly born wolves.
     */
    public void act(List<Actor> newHawks)
    {
        incrementAge();
      

        for (int i = 0; i==24; i++) {
            time+=1;
            //hawks inactive daytime 0-12, will not move or hunt during those times

            if (time >24)
            {
                time =0;
            }

            else
            {
                if (time <12)

                {
                    incrementHunger();

                    if (findMate() == true)
                    {
                        if(isAlive()) {
                            giveBirth(newHawks);            
                            // Move towards a source of food if found.
                            Location newLocation = findFood();
                            if(newLocation == null) { 
                                // No food found - try to move to a free location.
                                newLocation = getField().freeAdjacentLocation(getLocation());
                            }
                            // See if it was possible to move.
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
                            if (isAlive())
                            {
                                giveBirth(newHawks);
                            }
                        }
                    }
                }

                else
                {
                    Location newLocation = findFood();
                    if(newLocation == null) { 
                        // No food found - try to move to a free location.
                        newLocation = getField().freeAdjacentLocation(getLocation());
                    }
                    // See if it was possible to move.
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
    
    /**
     * Look for mouse adjacent to the current location.
     * Only the first live mouse is eaten.
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
            if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if(mouse.isAlive()) { 
                    mouse.setDead();
                    foodLevel = PREY_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }     
    
    /**
     * Check whether or not this Hawk is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHawk A list to return newly born Hawks.
     */
    private void giveBirth(List<Actor> newHawks)
    {
        // New Hawk are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Hawk young = new Hawk(false, field, loc);
            newHawks.add(young);
        }
    }
    
    /**
     * A Hawk can breed if it has reached the breeding age.
     * @return true if the Hawk can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
