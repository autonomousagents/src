
import java.util.Arrays;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class StateRepresentation {
    private double stateRep[][];
    public static final int stateRepWidth = Math.round(Environment.WIDTH/2)+1;
    public static final int stateRepHeight = Math.round(Environment.HEIGHT/2)+1;
    
    public StateRepresentation ( ){
        stateRep = new double[stateRepHeight][stateRepWidth];
        Arrays.fill(stateRep, 0.0);
    }

    public double getVvalue(int vertDistance, int horDistance){
    	return stateRep [vertDistance][horDistance];
    }

    public int[] getRelDistance(Position predator, Position prey){
    	int [] relativeDistance = new int[2];
    	//horizontal Distance
    	relativeDistance[0] = Math.abs(prey.getX()-predator.getX());
    	if(relativeDistance[0] > (Environment.WIDTH)/2) relativeDistance[0] = Environment.WIDTH - relativeDistance[0];
    	//vertical Distance
    	relativeDistance[1] = Math.abs(prey.getY()-predator.getY());
    	if(relativeDistance[1] > (Environment.HEIGHT)/2) relativeDistance[1] = Environment.HEIGHT - relativeDistance[1];
    	return relativeDistance;
    }

    public void setVvalue(int horDistance, int vertDistance, double value){
        stateRep[vertDistance][horDistance] = value;
    }

}
