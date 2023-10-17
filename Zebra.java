// import java.util.Random;
// import java.util.List;
// import java.util.Iterator;

// /**
 // * Write a description of class impala here.
 // *
 // * @author (your name)
 // * @version (a version number or a date)
 // */
// public class Zebra extends Animal implements Actor
// {

  // // Characteristics shared by all Imapalas (class variables).

    // // The age at which a rabbit can start to breed.
    // private static final int BREEDING_AGE = 5;
    // // The age to which a rabbit can live.
    // private static final int MAX_AGE = 40;
    // // The likelihood of a rabbit breeding.
    // private static final double BREEDING_PROBABILITY = 0.15;
    // // The maximum number of births.
    // private static final int MAX_LITTER_SIZE = 8;
    // // A shared random number generator to control breeding.
    // private static final Random rand = Randomizer.getRandom();
    
    // private static final int GRASS_FOOD_VALUE = 9;
        
    // // The rabbit's age.
    // private int age;
    
    // private int foodLevel;


    // /**
     // * Constructor for objects of class impala
     // */
    // public Zebra(boolean randomAge, Field field, Location location)
    // {
        // // initialise instance variables
        // super(field, location);
        // age = 0;
        
        // if(randomAge) {
            // age = rand.nextInt(MAX_AGE);
        // }

// }


    // public void act(List<Actor> newZebra)
    // {
        // incrementAge();
        // if(isAlive()) {
            // giveBirth(newZebra);            
            // // Try to move into a free location.
            // Location newLocation = getField().freeAdjacentLocation(getLocation());
            // if(newLocation != null) {
                // setLocation(newLocation);
            // }
            // else {
                // // Overcrowding.
                // setDead();
            // }
        // }
    // }

    // private void incrementAge()
    // {
        // age++;
        // if(age > MAX_AGE) {
            // setDead();
        // }
    // }
    
    // /**
     // * Check whether or not this rabbit is to give birth at this step.
     // * New births will be made into free adjacent locations.
     // * @param newRabbits A list to return newly born rabbits.
     // */
    // private void giveBirth(List<Actor> newZebra)
    // {
        // // New rabbits are born into adjacent locations.
        // // Get a list of adjacent free locations.
        // Field field = getField();
        // List<Location> free = field.getFreeAdjacentLocations(getLocation());
        // int births = breed();
        // for(int b = 0; b < births && free.size() > 0; b++) {
            // Location loc = free.remove(0);
            // Zebra young = new Zebra(false, field, loc);
            // newZebra.add(young);
        // }
    // }

    // private int breed()
    // {
        // int births = 0;
        // if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            // births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        // }
        // return births;
    // }

    
   // private boolean canBreed()
    // {
        // return age >= BREEDING_AGE;
    // }

    // private Location findFood()
    // {
        // Field field = getField();
        // List<Location> adjacent = field.adjacentLocations(getLocation());
        // Iterator<Location> it = adjacent.iterator();
        // while(it.hasNext()) {
            // Location where = it.next();
            // Object animal = field.getObjectAt(where);
            // if(animal instanceof Grass) {
                // Grass grass = (Grass) animal;
                // if(grass.isAlive()) { 
                    // grass.setDead();
                    // foodLevel = GRASS_FOOD_VALUE;
                    // return where;
                // }
            // }
        // }
        // return null;
    // }
    
// }