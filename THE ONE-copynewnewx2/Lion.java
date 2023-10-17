import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * Write a description of class lion here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Lion extends Animal implements Actor
{
    // instance variables - replace the example below with your own
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 350;

    private static final double BREEDING_PROBABILITY = 0.32;

    private static final int MAX_LITTER_SIZE = 50;

    private static final int PREY_FOOD_VALUE = 10;

    private static final Random rand = Randomizer.getRandom();

    private int age;
    private int foodLevel;

    private int time;

    /**
     * Constructor for objects of class lion
     */
    public Lion(boolean randomAge, Field field, Location location)
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

    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
            //System.out.println("BITCH");

        }
    }

    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    public void act(List<Actor> newLions)
    {
        incrementAge();

        for (int i = 0; i==24; i++) {
            time+=1;
            //lions inactive daytime 0-12, will not move or hunt during those times

            if (time >24)
            {
                time = 0;
            }

            else
            {
                if (time >12)

                {
                    incrementHunger();

                    if (isAlive()) 
                    {

                        if (findMate() == true)
                        {
                            giveBirth(newLions);

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
            }
        }
    }

    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Impala) {
                Impala impala = (Impala) animal;
                if(impala.isAlive()) { 
                    impala.setDead();
                    foodLevel = PREY_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }     

    private void giveBirth(List<Actor> newLions)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(false, field, loc);
            newLions.add(young);
        }
    }

    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
