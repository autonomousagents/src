/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

import java.util.Arrays;

/**
 * This class is used by an Environment object who controls the turns of 
 * the predator and the prey. It needs a certain type of predator 
 * as input which determines the policy, and thus the moves, of the predator. 
 * 
 * PredatorValueIteration is one type of such a predator, 
 * and it follows the Value Iteration policy. Value Iteration calculates 
 * the expected reward of the action that maximizes this expectation
 * for each possible state, in which all possible next states are 
 * taken into account. Then the predator does the move 
 * with the highest expected reward, according to these calculations.
 */
public class PredatorValueIteration implements Agent {
	private static final int nrActions = 5;
    private double v[][][][] = new double[Environment.HEIGHT][Environment.WIDTH][Environment.HEIGHT][Environment.WIDTH];
    private double vNew[][][][];
    private double theta, gamma;
    private Position myPos;

    /**
     * This is the constructor of the class that mainly initializes
     * variables. It also starts the calculations of all V-values
     * and prints out the result. Finally, it creates a Matlab document
     * with which it is easy to construct a corresponding color map.
     */
    public PredatorValueIteration(Position startPos) {
        myPos = startPos;
        setParams(0.0,0.9);
        fillV();
        calcV();
        printTable(); // Print table for prey at (5,5)
        View.writeVMatrix("v-matrixValueIteration.m", partialMatrix());
    }

    /**
     * This method calculates all V-values following the Value Iteration algorithm,
     * as mentioned in Sutton & Barto - Reinforcement Learning: An Introduction
     * in Section 4.4
     */
	private void calcV() {
        double delta;
        double[] vPerAction= new double [nrActions];
        int nrIterations = 0;
        do {
            delta = 0.0;
            vNew = new double[Environment.HEIGHT][Environment.WIDTH][Environment.HEIGHT][Environment.WIDTH];
            for(int i = 0; i < Environment.HEIGHT; i++) {
                for(int j = 0; j < Environment.WIDTH; j++) {
                    for(int k = 0; k < Environment.HEIGHT; k++) {
                        for(int l = 0; l < Environment.WIDTH; l++) {
                            if(!(i == k && j == l)) {
                                Arrays.fill(vPerAction,0);
                                vPerAction = calcVPerAction(i,j,k,l);
                                vNew[i][j][k][l] = getMaximum(vPerAction);
                                double difference = Math.abs(vNew[i][j][k][l]-v[i][j][k][l]);
                                if(difference > delta) {
                                    delta = difference;
                                }
                            }
                            else {
                                vNew[i][j][k][l] = 0.0;
                            }
                        }
                    }
                }
            }
            v = vNew;
            nrIterations++;
        } while(delta > theta);
        System.out.println("==> Value Iteration: nr iterations until convergence = " + nrIterations);
    }

	/**
	 * This method calculates the V-value for each possible action given
	 * the position of the predator and the position of the prey.
	 * @param xPred	The X-coordinate of the predator
	 * @param yPred	The Y-coordinate of the predator
	 * @param xPrey The X-coordinate of the prey
	 * @param yPrey The Y-coordinate of the prey
	 * @return		An array of the V-value of each possible action
	 */
    private double[] calcVPerAction(int xPred, int yPred, int xPrey, int yPrey) {
        Position[] actionsPred = getPostions(new Position(xPred, yPred));
        Position[] actionsPrey;
        double[] vPerAction = new double[nrActions];
        double[] pResultingStates;
        double vkP1; //V k+1 per action
        
        // For each resulting position of possible actions of the predator
        for(int i = 0; i < nrActions; i++) {
            vkP1 = 0.0;
            // For each possible action of the prey determining s'
            actionsPrey = getPostions(new Position(xPrey, yPrey));
            pResultingStates = probabilities(actionsPred[i],actionsPrey);
            for(int j = 0; j < nrActions; j++) {
                // V k(s')
                double vk = v[actionsPred[i].getX()][actionsPred[i].getY()][actionsPrey[j].getX()][actionsPrey[j].getY()];
                // Calculate v k+1 (s) per action
                vkP1 += pResultingStates[j] * (Environment.reward(actionsPrey[j], actionsPred[i]) + gamma * vk);
            }
            vPerAction[i] = vkP1;
        }
        
        return vPerAction;
    }
    
    /**
     * This method gives the probabilities of each possible action of the prey to occur.
     * @param predator	The position of the predator
     * @param prey		
     * @return			The probabilities of each action of the prey in an array
     */
    private double[] probabilities(Position predator, Position[] prey) {
        double[] probabilities = {0.05,0.05,0.05,0.05,0.8};
        int onPredator = -1;
        // Check for each action if the prey steps on the predator
        for(int i = 0; i < nrActions-1; i++){
            // If the prey steps on the predator set the probability of that state to zero
            if(prey[i].getX() == predator.getX() && prey[i].getY() == predator.getY()) {
                onPredator = i;
                probabilities[i]=0.0;
            }
        }
        // If necessary adjust the resulting probabilities
        if(onPredator != -1) {
            for(int i = 0; i < nrActions-1; i++) {
                if(i!=onPredator)
                    probabilities[i] = 0.2/3.0;
            }
        }
        if(predator.getX() == prey[nrActions-1].getX() && predator.getY() == prey[nrActions-1].getY()) {
            double[] pr = {0.0,0.0,0.0,0.0,1.0};
            probabilities = pr;
        }
        return probabilities;
    }

    /**
     * This method calculates from a certain position the neighboring positions that would
     * result from each of the actions the predator can take.
     * @param p		The current position of the predator
     * @return		An array of possible new positions
     */
    private Position[] getPostions(Position p) {
        Position[] pos  = new Position[5];
        //Up
        pos[0] = new Position(p.getX(), (p.getY()+Environment.HEIGHT-1)% Environment.HEIGHT);
        //Right
        pos[1] = new Position((p.getX() +Environment.WIDTH+1) % Environment.WIDTH, p.getY());
        //Down
        pos[2] = new Position(p.getX(), (p.getY()+Environment.HEIGHT+1)% Environment.HEIGHT);
        //Left
        pos[3] = new Position((p.getX() +Environment.WIDTH-1) % Environment.WIDTH, p.getY());
        //Wait
        pos[4] = new Position(p.getX(),p.getY());
        return pos;
    }

    /**
     * This is a method inherited by Agent. It gets the position of
     * the prey and calculates which actions it can take. It then 
     * chooses the best one and changes its position.
     */
    @Override
    public void doMove(Position other) {
        Position[] possibleMoves = getPostions(myPos);
        int maxAction = getBestAction(possibleMoves, other);
        myPos = possibleMoves[maxAction];
    }
    
    /**
     * This method chooses the best move the predator can do 
     * according to the maximum V-value of all possible actions 
     * it can take, given its possible moves from its current 
     * position and the position of the prey.
     * @param possibleMoves	An array of positions that would
     * 						result from each possible move
     * @param prey			The position of the prey
     * @return				The index of the best next position 
     * 						from possibleMoves	
     */
    private int getBestAction(Position[] possibleMoves, Position prey) {
        int xPrey = prey.getX();
        int yPrey = prey.getY();
        int maxAction = -1;
        double maxV = 0.0;
        for(int i = 0; i < nrActions; i++) {
            int xPred = possibleMoves[i].getX();
            int yPred = possibleMoves[i].getY();
            if(v[xPred][yPred][xPrey][yPrey] > maxV) {
                maxAction = i;
                maxV = v[xPred][yPred][xPrey][yPrey];
            }
        }
        return maxAction;
    }
    
    /**
     * This method returns the current position of the predator.
     */
    @Override
    public Position getPos() {
        return myPos;
    }

    /**
     * This method fills the array of V-values with zeros.
     */
    private void fillV() {
        for(int i = 0; i < Environment.HEIGHT; i++) {
            for(int j = 0; j < Environment.WIDTH; j++) {
                for(int k = 0; k < Environment.HEIGHT; k++) {
                    Arrays.fill(v[i][j][k], 0.0);
                }
            }
        }
    }

    /**
     * This method sets the values of theta and gamma,
     * needed for the Value Iteration algorithm.
     * @param theta	The value for which amount of change 
     * 				in the V-values the Value Iteration 
     * 				algorithm will terminate.
     * @param gamma	The discount factor
     */
    private void setParams(double theta, double gamma) {
    	this.theta = theta;
        this.gamma = gamma;
    }

    /**
     * This method takes an array of double values and returns
     * the highest value in that array.
     * @param values	An array of double values
     * @return			The highest double value of the give array
     */
    private double getMaximum(double[] values) {
        double max = values[0];
        for(int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    /**
     * This method gives a visual representation of all the V-values
     * in the console, in which the prey is located at position (5,5).
     */
    private void printTable() {
        System.out.println("The values of all states in which the prey is located at (5,5), in grid representation: ");
        for(int j = 0; j < Environment.WIDTH; j++) {
            for(int i = 0; i < Environment.HEIGHT; i++) {
                System.out.format("%7.3f",v[i][j][5][5]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
    
    /**
     * This method gives the V-values for when the prey is at position (5,5).
     * This is used to compute a visual representation of the V-values in a color map.
     * @return	A two-dimensional array of V-values for which the prey is
     * 			at position (5,5).
     */
    private double[][] partialMatrix() {
		double [][] p = new double[Environment.HEIGHT][Environment.WIDTH];
		for(int i = 0; i < Environment.HEIGHT; i++) {
			for(int j = 0; j < Environment.WIDTH; j++) {
				p[i][j] = v[i][j][5][5];
			}
		}
		return p;
	}

}
