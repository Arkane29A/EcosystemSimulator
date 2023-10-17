import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.08;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.09;
    
    private static final double IMPALA_CREATION_PROBABILITY = 0.04;
    
    private static final double ZEBRA_CREATION_PROBABILITY = 0.02;
    
    //probability that a lion will be created in any given given grid position
    
    private static final double LION_CREATION_PROBABILITY = 0.15;
    
    //probability that a Hyena will be created in any given grid position
    
    private static final double HYENA_CREATION_PROBABILITY = 0.1;
    
    private static final double HAWK_CREATION_PROBABILITY = 0.1;
    
    private static final double MOUSE_CREATION_PROBABILITY = 0.2;
    
    private static final double GRASS_CREATION_PROBABILITY = 0.01;
        
    
    // List of animals in the field.
    private List<Actor> animals;
    
    private List<Actor> Plant;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Bunny.class, Color.ORANGE);
        view.setColor(Wolf.class, Color.BLUE);
        view.setColor(Lion.class, Color.RED);
        view.setColor(Impala.class, new Color(237, 210, 121));
        view.setColor(Hyena.class, Color.YELLOW);
        view.setColor(Hawk.class, Color.CYAN);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Mouse.class, new Color(222, 151, 75));
        
        
        
        
        
        
        
        view.displaytime();
        
        
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(1000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
             //delay(40);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
            
        //assume one step = day
        
        
    
        // Provide space for newborn animals.
        List<Actor> newAnimals = new ArrayList<>();        
        // Let all rabbits act.
        for(Iterator<Actor> it = animals.iterator(); it.hasNext(); ) {
            Actor animal = it.next();
            animal.act(newAnimals);
            if(! animal.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, field);
        }
    
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(true, field, location);
                    animals.add(wolf);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Bunny bunny = new Bunny(true, field, location);
                    animals.add(bunny);
                }
                
                else if (rand.nextDouble() <= LION_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, field, location);
                    animals.add(lion);
                }
                
                
                else if (rand.nextDouble() <= IMPALA_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Impala impala = new Impala(true, field, location);
                    animals.add(impala);
                }
                
                else if (rand.nextDouble() <= HYENA_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Hyena hyena = new Hyena(true, field, location);
                    animals.add(hyena);
                }
                
                
                else if (rand.nextDouble() <= HAWK_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Hawk hawk = new Hawk(true, field, location);
                    animals.add(hawk);
                }
                
            
                else if (rand.nextDouble() <= MOUSE_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(true, field, location);
                    animals.add(mouse);
                }
            
                
                else if (rand.nextDouble() <= GRASS_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(true, field, location);
                    animals.add(grass);
                }
                
                // else leave the location empty.   
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
